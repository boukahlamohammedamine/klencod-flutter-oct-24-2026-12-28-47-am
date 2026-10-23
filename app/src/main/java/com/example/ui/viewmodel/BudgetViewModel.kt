package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.db.AppDatabase
import com.example.data.model.BudgetCategory
import com.example.data.model.SavingsGoalEntity
import com.example.data.model.TransactionEntity
import com.example.data.repository.BudgetRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

enum class CurrencyDisplayMode(val labelArabic: String) {
    BOTH("دينار وسنتيم"),
    DZD_ONLY("دينار (دج)"),
    CENTIMES_ONLY("سنتيم")
}

data class CategorySummary(
    val category: BudgetCategory,
    val totalDzd: Double,
    val percentageOfTotalExpenses: Float,
    val transactionCount: Int
)

data class BudgetUiState(
    val currentMonthKey: String = "",
    val availableMonths: List<String> = emptyList(),
    val transactions: List<TransactionEntity> = emptyList(),
    val filteredTransactions: List<TransactionEntity> = emptyList(),
    val savingsGoals: List<SavingsGoalEntity> = emptyList(),
    val totalIncomeDzd: Double = 0.0,
    val totalExpensesDzd: Double = 0.0,
    val balanceDzd: Double = 0.0,
    val spentPercentage: Float = 0f,
    val categorySummaries: List<CategorySummary> = emptyList(),
    val currencyDisplayMode: CurrencyDisplayMode = CurrencyDisplayMode.BOTH,
    val selectedCategoryFilter: String? = null,
    val searchQuery: String = "",
    val isLoading: Boolean = false
)

private data class FilterConfig(
    val monthKey: String,
    val currencyMode: CurrencyDisplayMode,
    val categoryFilter: String?,
    val query: String
)

class BudgetViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: BudgetRepository

    private val currentMonthKeyFlow = MutableStateFlow(
        SimpleDateFormat("yyyy-MM", Locale.US).format(Date())
    )
    private val currencyModeFlow = MutableStateFlow(CurrencyDisplayMode.BOTH)
    private val categoryFilterFlow = MutableStateFlow<String?>(null)
    private val searchQueryFlow = MutableStateFlow("")

    init {
        val database = AppDatabase.getInstance(application)
        repository = BudgetRepository(
            transactionDao = database.transactionDao(),
            savingsGoalDao = database.savingsGoalDao(),
            monthlyBudgetDao = database.monthlyBudgetDao(),
            database = database
        )

        viewModelScope.launch {
            repository.checkAndSeedIfEmpty()
        }
    }

    private val filterConfigFlow: Flow<FilterConfig> = combine(
        currentMonthKeyFlow,
        currencyModeFlow,
        categoryFilterFlow,
        searchQueryFlow
    ) { month, currency, category, query ->
        FilterConfig(month, currency, category, query)
    }

    private val currentMonthTransactionsFlow: Flow<List<TransactionEntity>> =
        currentMonthKeyFlow.flatMapLatest { month ->
            repository.getTransactionsForMonth(month)
        }

    val uiState: StateFlow<BudgetUiState> = combine(
        currentMonthTransactionsFlow,
        repository.getAllSavingsGoals(),
        repository.getAllMonthKeys(),
        filterConfigFlow
    ) { transactions: List<TransactionEntity>,
        savings: List<SavingsGoalEntity>,
        allMonthKeys: List<String>,
        filters: FilterConfig ->

        val totalIncome = transactions.filter { it.isIncome }.sumOf { it.amountDzd }
        val totalExpenses = transactions.filter { !it.isIncome }.sumOf { it.amountDzd }
        val balance = totalIncome - totalExpenses
        val spentPct = if (totalIncome > 0.0) {
            (totalExpenses / totalIncome).toFloat().coerceIn(0f, 1f)
        } else {
            0f
        }

        // Category breakdown for expenses
        val expenseTransactions = transactions.filter { !it.isIncome }
        val groupedByCategory = expenseTransactions.groupBy { it.categoryId }
        val summaries = groupedByCategory.map { (catId, txList) ->
            val catTotal = txList.sumOf { it.amountDzd }
            val catPct = if (totalExpenses > 0.0) (catTotal / totalExpenses).toFloat() else 0f
            CategorySummary(
                category = BudgetCategory.fromId(catId),
                totalDzd = catTotal,
                percentageOfTotalExpenses = catPct,
                transactionCount = txList.size
            )
        }.sortedByDescending { it.totalDzd }

        // Filter transactions by search query and category
        val filtered = transactions.filter { tx ->
            val matchesCategory = filters.categoryFilter == null || tx.categoryId == filters.categoryFilter
            val matchesQuery = filters.query.isBlank() ||
                    tx.title.contains(filters.query, ignoreCase = true) ||
                    tx.notes.contains(filters.query, ignoreCase = true)
            matchesCategory && matchesQuery
        }

        // Available months should always include current month
        val monthsList = (allMonthKeys + filters.monthKey).distinct().sortedDescending()

        BudgetUiState(
            currentMonthKey = filters.monthKey,
            availableMonths = monthsList,
            transactions = transactions,
            filteredTransactions = filtered,
            savingsGoals = savings,
            totalIncomeDzd = totalIncome,
            totalExpensesDzd = totalExpenses,
            balanceDzd = balance,
            spentPercentage = spentPct,
            categorySummaries = summaries,
            currencyDisplayMode = filters.currencyMode,
            selectedCategoryFilter = filters.categoryFilter,
            searchQuery = filters.query,
            isLoading = false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = BudgetUiState(
            currentMonthKey = currentMonthKeyFlow.value,
            isLoading = true
        )
    )

    fun selectMonth(monthKey: String) {
        currentMonthKeyFlow.value = monthKey
    }

    fun toggleCurrencyDisplayMode() {
        currencyModeFlow.update { current ->
            when (current) {
                CurrencyDisplayMode.BOTH -> CurrencyDisplayMode.DZD_ONLY
                CurrencyDisplayMode.DZD_ONLY -> CurrencyDisplayMode.CENTIMES_ONLY
                CurrencyDisplayMode.CENTIMES_ONLY -> CurrencyDisplayMode.BOTH
            }
        }
    }

    fun setCurrencyDisplayMode(mode: CurrencyDisplayMode) {
        currencyModeFlow.value = mode
    }

    fun setCategoryFilter(categoryId: String?) {
        categoryFilterFlow.value = categoryId
    }

    fun setSearchQuery(query: String) {
        searchQueryFlow.value = query
    }

    fun addTransaction(
        title: String,
        amountDzd: Double,
        categoryId: String,
        isIncome: Boolean,
        dateMillis: Long = System.currentTimeMillis(),
        notes: String = ""
    ) {
        viewModelScope.launch {
            val monthKey = SimpleDateFormat("yyyy-MM", Locale.US).format(Date(dateMillis))
            repository.addTransaction(
                TransactionEntity(
                    title = title.trim(),
                    amountDzd = amountDzd,
                    categoryId = categoryId,
                    isIncome = isIncome,
                    dateMillis = dateMillis,
                    monthKey = monthKey,
                    notes = notes.trim()
                )
            )
        }
    }

    fun deleteTransaction(transaction: TransactionEntity) {
        viewModelScope.launch {
            repository.deleteTransaction(transaction)
        }
    }

    fun deleteTransactionById(id: Long) {
        viewModelScope.launch {
            repository.deleteTransactionById(id)
        }
    }

    fun addSavingsGoal(
        title: String,
        targetAmountDzd: Double,
        initialSavedDzd: Double = 0.0,
        note: String = "",
        iconKey: String = "savings"
    ) {
        viewModelScope.launch {
            repository.addSavingsGoal(
                SavingsGoalEntity(
                    title = title.trim(),
                    targetAmountDzd = targetAmountDzd,
                    savedAmountDzd = initialSavedDzd,
                    note = note.trim(),
                    iconKey = iconKey
                )
            )
        }
    }

    fun depositToSavingsGoal(goal: SavingsGoalEntity, depositAmountDzd: Double) {
        viewModelScope.launch {
            repository.depositToSavingsGoal(goal.id, goal, depositAmountDzd)
            addTransaction(
                title = "تغذية الحصالة: ${goal.title}",
                amountDzd = depositAmountDzd,
                categoryId = BudgetCategory.SAVINGS.id,
                isIncome = false,
                notes = "تحويل تلقائي لحصالة ${goal.title}"
            )
        }
    }

    fun deleteSavingsGoal(goal: SavingsGoalEntity) {
        viewModelScope.launch {
            repository.deleteSavingsGoal(goal)
        }
    }

    fun resetAllData() {
        viewModelScope.launch {
            repository.resetAllData()
        }
    }
}
