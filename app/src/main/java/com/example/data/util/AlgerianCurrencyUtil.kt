package com.example.data.util

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

object AlgerianCurrencyUtil {

    private val numberFormat = DecimalFormat("#,##0", DecimalFormatSymbols(Locale.US).apply {
        groupingSeparator = ','
    })

    /**
     * Formats an amount in Algerian Dinars (e.g. "55,000 دج")
     */
    fun formatDzd(amount: Double): String {
        return "${numberFormat.format(amount)} دج"
    }

    /**
     * Converts Algerian Dinars to Centimes (1 DZD = 100 Centimes)
     */
    fun dzdToCentimes(amountDzd: Double): Long {
        return (amountDzd * 100).toLong()
    }

    /**
     * Converts Centimes to Algerian Dinars
     */
    fun centimesToDzd(centimes: Long): Double {
        return centimes / 100.0
    }

    /**
     * Formats an amount in Centimes (e.g. "5 ملايين و 500 ألف سنتيم")
     */
    fun formatCentimesShort(amountDzd: Double): String {
        val centimes = dzdToCentimes(amountDzd)
        return when {
            centimes >= 1_000_000 -> {
                val millions = centimes / 1_000_000
                val remainderThousands = (centimes % 1_000_000) / 1_000
                if (remainderThousands > 0) {
                    "$millions ملايين و $remainderThousands ألف سنتيم"
                } else {
                    "$millions ملايين سنتيم"
                }
            }
            centimes >= 1000 -> {
                val thousands = centimes / 1000
                "$thousands ألف سنتيم"
            }
            else -> "$centimes سنتيم"
        }
    }

    /**
     * Friendly Algerian spoken translation (بالعامية الجزائرية)
     * e.g. "خمسة ملايين وخمسمية ألف سنتيم (55 ألف دينار)"
     */
    fun toAlgerianSpokenCentimes(amountDzd: Double): String {
        val centimes = dzdToCentimes(amountDzd)
        if (centimes <= 0) return "0 سنتيم"

        val millions = centimes / 1_000_000
        val remainingCentimes = centimes % 1_000_000
        val thousands = remainingCentimes / 1_000

        val millionPart = when (millions) {
            0L -> ""
            1L -> "مليون سنتيم"
            2L -> "زوج ملايين سنتيم"
            in 3..10 -> "$millions ملايين سنتيم"
            else -> "$millions مليون سنتيم"
        }

        val thousandPart = when (thousands) {
            0L -> ""
            1L -> "ألف سنتيم (مية دورو)"
            2L -> "ألفين سنتيم (ميتين دورو)"
            100L -> "مية ألف سنتيم (100 الف)"
            200L -> "ميتين ألف سنتيم (200 الف)"
            in 3..10 -> "$thousands آلاف سنتيم"
            else -> "$thousands ألف سنتيم"
        }

        return when {
            millions > 0 && thousands > 0 -> {
                val mStr = when (millions) {
                    1L -> "مليون"
                    2L -> "زوج ملايين"
                    in 3..10 -> "$millions ملايين"
                    else -> "$millions مليون"
                }
                val tStr = when (thousands) {
                    100L -> "مية ألف"
                    200L -> "ميتين ألف"
                    500L -> "خمسمية ألف"
                    else -> "$thousands ألف"
                }
                "$mStr و $tStr سنتيم"
            }
            millions > 0 -> millionPart
            thousands > 0 -> thousandPart
            else -> "$centimes سنتيم"
        }
    }

    /**
     * Splits salary according to the 50/30/20 budget rule for Algerian salaries:
     * 50% Necessities (الضروريات: قفة، فواتير، كراء، نقل)
     * 30% Lifestyle & Discretionary (الرغبات: قهوة، مطاعم، ترفيه، ملابس)
     * 20% Savings (الادخار والحصالة: طوارئ، مشاريع، أعياد)
     */
    data class BudgetSplit(
        val totalDzd: Double,
        val necessitiesDzd: Double,
        val lifestyleDzd: Double,
        val savingsDzd: Double
    )

    fun calculate503020Split(salaryDzd: Double): BudgetSplit {
        val necessities = salaryDzd * 0.50
        val lifestyle = salaryDzd * 0.30
        val savings = salaryDzd * 0.20
        return BudgetSplit(
            totalDzd = salaryDzd,
            necessitiesDzd = necessities,
            lifestyleDzd = lifestyle,
            savingsDzd = savings
        )
    }
}
