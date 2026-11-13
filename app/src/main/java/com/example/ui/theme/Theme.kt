package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.data.model.AppThemePreset

private val DefaultLightBlueColorScheme = lightColorScheme(
    primary = CorporateBlue,
    onPrimary = Color.White,
    primaryContainer = LightBlueSurface,
    onPrimaryContainer = DeepExecutiveBlue,
    secondary = AccentGold,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFFEF3C7),
    onSecondaryContainer = Color(0xFF78350F),
    tertiary = SuccessEmerald,
    background = LightBlueBg,
    onBackground = DeepExecutiveBlue,
    surface = Color.White,
    onSurface = DeepExecutiveBlue,
    surfaceVariant = LightBlueSurface,
    onSurfaceVariant = CorporateDarkBlue,
    outline = LightBlueBorder
)

@Composable
fun MyApplicationTheme(
    activeTheme: AppThemePreset? = null,
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (activeTheme != null) {
        lightColorScheme(
            primary = Color(activeTheme.primaryHex),
            onPrimary = Color.White,
            primaryContainer = Color(activeTheme.surfaceHex),
            onPrimaryContainer = Color(activeTheme.primaryHex),
            secondary = AccentGold,
            onSecondary = Color.White,
            secondaryContainer = Color(0xFFFEF3C7),
            onSecondaryContainer = Color(0xFF78350F),
            tertiary = SuccessEmerald,
            background = Color(activeTheme.bgHex),
            onBackground = DeepExecutiveBlue,
            surface = Color.White,
            onSurface = DeepExecutiveBlue,
            surfaceVariant = Color(activeTheme.surfaceHex),
            onSurfaceVariant = Color(activeTheme.primaryHex),
            outline = Color(activeTheme.borderHex)
        )
    } else {
        DefaultLightBlueColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
