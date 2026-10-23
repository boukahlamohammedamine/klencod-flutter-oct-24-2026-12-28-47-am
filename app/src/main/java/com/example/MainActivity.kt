package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CurrencyExchange
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.AddTransactionBottomSheet
import com.example.ui.screens.ConverterGuideScreen
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.SavingsScreen
import com.example.ui.screens.StatisticsScreen
import com.example.ui.theme.AlgerianGold
import com.example.ui.theme.AlgerianGreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.BudgetViewModel

enum class NavigationScreen(
    val titleArabic: String,
    val icon: ImageVector
) {
    DASHBOARD("الرئيسية", Icons.Default.AccountBalanceWallet),
    STATISTICS("الإحصائيات", Icons.Default.PieChart),
    SAVINGS("الحصالة", Icons.Default.Savings),
    CONVERTER("المحول", Icons.Default.CurrencyExchange)
}

class MainActivity : ComponentActivity() {

    private val viewModel: BudgetViewModel by viewModels()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MyApplicationTheme {
                // Ensure RTL layout for Arabic natural reading flow
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                    val state by viewModel.uiState.collectAsStateWithLifecycle()
                    var selectedTab by remember { mutableIntStateOf(0) }
                    var showAddTransactionSheet by remember { mutableStateOf(false) }
                    var isIncomeTransaction by remember { mutableStateOf(false) }

                    if (showAddTransactionSheet) {
                        AddTransactionBottomSheet(
                            initialIsIncome = isIncomeTransaction,
                            onDismiss = { showAddTransactionSheet = false },
                            onAddTransaction = { title, amount, categoryId, isIncome, notes ->
                                viewModel.addTransaction(
                                    title = title,
                                    amountDzd = amount,
                                    categoryId = categoryId,
                                    isIncome = isIncome,
                                    notes = notes
                                )
                            }
                        )
                    }

                    Scaffold(
                        modifier = Modifier
                            .fillMaxSize()
                            .testTag("main_scaffold"),
                        topBar = {
                            CenterAlignedTopAppBar(
                                title = {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Box(
                                            modifier = Modifier
                                                .size(32.dp)
                                                .clip(CircleShape)
                                                .background(AlgerianGreen),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = "🇩🇿",
                                                fontSize = 18.sp
                                            )
                                        }
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                            Text(
                                                text = "ميزانيتي",
                                                fontWeight = FontWeight.ExtraBold,
                                                fontSize = 18.sp,
                                                color = MaterialTheme.colorScheme.onSurface
                                            )
                                            Text(
                                                text = "تسيير ميزانية الجزائري",
                                                fontSize = 11.sp,
                                                color = AlgerianGreen,
                                                fontWeight = FontWeight.SemiBold
                                            )
                                        }
                                    }
                                },
                                actions = {
                                    IconButton(
                                        onClick = {
                                            isIncomeTransaction = false
                                            showAddTransactionSheet = true
                                        },
                                        modifier = Modifier.testTag("top_add_btn")
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Add,
                                            contentDescription = "إضافة معاملة",
                                            tint = AlgerianGreen
                                        )
                                    }
                                },
                                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                                    containerColor = MaterialTheme.colorScheme.surface
                                )
                            )
                        },
                        bottomBar = {
                            NavigationBar(
                                containerColor = MaterialTheme.colorScheme.surface,
                                tonalElevation = 4.dp
                            ) {
                                NavigationScreen.entries.forEachIndexed { index, screen ->
                                    val isSelected = selectedTab == index
                                    NavigationBarItem(
                                        selected = isSelected,
                                        onClick = { selectedTab = index },
                                        icon = {
                                            Icon(
                                                imageVector = screen.icon,
                                                contentDescription = screen.titleArabic
                                            )
                                        },
                                        label = {
                                            Text(
                                                text = screen.titleArabic,
                                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                                fontSize = 12.sp
                                            )
                                        },
                                        colors = NavigationBarItemDefaults.colors(
                                            selectedIconColor = AlgerianGreen,
                                            selectedTextColor = AlgerianGreen,
                                            indicatorColor = MaterialTheme.colorScheme.primaryContainer
                                        ),
                                        modifier = Modifier.testTag("nav_${screen.name.lowercase()}")
                                    )
                                }
                            }
                        },
                        floatingActionButton = {
                            if (selectedTab == 0) {
                                FloatingActionButton(
                                    onClick = {
                                        isIncomeTransaction = false
                                        showAddTransactionSheet = true
                                    },
                                    containerColor = AlgerianGreen,
                                    contentColor = Color.White,
                                    modifier = Modifier.testTag("fab_add_tx")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Add,
                                        contentDescription = "إضافة مصروف"
                                    )
                                }
                            }
                        }
                    ) { innerPadding ->
                        AnimatedContent(
                            targetState = selectedTab,
                            label = "tab_transition",
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding)
                        ) { tabIndex ->
                            when (tabIndex) {
                                0 -> DashboardScreen(
                                    state = state,
                                    onSelectMonth = { viewModel.selectMonth(it) },
                                    onToggleCurrencyMode = { viewModel.toggleCurrencyDisplayMode() },
                                    onSelectCategoryFilter = { viewModel.setCategoryFilter(it) },
                                    onSearchQueryChange = { viewModel.setSearchQuery(it) },
                                    onOpenAddTransaction = { isIncome ->
                                        isIncomeTransaction = isIncome
                                        showAddTransactionSheet = true
                                    },
                                    onDeleteTransaction = { tx -> viewModel.deleteTransaction(tx) }
                                )
                                1 -> StatisticsScreen(
                                    state = state
                                )
                                2 -> SavingsScreen(
                                    state = state,
                                    onAddGoal = { title, target, initial, note ->
                                        viewModel.addSavingsGoal(title, target, initial, note)
                                    },
                                    onDepositToGoal = { goal, amount ->
                                        viewModel.depositToSavingsGoal(goal, amount)
                                    },
                                    onDeleteGoal = { goal ->
                                        viewModel.deleteSavingsGoal(goal)
                                    }
                                )
                                3 -> ConverterGuideScreen(
                                    onResetData = { viewModel.resetAllData() }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
