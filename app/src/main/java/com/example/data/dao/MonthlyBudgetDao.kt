package com.example.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.model.MonthlyBudgetSetting
import kotlinx.coroutines.flow.Flow

@Dao
interface MonthlyBudgetDao {
    @Query("SELECT * FROM monthly_budget_settings WHERE monthKey = :monthKey LIMIT 1")
    fun getSetting(monthKey: String): Flow<MonthlyBudgetSetting?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(setting: MonthlyBudgetSetting)

    @Query("DELETE FROM monthly_budget_settings")
    suspend fun deleteAll()
}
