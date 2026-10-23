package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.CurrencyExchange
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.BudgetCategory
import com.example.data.model.TransactionEntity
import com.example.data.util.AlgerianCurrencyUtil
import com.example.ui.components.CurrencyDisplay
import com.example.ui.theme.AlgerianGold
import com.example.ui.theme.AlgerianGreen
import com.example.ui.theme.ExpenseColor
import com.example.ui.theme.IncomeColor
import com.example.ui.viewmodel.BudgetUiState
import com.example.ui.viewmodel.CurrencyDisplayMode
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    state: BudgetUiState,
    onSelectMonth: (String) -> Unit,
    onToggleCurrencyMode: () -> Unit,
    onSelectCategoryFilter: (String?) -> Unit,
    onSearchQueryChange: (String) -> Unit,
    onOpenAddTransaction: (isIncome: Boolean) -> Unit,
    onDeleteTransaction: (TransactionEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    var isSearchExpanded by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("dashboard_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Top Header: Month Bar and Currency Switcher
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Month Selector
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.CalendarMonth,
                        contentDescription = "الشهر",
                        tint = AlgerianGreen,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "شهر ${formatMonthArabic(state.currentMonthKey)}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // Currency Mode Switcher
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .clickable(onClick = onToggleCurrencyMode)
                        .testTag("currency_toggle_btn")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.CurrencyExchange,
                            contentDescription = "تبديل العملة",
                            tint = MaterialTheme.colorScheme.onSecondaryContainer,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = state.currencyDisplayMode.labelArabic,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }
                }
            }
        }

        // Hero Algerian Budget Summary Card
        item {
            BudgetHeroCard(
                state = state,
                onToggleCurrencyMode = onToggleCurrencyMode
            )
        }

        // Action Buttons Row (Add Expense / Add Income)
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = { onOpenAddTransaction(false) },
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp)
                        .testTag("add_expense_btn"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ExpenseColor
                    ),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowDownward,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "تسجيل مصروف",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }

                Button(
                    onClick = { onOpenAddTransaction(true) },
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp)
                        .testTag("add_income_btn"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = IncomeColor
                    ),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowUpward,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "إضافة مدخول",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }
        }

        // Category Filter Chips & Search Bar
        item {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "المعاملات الأخيرة (${state.filteredTransactions.size})",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(
                        onClick = { isSearchExpanded = !isSearchExpanded },
                        modifier = Modifier.testTag("toggle_search_btn")
                    ) {
                        Icon(
                            imageVector = if (isSearchExpanded) Icons.Default.Clear else Icons.Default.Search,
                            contentDescription = "بحث"
                        )
                    }
                }

                AnimatedVisibility(visible = isSearchExpanded) {
                    OutlinedTextField(
                        value = state.searchQuery,
                        onValueChange = onSearchQueryChange,
                        placeholder = { Text("ابحث في المصاريف والملاحظات...") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .testTag("search_field"),
                        singleLine = true,
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.Search, contentDescription = null)
                        },
                        trailingIcon = {
                            if (state.searchQuery.isNotEmpty()) {
                                IconButton(onClick = { onSearchQueryChange("") }) {
                                    Icon(imageVector = Icons.Default.Clear, contentDescription = "مسح")
                                }
                            }
                        }
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Filter Chips Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = state.selectedCategoryFilter == null,
                        onClick = { onSelectCategoryFilter(null) },
                        label = { Text("الكل") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = AlgerianGreen,
                            selectedLabelColor = Color.White
                        )
                    )
                    BudgetCategory.entries.forEach { category ->
                        val isSelected = state.selectedCategoryFilter == category.id
                        FilterChip(
                            selected = isSelected,
                            onClick = {
                                onSelectCategoryFilter(if (isSelected) null else category.id)
                            },
                            label = { Text(category.titleArabic, fontSize = 12.sp) },
                            leadingIcon = {
                                Icon(
                                    imageVector = category.icon,
                                    contentDescription = null,
                                    tint = if (isSelected) Color.White else category.color,
                                    modifier = Modifier.size(16.dp)
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = category.color,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }
            }
        }

        // Transactions List or Empty State
        if (state.filteredTransactions.isEmpty()) {
            item {
                EmptyTransactionsCard()
            }
        } else {
            items(
                items = state.filteredTransactions,
                key = { it.id }
            ) { transaction ->
                TransactionItemCard(
                    transaction = transaction,
                    currencyMode = state.currencyDisplayMode,
                    onDelete = { onDeleteTransaction(transaction) }
                )
            }
        }
    }
}

@Composable
fun BudgetHeroCard(
    state: BudgetUiState,
    onToggleCurrencyMode: () -> Unit
) {
    val isPositiveBalance = state.balanceDzd >= 0
    val budgetStatusColor = when {
        state.spentPercentage < 0.6f -> IncomeColor
        state.spentPercentage < 0.85f -> AlgerianGold
        else -> ExpenseColor
    }
    val budgetStatusText = when {
        state.totalIncomeDzd == 0.0 -> "لم يتم تسجيل أي مدخول بعد"
        state.spentPercentage < 0.6f -> "ميزانية ممتازة ومتحكم فيها 👏"
        state.spentPercentage < 0.85f -> "انتبه: اقتراب استنزاف الراتب ⚠️"
        state.spentPercentage <= 1.0f -> "حرج: الراتب قارب على الانتهاء! 🚨"
        else -> "عجز مالي: المصاريف فاقت المداخيل! ⚠️"
    }

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("budget_hero_card"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            // Status Tag
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "المتبقي من الراتب (الباقي)",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = budgetStatusColor.copy(alpha = 0.15f)
                ) {
                    Text(
                        text = budgetStatusText,
                        color = budgetStatusColor,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Big Remaining Amount
            Text(
                text = AlgerianCurrencyUtil.formatDzd(state.balanceDzd),
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold,
                color = if (isPositiveBalance) AlgerianGreen else ExpenseColor
            )

            // Spoken Dialect centimes banner
            Text(
                text = "أي ما يعادل: ${AlgerianCurrencyUtil.toAlgerianSpokenCentimes(state.balanceDzd)}",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.9f)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Spent percentage progress bar
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "نسبة الصرف من المداخيل",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "${(state.spentPercentage * 100).toInt()}%",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = budgetStatusColor
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                LinearProgressIndicator(
                    progress = { state.spentPercentage },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = budgetStatusColor,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                    strokeCap = StrokeCap.Round
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Income and Expense Dual Pill
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                // Incomes
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.ArrowUpward,
                            contentDescription = null,
                            tint = IncomeColor,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "المداخيل",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = AlgerianCurrencyUtil.formatDzd(state.totalIncomeDzd),
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = IncomeColor
                    )
                }

                Box(
                    modifier = Modifier
                        .width(1.dp)
                        .height(36.dp)
                        .background(MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
                )

                // Expenses
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.ArrowDownward,
                            contentDescription = null,
                            tint = ExpenseColor,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "المصاريف",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = AlgerianCurrencyUtil.formatDzd(state.totalExpensesDzd),
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = ExpenseColor
                    )
                }
            }
        }
    }
}

@Composable
fun TransactionItemCard(
    transaction: TransactionEntity,
    currencyMode: CurrencyDisplayMode,
    onDelete: () -> Unit
) {
    val category = BudgetCategory.fromId(transaction.categoryId)
    val dateFormat = remember { SimpleDateFormat("dd MMM yyyy", Locale("ar")) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("tx_item_${transaction.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Icon & Details
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Category Icon
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(category.color.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = category.icon,
                        contentDescription = category.titleArabic,
                        tint = category.color,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = transaction.title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "${category.titleArabic} • ${dateFormat.format(Date(transaction.dateMillis))}",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    if (transaction.notes.isNotBlank()) {
                        Text(
                            text = transaction.notes,
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.outline,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }

            // Amount & Delete Button
            Row(verticalAlignment = Alignment.CenterVertically) {
                CurrencyDisplay(
                    amountDzd = transaction.amountDzd,
                    currencyMode = currencyMode,
                    color = if (transaction.isIncome) IncomeColor else ExpenseColor,
                    fontSize = 15.sp,
                    showPrefixSign = true,
                    isIncome = transaction.isIncome
                )

                IconButton(
                    onClick = onDelete,
                    modifier = Modifier
                        .size(36.dp)
                        .testTag("delete_tx_${transaction.id}")
                ) {
                    Icon(
                        imageVector = Icons.Default.DeleteOutline,
                        contentDescription = "حذف المعاملة",
                        tint = MaterialTheme.colorScheme.outline.copy(alpha = 0.7f),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun EmptyTransactionsCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.ShoppingBag,
                contentDescription = null,
                tint = AlgerianGreen.copy(alpha = 0.6f),
                modifier = Modifier.size(54.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "لا توجد معاملات مسجلة في هذا الشهر",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "اضغط على 'تسجيل مصروف' أو 'إضافة مدخول' لبدء تسيير ميزانيتك بسهولة!",
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}

fun formatMonthArabic(monthKey: String): String {
    return try {
        val parser = SimpleDateFormat("yyyy-MM", Locale.US)
        val date = parser.parse(monthKey) ?: Date()
        val formatter = SimpleDateFormat("MMMM yyyy", Locale("ar"))
        formatter.format(date)
    } catch (e: Exception) {
        monthKey
    }
}
