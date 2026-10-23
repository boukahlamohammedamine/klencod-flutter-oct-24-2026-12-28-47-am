package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.util.AlgerianCurrencyUtil
import com.example.ui.viewmodel.CurrencyDisplayMode

@Composable
fun CurrencyDisplay(
    amountDzd: Double,
    currencyMode: CurrencyDisplayMode,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onSurface,
    fontSize: TextUnit = 16.sp,
    fontWeight: FontWeight = FontWeight.Bold,
    showPrefixSign: Boolean = false,
    isIncome: Boolean = false
) {
    val sign = if (showPrefixSign) {
        if (isIncome) "+ " else "- "
    } else ""

    when (currencyMode) {
        CurrencyDisplayMode.DZD_ONLY -> {
            Text(
                text = "$sign${AlgerianCurrencyUtil.formatDzd(amountDzd)}",
                color = color,
                fontSize = fontSize,
                fontWeight = fontWeight,
                modifier = modifier
            )
        }
        CurrencyDisplayMode.CENTIMES_ONLY -> {
            Text(
                text = "$sign${AlgerianCurrencyUtil.formatCentimesShort(amountDzd)}",
                color = color,
                fontSize = fontSize,
                fontWeight = fontWeight,
                modifier = modifier
            )
        }
        CurrencyDisplayMode.BOTH -> {
            Column(
                modifier = modifier,
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "$sign${AlgerianCurrencyUtil.formatDzd(amountDzd)}",
                    color = color,
                    fontSize = fontSize,
                    fontWeight = fontWeight
                )
                Text(
                    text = AlgerianCurrencyUtil.formatCentimesShort(amountDzd),
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.85f),
                    fontSize = (fontSize.value * 0.75f).coerceAtLeast(11f).sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}
