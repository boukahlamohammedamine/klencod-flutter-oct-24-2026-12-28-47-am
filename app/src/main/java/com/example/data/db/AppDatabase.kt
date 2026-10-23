package com.example.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.dao.MonthlyBudgetDao
import com.example.data.dao.SavingsGoalDao
import com.example.data.dao.TransactionDao
import com.example.data.model.MonthlyBudgetSetting
import com.example.data.model.SavingsGoalEntity
import com.example.data.model.TransactionEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Database(
    entities = [
        TransactionEntity::class,
        SavingsGoalEntity::class,
        MonthlyBudgetSetting::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun transactionDao(): TransactionDao
    abstract fun savingsGoalDao(): SavingsGoalDao
    abstract fun monthlyBudgetDao(): MonthlyBudgetDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "mizaniti_budget_db"
                )
                    .addCallback(DatabaseCallback())
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    CoroutineScope(Dispatchers.IO).launch {
                        seedSampleAlgerianData(database)
                    }
                }
            }
        }

        suspend fun seedSampleAlgerianData(database: AppDatabase) {
            val now = Calendar.getInstance()
            val monthFormat = SimpleDateFormat("yyyy-MM", Locale.US)
            val currentMonth = monthFormat.format(now.time)
            val time = now.timeInMillis

            // Initial Salary setting
            database.monthlyBudgetDao().insertOrUpdate(
                MonthlyBudgetSetting(
                    monthKey = currentMonth,
                    salaryDzd = 55000.0,
                    budgetCapDzd = 50000.0
                )
            )

            // Realistic Algerian transactions
            val initialTransactions = listOf(
                TransactionEntity(
                    title = "راتب الشهر (الشهرية)",
                    amountDzd = 55000.0,
                    categoryId = "salary",
                    isIncome = true,
                    dateMillis = time - (12 * 86400000L),
                    monthKey = currentMonth,
                    notes = "تحويل الراتب الشهري في حساب CCP"
                ),
                TransactionEntity(
                    title = "كراء الشقة",
                    amountDzd = 18000.0,
                    categoryId = "housing",
                    isIncome = false,
                    dateMillis = time - (11 * 86400000L),
                    monthKey = currentMonth,
                    notes = "إيجار شقة F3"
                ),
                TransactionEntity(
                    title = "قفة السوق الأسبوعية",
                    amountDzd = 8500.0,
                    categoryId = "market",
                    isIncome = false,
                    dateMillis = time - (8 * 86400000L),
                    monthKey = currentMonth,
                    notes = "خضر، فواكه، بطاطا، دجاج وسوبيرات"
                ),
                TransactionEntity(
                    title = "فاتورة سونلغاز (الكهرباء والغاز)",
                    amountDzd = 3800.0,
                    categoryId = "bills",
                    isIncome = false,
                    dateMillis = time - (6 * 86400000L),
                    monthKey = currentMonth,
                    notes = "فاتورة الثلاثي الأول Sonelgaz"
                ),
                TransactionEntity(
                    title = "اشتراك إنترنت إيدوم فيبر",
                    amountDzd = 1600.0,
                    categoryId = "bills",
                    isIncome = false,
                    dateMillis = time - (5 * 86400000L),
                    monthKey = currentMonth,
                    notes = "اتصالات الجزائر - اشتراك شهري"
                ),
                TransactionEntity(
                    title = "بنزين نفطال للسيارة",
                    amountDzd = 2500.0,
                    categoryId = "transport",
                    isIncome = false,
                    dateMillis = time - (4 * 86400000L),
                    monthKey = currentMonth,
                    notes = "بنزين بدون رصاص محطة نفطال"
                ),
                TransactionEntity(
                    title = "شراء أدوية من الصيدلية",
                    amountDzd = 1400.0,
                    categoryId = "health",
                    isIncome = false,
                    dateMillis = time - (3 * 86400000L),
                    monthKey = currentMonth,
                    notes = "فيتامينات ومسكنات"
                ),
                TransactionEntity(
                    title = "قهوة ومصروف يومي",
                    amountDzd = 1200.0,
                    categoryId = "cafe",
                    isIncome = false,
                    dateMillis = time - (1 * 86400000L),
                    monthKey = currentMonth,
                    notes = "قهوة مع الصحاب وأكل سريع"
                ),
                TransactionEntity(
                    title = "تحويل إلى الحصالة (ادخار)",
                    amountDzd = 5000.0,
                    categoryId = "savings",
                    isIncome = false,
                    dateMillis = time - (2 * 86400000L),
                    monthKey = currentMonth,
                    notes = "توفير شهري لكبش العيد"
                )
            )
            database.transactionDao().insertAll(initialTransactions)

            // Realistic Algerian Savings Goals (الحصالة)
            val initialGoals = listOf(
                SavingsGoalEntity(
                    title = "كبش العيد الكبير",
                    targetAmountDzd = 80000.0,
                    savedAmountDzd = 35000.0,
                    note = "شراء أضحية العيد من سوق الماشية",
                    iconKey = "eid"
                ),
                SavingsGoalEntity(
                    title = "صندوق الطوارئ العائلي",
                    targetAmountDzd = 100000.0,
                    savedAmountDzd = 45000.0,
                    note = "مبلغ احتياطي للحالات المستعجلة",
                    iconKey = "emergency"
                ),
                SavingsGoalEntity(
                    title = "عطلة الصيف في جيجل",
                    targetAmountDzd = 60000.0,
                    savedAmountDzd = 20000.0,
                    note = "كراء سكن صيفي ومصاريف التخييم",
                    iconKey = "summer"
                )
            )
            database.savingsGoalDao().insertAll(initialGoals)
        }
    }
}
