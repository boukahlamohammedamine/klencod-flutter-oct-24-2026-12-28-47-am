package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val amountDzd: Double,
    val categoryId: String,
    val isIncome: Boolean,
    val dateMillis: Long,
    val monthKey: String, // e.g. "2026-10"
    val notes: String = ""
)
