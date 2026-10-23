package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "monthly_budget_settings")
data class MonthlyBudgetSetting(
    @PrimaryKey
    val monthKey: String, // e.g. "2026-10"
    val salaryDzd: Double = 0.0,
    val budgetCapDzd: Double = 0.0
)
