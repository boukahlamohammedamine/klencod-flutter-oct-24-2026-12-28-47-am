package com.example.data.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.FamilyRestroom
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalPharmacy
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

enum class BudgetCategory(
    val id: String,
    val titleArabic: String,
    val subtitleArabic: String,
    val color: Color,
    val icon: ImageVector,
    val isExpenseOnly: Boolean = true
) {
    MARKET(
        id = "market",
        titleArabic = "قفة السوق والمشتريات",
        subtitleArabic = "خضر، فواكه، لحوم، سوبيرات ومارشي",
        color = Color(0xFF43A047),
        icon = Icons.Default.ShoppingCart
    ),
    BILLS(
        id = "bills",
        titleArabic = "الفواتير (سونلغاز / نت / ماء)",
        subtitleArabic = "Sonelgaz، الجزائرية للمياه، إيدوم فيبر",
        color = Color(0xFFFFB300),
        icon = Icons.Default.Bolt
    ),
    HOUSING(
        id = "housing",
        titleArabic = "الكراء ومصاريف السكن",
        subtitleArabic = "إيجار البيت، لوازم المنزل والصيانة",
        color = Color(0xFF5E35B1),
        icon = Icons.Default.Home
    ),
    TRANSPORT(
        id = "transport",
        titleArabic = "النقل والوقود",
        subtitleArabic = "بنزين نفطال، طاكسي، ترامواي وصيانة",
        color = Color(0xFF0288D1),
        icon = Icons.Default.DirectionsCar
    ),
    FAMILY(
        id = "family",
        titleArabic = "العائلة والأولاد",
        subtitleArabic = "لوازم الأطفال، دراسة وملابس",
        color = Color(0xFFE91E63),
        icon = Icons.Default.FamilyRestroom
    ),
    HEALTH(
        id = "health",
        titleArabic = "الصحة والصيدلية",
        subtitleArabic = "فحص طبي، أدوية وتحاليل",
        color = Color(0xFFE53935),
        icon = Icons.Default.LocalPharmacy
    ),
    CAFE(
        id = "cafe",
        titleArabic = "قهوة، مطاعم وخروج",
        subtitleArabic = "قهوة مع الحباب، أكل سريع وترفيه",
        color = Color(0xFF8D6E63),
        icon = Icons.Default.Fastfood
    ),
    SAVINGS(
        id = "savings",
        titleArabic = "الحصالة والادخار",
        subtitleArabic = "توفير شهري للأيام الصعبة والمستقبل",
        color = Color(0xFF00897B),
        icon = Icons.Default.Savings
    ),
    SALARY(
        id = "salary",
        titleArabic = "الراتب والشهرية",
        subtitleArabic = "الشهرية، أجر إضافي أو مداخيل حرة",
        color = Color(0xFF2E7D32),
        icon = Icons.Default.AttachMoney,
        isExpenseOnly = false
    ),
    OTHER(
        id = "other",
        titleArabic = "طوارئ ومصاريف أخرى",
        subtitleArabic = "مصاريف غير متوقعة ومتنوعة",
        color = Color(0xFF78909C),
        icon = Icons.Default.MoreHoriz
    );

    companion object {
        fun fromId(id: String): BudgetCategory {
            return entries.find { it.id.equals(id, ignoreCase = true) } ?: OTHER
        }

        fun expenseCategories(): List<BudgetCategory> {
            return entries.filter { it != SALARY }
        }
    }
}
