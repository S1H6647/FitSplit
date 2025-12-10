package com.fitness.fitsplit.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.fitness.fitsplit.ui.theme.DarkBackground
import com.fitness.fitsplit.ui.theme.DarkContainer
import com.fitness.fitsplit.ui.theme.DarkError
import com.fitness.fitsplit.ui.theme.DarkOutline
import com.fitness.fitsplit.ui.theme.DarkSecondary
import com.fitness.fitsplit.ui.theme.DarkSurface
import com.fitness.fitsplit.ui.theme.DarkTertiary
import com.fitness.fitsplit.ui.theme.LightBackground
import com.fitness.fitsplit.ui.theme.LightContainer
import com.fitness.fitsplit.ui.theme.LightError
import com.fitness.fitsplit.ui.theme.LightOutline
import com.fitness.fitsplit.ui.theme.LightSecondary
import com.fitness.fitsplit.ui.theme.LightSurface
import com.fitness.fitsplit.ui.theme.LightTertiary
import com.fitness.fitsplit.ui.theme.OnDarkBackground
import com.fitness.fitsplit.ui.theme.OnDarkContainer
import com.fitness.fitsplit.ui.theme.OnDarkSurface
import com.fitness.fitsplit.ui.theme.OnLightBackground
import com.fitness.fitsplit.ui.theme.OnLightContainer
import com.fitness.fitsplit.ui.theme.OnLightSurface
import com.fitness.fitsplit.ui.theme.Primary
import com.fitness.fitsplit.ui.theme.PrimaryLight


private val LightColorScheme = lightColorScheme(
    // Primary colors
    primary = Primary,
    onPrimary = Color.White,
    primaryContainer = LightContainer,
    onPrimaryContainer = OnLightContainer,

    // Secondary colors
    secondary = LightSecondary,
    onSecondary = Color.White,
    secondaryContainer = LightSurface,
    onSecondaryContainer = OnLightSurface,

    // Tertiary colors
    tertiary = LightTertiary,
    onTertiary = Color.White,
    tertiaryContainer = LightContainer,
    onTertiaryContainer = OnLightContainer,

    // Error colors
    error = LightError,
    onError = Color.White,
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF410002),

    // Background colors
    background = LightBackground,
    onBackground = OnLightBackground,

    // Surface colors
    surface = LightSurface,
    onSurface = OnLightSurface,
    surfaceVariant = LightContainer,
    onSurfaceVariant = OnLightContainer,

    // Outline colors
    outline = LightOutline,
    outlineVariant = Color(0xFFE0E4F0),

    // Other colors
    scrim = Color.Black.copy(alpha = 0.5f),
    inverseSurface = Color(0xFF313033),
    inverseOnSurface = Color(0xFFF4EFF4),
    inversePrimary = PrimaryLight,

    // Surface tint
    surfaceTint = Primary
)


private val DarkColorScheme = darkColorScheme(
    // Primary colors
    primary = Primary,
    onPrimary = Color.White,
    primaryContainer = DarkContainer,
    onPrimaryContainer = OnDarkContainer,

    // Secondary colors
    secondary = DarkSecondary,
    onSecondary = DarkBackground,
    secondaryContainer = DarkSurface,
    onSecondaryContainer = OnDarkSurface,

    // Tertiary colors
    tertiary = DarkTertiary,
    onTertiary = DarkBackground,
    tertiaryContainer = DarkContainer,
    onTertiaryContainer = OnDarkContainer,

    // Error colors
    error = DarkError,
    onError = Color(0xFF690005),
    errorContainer = Color(0xFF93000A),
    onErrorContainer = DarkError,

    // Background colors
    background = DarkBackground,
    onBackground = OnDarkBackground,

    // Surface colors
    surface = DarkSurface,
    onSurface = OnDarkSurface,
    surfaceVariant = DarkContainer,
    onSurfaceVariant = OnDarkContainer,

    // Outline colors
    outline = DarkOutline,
    outlineVariant = Color(0xFF2C2F3C),

    // Other colors
    scrim = Color.Black.copy(alpha = 0.7f),
    inverseSurface = Color(0xFFE6E1E5),
    inverseOnSurface = Color(0xFF313033),
    inversePrimary = Primary,

    // Surface tint
    surfaceTint = Primary
)

@Composable
fun FitSplitTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}