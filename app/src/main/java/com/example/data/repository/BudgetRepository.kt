package com.example.data.repository

import com.example.data.dao.MonthlyBudgetDao
import com.example.data.dao.SavingsGoalDao
import com.example.data.dao.TransactionDao
import com.example.data.db.AppDatabase
import com.example.data.model.MonthlyBudgetSetting
import com.example.data.model.SavingsGoalEntity
import com.example.data.model.TransactionEntity
import kotlinx.coroutines.flow.Flow

class BudgetRepository(
    private val transactionDao: TransactionDao,
    private val savingsGoalDao: SavingsGoalDao,
    private val monthlyBudgetDao: MonthlyBudgetDao,
    private val database: AppDatabase
) {
    fun getTransactionsForMonth(monthKey: String): Flow<List<TransactionEntity>> {
        return transactionDao.getTransactionsByMonth(monthKey)
    }

    fun getAllMonthKeys(): Flow<List<String>> {
        return transactionDao.getAllMonthKeys()
    }

    fun getAllSavingsGoals(): Flow<List<SavingsGoalEntity>> {
        return savingsGoalDao.getAllGoals()
    }

    fun getMonthlyBudgetSetting(monthKey: String): Flow<MonthlyBudgetSetting?> {
        return monthlyBudgetDao.getSetting(monthKey)
    }

    suspend fun addTransaction(transaction: TransactionEntity): Long {
        return transactionDao.insert(transaction)
    }

    suspend fun updateTransaction(transaction: TransactionEntity) {
        transactionDao.update(transaction)
    }

    suspend fun deleteTransaction(transaction: TransactionEntity) {
        transactionDao.delete(transaction)
    }

    suspend fun deleteTransactionById(id: Long) {
        transactionDao.deleteById(id)
    }

    suspend fun updateSalary(monthKey: String, salaryDzd: Double, budgetCap: Double = 0.0) {
        monthlyBudgetDao.insertOrUpdate(
            MonthlyBudgetSetting(
                monthKey = monthKey,
                salaryDzd = salaryDzd,
                budgetCapDzd = budgetCap
            )
        )
    }

    suspend fun addSavingsGoal(goal: SavingsGoalEntity): Long {
        return savingsGoalDao.insert(goal)
    }

    suspend fun updateSavingsGoal(goal: SavingsGoalEntity) {
        savingsGoalDao.update(goal)
    }

    suspend fun depositToSavingsGoal(goalId: Long, currentGoal: SavingsGoalEntity, depositAmountDzd: Double) {
        val newSavedAmount = currentGoal.savedAmountDzd + depositAmountDzd
        savingsGoalDao.update(currentGoal.copy(savedAmountDzd = newSavedAmount))
    }

    suspend fun deleteSavingsGoal(goal: SavingsGoalEntity) {
        savingsGoalDao.delete(goal)
    }

    suspend fun deleteSavingsGoalById(id: Long) {
        savingsGoalDao.deleteById(id)
    }

    suspend fun checkAndSeedIfEmpty() {
        if (transactionDao.getCount() == 0) {
            AppDatabase.seedSampleAlgerianData(database)
        }
    }

    suspend fun resetAllData() {
        transactionDao.deleteAll()
        savingsGoalDao.deleteAll()
        monthlyBudgetDao.deleteAll()
        AppDatabase.seedSampleAlgerianData(database)
    }
}
