package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CurrencyExchange
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.util.AlgerianCurrencyUtil
import com.example.ui.theme.AlgerianGold
import com.example.ui.theme.AlgerianGreen
import com.example.ui.theme.ExpenseColor
import com.example.ui.theme.IncomeColor
import com.example.ui.theme.SavingsColor

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ConverterGuideScreen(
    onResetData: () -> Unit,
    modifier: Modifier = Modifier
) {
    var dzdInputText by remember { mutableStateOf("50000") }
    var salaryPlannerInput by remember { mutableStateOf("55000") }
    var showResetConfirmDialog by remember { mutableStateOf(false) }

    val dzdAmount = dzdInputText.toDoubleOrNull() ?: 0.0
    val salaryAmount = salaryPlannerInput.toDoubleOrNull() ?: 0.0
    val split = AlgerianCurrencyUtil.calculate503020Split(salaryAmount)

    val quickDzdAmounts = listOf("1000", "2000", "5000", "10000", "25000", "50000", "100000")
    val quickSalaries = listOf("35000", "45000", "55000", "70000", "90000", "120000")

    if (showResetConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showResetConfirmDialog = false },
            title = { Text("إعادة ضبط البيانات التجريبية") },
            text = { Text("هل تريد استعادة البيانات الجزائرية النموذجية الأولية؟ سيتم تحديث السجلات إلى شهرية نموذجية.") },
            confirmButton = {
                Button(
                    onClick = {
                        onResetData()
                        showResetConfirmDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = AlgerianGreen)
                ) {
                    Text("نعم، إعادة الضبط")
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetConfirmDialog = false }) {
                    Text("إلغاء")
                }
            }
        )
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("converter_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        // Section 1: Algerian Dinar <-> Centimes Interactive Converter
        item {
            ElevatedCard(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(CircleShape)
                                .background(AlgerianGreen.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.CurrencyExchange,
                                contentDescription = null,
                                tint = AlgerianGreen,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "محول الدينار ⇋ السنتيم الفوري",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            Text(
                                text = "ترجمة المبالغ إلى العامية الجزائرية بالمليون والمية ألف",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = dzdInputText,
                        onValueChange = { input ->
                            if (input.all { it.isDigit() || it == '.' }) {
                                dzdInputText = input
                            }
                        },
                        label = { Text("المبلغ بالدينار الجزائري (دج)") },
                        placeholder = { Text("مثلاً: 25000") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("converter_dzd_input")
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Shortcut amount chips
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        quickDzdAmounts.forEach { amountStr ->
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .clickable { dzdInputText = amountStr }
                            ) {
                                Text(
                                    text = "${AlgerianCurrencyUtil.formatDzd(amountStr.toDouble())}",
                                    fontSize = 11.sp,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Result Display Box
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp)
                        ) {
                            Text(
                                text = "بالعامية الجزائرية (النطق):",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = AlgerianCurrencyUtil.toAlgerianSpokenCentimes(dzdAmount),
                                fontSize = 18.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = AlgerianGreen
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "بالأرقام: ${AlgerianCurrencyUtil.dzdToCentimes(dzdAmount)} سنتيم (${AlgerianCurrencyUtil.formatDzd(dzdAmount)})",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                    }
                }
            }
        }

        // Section 2: Algerian 50/30/20 Monthly Salary Planner
        item {
            ElevatedCard(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(CircleShape)
                                .background(AlgerianGold.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Calculate,
                                contentDescription = null,
                                tint = AlgerianGold,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "مخطط تقسيم الشهرية (قاعدة 50/30/20)",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            Text(
                                text = "التوزيع المثالي لراتبك الشهري لتفادي الديون والتوفير",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = salaryPlannerInput,
                        onValueChange = { input ->
                            if (input.all { it.isDigit() || it == '.' }) {
                                salaryPlannerInput = input
                            }
                        },
                        label = { Text("قيمة الراتب الشهري (دج)") },
                        placeholder = { Text("مثلاً: 55000") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("salary_planner_input")
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        quickSalaries.forEach { salStr ->
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .clickable { salaryPlannerInput = salStr }
                            ) {
                                Text(
                                    text = "${AlgerianCurrencyUtil.formatDzd(salStr.toDouble())}",
                                    fontSize = 11.sp,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // 50% Necessities Card
                    SplitRuleItemCard(
                        percentageText = "50%",
                        title = "الضروريات والأساسيات",
                        subtitle = "قفة المارشي، كراء، سونلغاز، ماء، أدوية ونقل",
                        amountDzd = split.necessitiesDzd,
                        color = AlgerianGreen
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // 30% Lifestyle Card
                    SplitRuleItemCard(
                        percentageText = "30%",
                        title = "الرغبات والمصاريف الشخصية",
                        subtitle = "قهوة، مطاعم، ملابس، ترفيه وخروج",
                        amountDzd = split.lifestyleDzd,
                        color = AlgerianGold
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // 20% Savings Card
                    SplitRuleItemCard(
                        percentageText = "20%",
                        title = "الحصالة والادخار",
                        subtitle = "طوارئ، كبش العيد، أهداف مستقبلية واستثمار",
                        amountDzd = split.savingsDzd,
                        color = SavingsColor
                    )
                }
            }
        }

        // Section 3: Reset to sample data
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "استعادة النموذج التجريبي",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        Text(
                            text = "إعادة ضبط المعاملات وأهداف الحصالة الجزائرية الافتراضية",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    OutlinedButton(
                        onClick = { showResetConfirmDialog = true },
                        modifier = Modifier.testTag("reset_data_btn")
                    ) {
                        Icon(imageVector = Icons.Default.RestartAlt, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "إعادة ضبط", fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun SplitRuleItemCard(
    percentageText: String,
    title: String,
    subtitle: String,
    amountDzd: Double,
    color: Color
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.08f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = color
                ) {
                    Text(
                        text = percentageText,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column {
                    Text(
                        text = title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = subtitle,
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = AlgerianCurrencyUtil.formatDzd(amountDzd),
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = color
                )
                Text(
                    text = AlgerianCurrencyUtil.toAlgerianSpokenCentimes(amountDzd),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
