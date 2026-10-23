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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.SavingsGoalEntity
import com.example.data.util.AlgerianCurrencyUtil
import com.example.ui.components.AddSavingsGoalDialog
import com.example.ui.components.CurrencyDisplay
import com.example.ui.components.DepositSavingsDialog
import com.example.ui.theme.AlgerianGold
import com.example.ui.theme.AlgerianGreen
import com.example.ui.theme.IncomeColor
import com.example.ui.theme.SavingsColor
import com.example.ui.viewmodel.BudgetUiState

@Composable
fun SavingsScreen(
    state: BudgetUiState,
    onAddGoal: (title: String, targetAmountDzd: Double, initialSavedDzd: Double, note: String) -> Unit,
    onDepositToGoal: (goal: SavingsGoalEntity, amountDzd: Double) -> Unit,
    onDeleteGoal: (SavingsGoalEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    var showAddDialog by remember { mutableStateOf(false) }
    var goalToDeposit by remember { mutableStateOf<SavingsGoalEntity?>(null) }

    val totalSavedAllGoals = state.savingsGoals.sumOf { it.savedAmountDzd }
    val totalTargetAllGoals = state.savingsGoals.sumOf { it.targetAmountDzd }

    if (showAddDialog) {
        AddSavingsGoalDialog(
            onDismiss = { showAddDialog = false },
            onConfirm = { title, target, initial, note ->
                onAddGoal(title, target, initial, note)
            }
        )
    }

    goalToDeposit?.let { goal ->
        DepositSavingsDialog(
            goal = goal,
            onDismiss = { goalToDeposit = null },
            onDeposit = { amount ->
                onDepositToGoal(goal, amount)
            }
        )
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("savings_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Screen Header & Total Savings
        item {
            ElevatedCard(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "حصالة التوفير والأهداف 🪙",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = AlgerianCurrencyUtil.formatDzd(totalSavedAllGoals),
                                fontSize = 28.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = AlgerianGreen
                            )
                            Text(
                                text = "= ${AlgerianCurrencyUtil.toAlgerianSpokenCentimes(totalSavedAllGoals)}",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = AlgerianGold
                            )
                        }

                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.secondaryContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Savings,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSecondaryContainer,
                                modifier = Modifier.size(30.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "إجمالي الأهداف المستهدفة: ${AlgerianCurrencyUtil.formatDzd(totalTargetAllGoals)}",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // Action Button: Add New Goal
        item {
            Button(
                onClick = { showAddDialog = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("create_goal_btn"),
                colors = ButtonDefaults.buttonColors(containerColor = AlgerianGreen),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "إنشاء هدف توفير جديد في الحصالة",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
        }

        // Goals List Header
        item {
            Text(
                text = "أهدافك الحالية (${state.savingsGoals.size})",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }

        // Goals items
        if (state.savingsGoals.isEmpty()) {
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
                            text = "لا توجد أهداف توفير حالياً",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "ابدأ بوضع هدف مثل كبش العيد أو صندوق الطوارئ لتبدأ في الادخار!",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        } else {
            items(state.savingsGoals, key = { it.id }) { goal ->
                SavingsGoalItemCard(
                    goal = goal,
                    currencyMode = state.currencyDisplayMode,
                    onDeposit = { goalToDeposit = goal },
                    onDelete = { onDeleteGoal(goal) }
                )
            }
        }
    }
}

@Composable
fun SavingsGoalItemCard(
    goal: SavingsGoalEntity,
    currencyMode: com.example.ui.viewmodel.CurrencyDisplayMode,
    onDeposit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("goal_card_${goal.id}")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header Row: Title, Complete Badge, Delete
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(if (goal.isCompleted) IncomeColor.copy(alpha = 0.15f) else SavingsColor.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (goal.isCompleted) Icons.Default.CheckCircle else Icons.Default.Savings,
                            contentDescription = null,
                            tint = if (goal.isCompleted) IncomeColor else SavingsColor,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = goal.title,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        if (goal.note.isNotBlank()) {
                            Text(
                                text = goal.note,
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (goal.isCompleted) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = IncomeColor.copy(alpha = 0.15f)
                        ) {
                            Text(
                                text = "مكتمل 🎉",
                                color = IncomeColor,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                    IconButton(
                        onClick = onDelete,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.DeleteOutline,
                            contentDescription = "حذف الهدف",
                            tint = MaterialTheme.colorScheme.outline.copy(alpha = 0.6f),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Progress Bar & Percentage
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "تم تجميع ${AlgerianCurrencyUtil.formatDzd(goal.savedAmountDzd)} من أصل ${AlgerianCurrencyUtil.formatDzd(goal.targetAmountDzd)}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "${(goal.progress * 100).toInt()}%",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (goal.isCompleted) IncomeColor else AlgerianGreen
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            LinearProgressIndicator(
                progress = { goal.progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = if (goal.isCompleted) IncomeColor else AlgerianGreen,
                trackColor = MaterialTheme.colorScheme.surfaceVariant,
                strokeCap = StrokeCap.Round
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Remaining amount
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (!goal.isCompleted) {
                    Text(
                        text = "باقي: ${AlgerianCurrencyUtil.formatDzd(goal.remainingDzd)} (${AlgerianCurrencyUtil.toAlgerianSpokenCentimes(goal.remainingDzd)})",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                } else {
                    Text(
                        text = "أحسنت! تم الوصول للمبلغ المطلوب بالكامل",
                        fontSize = 11.sp,
                        color = IncomeColor,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                OutlinedButton(
                    onClick = onDeposit,
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                    modifier = Modifier.testTag("deposit_btn_${goal.id}")
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "إيداع دراهم", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
