package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "savings_goals")
data class SavingsGoalEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val targetAmountDzd: Double,
    val savedAmountDzd: Double,
    val note: String = "",
    val iconKey: String = "savings"
) {
    val progress: Float
        get() = if (targetAmountDzd > 0) (savedAmountDzd / targetAmountDzd).toFloat().coerceIn(0f, 1f) else 0f

    val remainingDzd: Double
        get() = (targetAmountDzd - savedAmountDzd).coerceAtLeast(0.0)

    val isCompleted: Boolean
        get() = savedAmountDzd >= targetAmountDzd
}
