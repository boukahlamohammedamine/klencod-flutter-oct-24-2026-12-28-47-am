package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val LightColorScheme = lightColorScheme(
    primary = AlgerianGreen,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFC8E6C9),
    onPrimaryContainer = Color(0xFF002204),
    secondary = AlgerianGold,
    onSecondary = Color.Black,
    secondaryContainer = Color(0xFFFFF3CD),
    onSecondaryContainer = Color(0xFF2E1F00),
    tertiary = Color(0xFF006874),
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFF97F0FF),
    onTertiaryContainer = Color(0xFF001F24),
    error = AlgerianRed,
    onError = Color.White,
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF410002),
    background = SurfaceSand,
    onBackground = TextPrimary,
    surface = SurfaceCard,
    onSurface = TextPrimary,
    surfaceVariant = SurfaceCardVariant,
    onSurfaceVariant = TextSecondary,
    outline = Color(0xFF717970)
)

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF81C784),
    onPrimary = Color(0xFF00390B),
    primaryContainer = AlgerianGreenDark,
    onPrimaryContainer = Color(0xFFA5D6A7),
    secondary = Color(0xFFFFD54F),
    onSecondary = Color(0xFF402D00),
    secondaryContainer = Color(0xFF5C4300),
    onSecondaryContainer = Color(0xFFFFE082),
    tertiary = Color(0xFF4DD0E1),
    onTertiary = Color(0xFF00363D),
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005),
    background = Color(0xFF111411),
    onBackground = Color(0xFFE2E3DE),
    surface = Color(0xFF1A1C1A),
    onSurface = Color(0xFFE2E3DE),
    surfaceVariant = Color(0xFF262C25),
    onSurfaceVariant = Color(0xFFC1C9BF),
    outline = Color(0xFF8B938A)
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Keep branded Algerian Green & Gold palette
    content: @Composable () -> Unit,
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
