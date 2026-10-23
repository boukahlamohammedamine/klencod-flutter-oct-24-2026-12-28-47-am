package com.example.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.SavingsGoalEntity
import com.example.data.util.AlgerianCurrencyUtil
import com.example.ui.theme.AlgerianGreen

@Composable
fun DepositSavingsDialog(
    goal: SavingsGoalEntity,
    onDismiss: () -> Unit,
    onDeposit: (amountDzd: Double) -> Unit
) {
    var amountText by remember { mutableStateOf("") }
    var isAmountError by remember { mutableStateOf(false) }

    val amountDouble = amountText.toDoubleOrNull() ?: 0.0

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "وضع دراهم في حصالة: ${goal.title}",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "المبلغ المحفوظ حالياً: ${AlgerianCurrencyUtil.formatDzd(goal.savedAmountDzd)} من أصل ${AlgerianCurrencyUtil.formatDzd(goal.targetAmountDzd)}",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "المتبقي لإكمال الهدف: ${AlgerianCurrencyUtil.formatDzd(goal.remainingDzd)}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = AlgerianGreen
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = amountText,
                    onValueChange = { input ->
                        if (input.all { it.isDigit() || it == '.' }) {
                            amountText = input
                            isAmountError = false
                        }
                    },
                    label = { Text("المبلغ المضاف للحصالة (دج)") },
                    placeholder = { Text("مثلاً: 5000") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true,
                    isError = isAmountError,
                    supportingText = {
                        if (isAmountError) {
                            Text("أدخل مبلغاً صحيحاً أكبر من 0")
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
                        .testTag("deposit_amount_input")
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val amount = amountText.toDoubleOrNull()
                    if (amount == null || amount <= 0) {
                        isAmountError = true
                        return@Button
                    }
                    onDeposit(amount)
                    onDismiss()
                },
                colors = ButtonDefaults.buttonColors(containerColor = AlgerianGreen),
                modifier = Modifier.testTag("confirm_deposit_btn")
            ) {
                Text("إيداع في الحصالة")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("إلغاء")
            }
        }
    )
}
