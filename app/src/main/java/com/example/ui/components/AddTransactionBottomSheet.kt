package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
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
import com.example.data.model.BudgetCategory
import com.example.data.util.AlgerianCurrencyUtil
import com.example.ui.theme.AlgerianGreen
import com.example.ui.theme.ExpenseColor
import com.example.ui.theme.IncomeColor

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun AddTransactionBottomSheet(
    sheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
    initialIsIncome: Boolean = false,
    onDismiss: () -> Unit,
    onAddTransaction: (title: String, amountDzd: Double, categoryId: String, isIncome: Boolean, notes: String) -> Unit
) {
    var isIncome by remember { mutableStateOf(initialIsIncome) }
    var title by remember { mutableStateOf("") }
    var amountText by remember { mutableStateOf("") }
    var selectedCategory by remember {
        mutableStateOf(if (initialIsIncome) BudgetCategory.SALARY else BudgetCategory.MARKET)
    }
    var notes by remember { mutableStateOf("") }
    var isAmountError by remember { mutableStateOf(false) }
    var isTitleError by remember { mutableStateOf(false) }

    val amountDouble = amountText.toDoubleOrNull() ?: 0.0

    // Quick suggestions for Algerian common expenses/incomes
    val quickSuggestions = if (isIncome) {
        listOf("راتب الشهرية (CCP)", "منحة أو بريم", "عمل إضافي حر", "استرجاع دين")
    } else {
        listOf(
            "قفة الخضر والفواكه",
            "فاتورة سونلغاز",
            "اشتراك إنترنت أيدوم",
            "بنزين للسيارة",
            "كراء الشقة",
            "حليب وخبز وسوبيرات",
            "طبيب وأدوية",
            "قهوة مع الصحاب"
        )
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (isIncome) "إضافة مدخول / راتب" else "تسجيل مصروف جديد",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.testTag("close_add_transaction_btn")
                ) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "إلغاء")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Income / Expense Toggle
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                // Expense Button
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (!isIncome) ExpenseColor else Color.Transparent)
                        .clickable {
                            isIncome = false
                            if (selectedCategory == BudgetCategory.SALARY) {
                                selectedCategory = BudgetCategory.MARKET
                            }
                        }
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "مصروف (-)",
                        fontWeight = FontWeight.Bold,
                        color = if (!isIncome) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // Income Button
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isIncome) IncomeColor else Color.Transparent)
                        .clickable {
                            isIncome = true
                            selectedCategory = BudgetCategory.SALARY
                        }
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "مدخول / شهرية (+)",
                        fontWeight = FontWeight.Bold,
                        color = if (isIncome) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Amount Input Field
            OutlinedTextField(
                value = amountText,
                onValueChange = { input ->
                    if (input.all { it.isDigit() || it == '.' }) {
                        amountText = input
                        isAmountError = false
                    }
                },
                label = { Text("المبلغ بالدينار الجزائري (دج)") },
                placeholder = { Text("مثلاً: 5000") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                singleLine = true,
                isError = isAmountError,
                supportingText = {
                    if (isAmountError) {
                        Text("الرجاء إدخال مبلغ صحيح أكبر من 0")
                    } else if (amountDouble > 0) {
                        Text(
                            text = "= ${AlgerianCurrencyUtil.toAlgerianSpokenCentimes(amountDouble)}",
                            color = AlgerianGreen,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("amount_input")
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Title Input Field
            OutlinedTextField(
                value = title,
                onValueChange = {
                    title = it
                    isTitleError = false
                },
                label = { Text("وصف المعاملة أو الاسم") },
                placeholder = { Text("مثال: قفة الخضر، سونلغاز، بنزين...") },
                singleLine = true,
                isError = isTitleError,
                supportingText = if (isTitleError) {
                    { Text("الرجاء كتابة اسم أو وصف للمعاملة") }
                } else null,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("title_input")
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Quick suggestion chips
            Text(
                text = "اقتراحات شائعة:",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(4.dp))
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                quickSuggestions.forEach { suggestion ->
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .clickable {
                                title = suggestion
                                isTitleError = false
                                // Auto set appropriate category
                                when {
                                    suggestion.contains("سونلغاز") || suggestion.contains("إنترنت") -> selectedCategory = BudgetCategory.BILLS
                                    suggestion.contains("قفة") || suggestion.contains("خضر") || suggestion.contains("حليب") -> selectedCategory = BudgetCategory.MARKET
                                    suggestion.contains("بنزين") -> selectedCategory = BudgetCategory.TRANSPORT
                                    suggestion.contains("كراء") -> selectedCategory = BudgetCategory.HOUSING
                                    suggestion.contains("طبيب") || suggestion.contains("أدوية") -> selectedCategory = BudgetCategory.HEALTH
                                    suggestion.contains("قهوة") -> selectedCategory = BudgetCategory.CAFE
                                    suggestion.contains("راتب") || suggestion.contains("شهرية") -> selectedCategory = BudgetCategory.SALARY
                                }
                            }
                    ) {
                        Text(
                            text = suggestion,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Category Selection
            Text(
                text = "الفئة (التصنيف):",
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))

            val availableCategories = if (isIncome) {
                listOf(BudgetCategory.SALARY, BudgetCategory.OTHER)
            } else {
                BudgetCategory.expenseCategories()
            }

            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                availableCategories.forEach { category ->
                    val isSelected = selectedCategory == category
                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedCategory = category },
                        label = { Text(category.titleArabic, fontSize = 12.sp) },
                        leadingIcon = {
                            Icon(
                                imageVector = category.icon,
                                contentDescription = category.titleArabic,
                                tint = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else category.color,
                                modifier = Modifier.size(18.dp)
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Notes Input
            OutlinedTextField(
                value = notes,
                onValueChange = { notes = it },
                label = { Text("ملاحظة إضافية (اختياري)") },
                placeholder = { Text("مثلاً: تم الشراء من مارشي بلكور") },
                maxLines = 2,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Submit Button
            Button(
                onClick = {
                    val amount = amountText.toDoubleOrNull()
                    if (amount == null || amount <= 0) {
                        isAmountError = true
                        return@Button
                    }
                    if (title.isBlank()) {
                        isTitleError = true
                        return@Button
                    }

                    onAddTransaction(
                        title,
                        amount,
                        selectedCategory.id,
                        isIncome,
                        notes
                    )
                    onDismiss()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("confirm_add_transaction_btn"),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isIncome) IncomeColor else AlgerianGreen
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(imageVector = Icons.Default.Check, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (isIncome) "حفظ المدخول" else "تسجيل المصروف",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
