package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.util.AlgerianCurrencyUtil
import com.example.ui.components.CurrencyDisplay
import com.example.ui.theme.AlgerianGold
import com.example.ui.theme.AlgerianGreen
import com.example.ui.viewmodel.BudgetUiState
import com.example.ui.viewmodel.CategorySummary

@Composable
fun StatisticsScreen(
    state: BudgetUiState,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("statistics_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Screen Title
        item {
            Column {
                Text(
                    text = "تحليل وتوزيع مصاريف الشهر",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "تتبع أين تذهب أموالك لمعرفة أكثر الفئات استهلاكاً للراتب",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Total Expenses Summary Card
        item {
            ElevatedCard(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "مجموع مصاريف شهر ${formatMonthArabic(state.currentMonthKey)}",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = AlgerianCurrencyUtil.formatDzd(state.totalExpensesDzd),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "= ${AlgerianCurrencyUtil.toAlgerianSpokenCentimes(state.totalExpensesDzd)}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = AlgerianGreen
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.PieChart,
                            contentDescription = null,
                            tint = AlgerianGreen,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
            }
        }

        // Category breakdown list
        if (state.categorySummaries.isEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "لا توجد مصاريف لتحليلها في هذا الشهر",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    }
                }
            }
        } else {
            item {
                Text(
                    text = "ترتيب الفئات حسب الاستهلاك:",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            items(state.categorySummaries, key = { it.category.id }) { summary ->
                CategoryBreakdownCard(
                    summary = summary,
                    currencyMode = state.currencyDisplayMode
                )
            }
        }

        // Algerian Financial Wisdom & Tips Card
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.45f)
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Lightbulb,
                            contentDescription = null,
                            tint = AlgerianGold,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "نصائح ذكية لتسيير الميزانية في الجزائر:",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "• قفة المارشي: الشراء الأسبوعي من الأسواق الشعبية أو سوق الخضر بالجملة يوفر ما يصل إلى 30% مقارنة بالشراء اليومي.",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        lineHeight = 18.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "• فواتير سونلغاز: تجنب ترك سخان الماء أو المدافيء في درجات قصوى لتقليص الشطر الثالث والرابع عالي التكلفة.",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        lineHeight = 18.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "• الحصالة أولاً: احرص دائماً على عزل مبلغ الادخار في بداية نزول الشهرية وليس في نهايتها.",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        lineHeight = 18.sp
                    )
                }
            }
        }
    }
}

@Composable
fun CategoryBreakdownCard(
    summary: CategorySummary,
    currencyMode: com.example.ui.viewmodel.CurrencyDisplayMode
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(summary.category.color.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = summary.category.icon,
                            contentDescription = null,
                            tint = summary.category.color,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = summary.category.titleArabic,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        Text(
                            text = "${summary.transactionCount} عمليات شراء",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Column(horizontalAlignment = Alignment.End) {
                    CurrencyDisplay(
                        amountDzd = summary.totalDzd,
                        currencyMode = currencyMode,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${(summary.percentageOfTotalExpenses * 100).toInt()}% من المصاريف",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = summary.category.color
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            LinearProgressIndicator(
                progress = { summary.percentageOfTotalExpenses },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = summary.category.color,
                trackColor = MaterialTheme.colorScheme.surfaceVariant,
                strokeCap = StrokeCap.Round
            )
        }
    }
}
