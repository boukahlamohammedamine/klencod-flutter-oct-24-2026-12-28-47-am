package com.example.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.util.AlgerianCurrencyUtil
import com.example.ui.theme.AlgerianGreen

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AddSavingsGoalDialog(
    onDismiss: () -> Unit,
    onConfirm: (title: String, targetAmountDzd: Double, initialSavedDzd: Double, note: String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var targetAmountText by remember { mutableStateOf("") }
    var initialAmountText by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }
    var isTitleError by remember { mutableStateOf(false) }
    var isAmountError by remember { mutableStateOf(false) }

    val popularGoals = listOf(
        "كبش العيد الكبير",
        "صندوق الطوارئ العائلي",
        "شراء سيارة",
        "عمرة إلى البقاع المقدسة",
        "عطلة الصيف",
        "تأثيث وتجهيز السكن",
        "عرس / زواج"
    )

    val targetDouble = targetAmountText.toDoubleOrNull() ?: 0.0

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "إنشاء هدف توفير جديد في الحصالة",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = title,
                    onValueChange = {
                        title = it
                        isTitleError = false
                    },
                    label = { Text("اسم الهدف") },
                    placeholder = { Text("مثلاً: كبش العيد، شراء سيارة...") },
                    singleLine = true,
                    isError = isTitleError,
                    supportingText = if (isTitleError) {
                        { Text("يرجى كتابة اسم الهدف") }
                    } else null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("goal_title_input")
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "أهداف جزائرية شائعة:",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    popularGoals.forEach { goalName ->
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .clickable {
                                    title = goalName
                                    isTitleError = false
                                    // Default typical amounts in Algeria
                                    when (goalName) {
                                        "كبش العيد الكبير" -> targetAmountText = "80000"
                                        "صندوق الطوارئ العائلي" -> targetAmountText = "100000"
                                        "عمرة إلى البقاع المقدسة" -> targetAmountText = "250000"
                                        "عطلة الصيف" -> targetAmountText = "60000"
                                    }
                                }
                        ) {
                            Text(
                                text = goalName,
                                fontSize = 11.sp,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = targetAmountText,
                    onValueChange = { input ->
                        if (input.all { it.isDigit() || it == '.' }) {
                            targetAmountText = input
                            isAmountError = false
                        }
                    },
                    label = { Text("المبلغ المستهدف (دج)") },
                    placeholder = { Text("مثلاً: 80000") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true,
                    isError = isAmountError,
                    supportingText = {
                        if (isAmountError) {
                            Text("أدخل مبلغاً مستهدفاً صحيحاً")
                        } else if (targetDouble > 0) {
                            Text(
                                text = "= ${AlgerianCurrencyUtil.toAlgerianSpokenCentimes(targetDouble)}",
                                color = AlgerianGreen,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("goal_target_input")
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = initialAmountText,
                    onValueChange = { input ->
                        if (input.all { it.isDigit() || it == '.' }) {
                            initialAmountText = input
                        }
                    },
                    label = { Text("المبلغ المتوفر حالياً (اختياري - دج)") },
                    placeholder = { Text("مثلاً: 10000") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = note,
                    onValueChange = { note = it },
                    label = { Text("ملاحظة (اختياري)") },
                    maxLines = 2,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val target = targetAmountText.toDoubleOrNull()
                    if (title.isBlank()) {
                        isTitleError = true
                        return@Button
                    }
                    if (target == null || target <= 0) {
                        isAmountError = true
                        return@Button
                    }
                    val initial = initialAmountText.toDoubleOrNull() ?: 0.0
                    onConfirm(title, target, initial, note)
                    onDismiss()
                },
                colors = ButtonDefaults.buttonColors(containerColor = AlgerianGreen),
                modifier = Modifier.testTag("save_goal_btn")
            ) {
                Text("حفظ الهدف")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("إلغاء")
            }
        }
    )
}
