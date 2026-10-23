package com.example.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.TransactionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {
    @Query("SELECT * FROM transactions WHERE monthKey = :monthKey ORDER BY dateMillis DESC, id DESC")
    fun getTransactionsByMonth(monthKey: String): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions ORDER BY dateMillis DESC")
    fun getAllTransactions(): Flow<List<TransactionEntity>>

    @Query("SELECT DISTINCT monthKey FROM transactions ORDER BY monthKey DESC")
    fun getAllMonthKeys(): Flow<List<String>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(transaction: TransactionEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(transactions: List<TransactionEntity>)

    @Update
    suspend fun update(transaction: TransactionEntity)

    @Delete
    suspend fun delete(transaction: TransactionEntity)

    @Query("DELETE FROM transactions WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("SELECT COUNT(*) FROM transactions")
    suspend fun getCount(): Int

    @Query("DELETE FROM transactions")
    suspend fun deleteAll()
}
