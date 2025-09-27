package com.cs407.cardfolio.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// Custom theme extension to hold app-specific colors (like gradients)
// These are not part of the default Material3 color scheme.
data class CustomColors(
    val gradientTop: Color,
    val gradientBottom: Color
)

// CompositionLocal to provide access to CustomColors throughout the app
private val LocalCustomColors = staticCompositionLocalOf {
    CustomColors(
        gradientTop = Color.Unspecified,
        gradientBottom = Color.Unspecified
    )
}

// Default Material 3 dark color scheme for the app
private val DarkColorScheme = darkColorScheme(
    tertiary = Pink80,
    primary = PrimaryBlue,
    secondary = SecondaryPink,
    surface = SurfaceBackgroundDark,
    onSurface = Color.White,
    onSurfaceVariant = Color.LightGray,
    outline = OutlineLight
)

// Default Material 3 light color scheme for the app
private val LightColorScheme = lightColorScheme(
    tertiary = Pink40,
    primary = PrimaryBlue,
    secondary = SecondaryPink,
    surface = SurfaceBackground,
    onSurface = Color.Black,
    onSurfaceVariant = Color.Gray,
    outline = OutlineLight
)

// Light mode gradient values
val LightCustomColors = CustomColors(
    gradientTop = LightGradientTop,
    gradientBottom = LightGradientBottom
)

// Dark mode gradient values
val DarkCustomColors = CustomColors(
    gradientTop = DarkGradientTop,
    gradientBottom = DarkGradientBottom
)

// Singleton object to expose custom colors anywhere in the app
object AppTheme {
    val customColors: CustomColors
        @Composable
        @ReadOnlyComposable
        get() = LocalCustomColors.current
}

/**
 * Top-level theme composable for the app.
 *
 * - Supports dark/light themes
 * - Optionally uses dynamic colors (on Android 12+)
 * - Provides both Material3 colors and app-specific CustomColors
 */
@Composable
fun CardfolioTheme(
    darkTheme: Boolean = isSystemInDarkTheme(), // Detect system dark mode by default
    dynamicColor: Boolean = true,               // Enable Material You dynamic colors if supported
    content: @Composable () -> Unit             // UI content wrapped in this theme
) {
    // Choose appropriate color scheme
    val colorScheme = when {
        // On Android 12 (S) and above, use dynamic colors if enabled
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        // Fallback to predefined dark or light schemes
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    // Select custom gradient colors depending on theme
    val currentCustomColors = if (darkTheme) DarkCustomColors else LightCustomColors

    // Set status bar appearance (light/dark icons depending on theme)
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            val insetsController = WindowCompat.getInsetsController(window, view)
            insetsController.isAppearanceLightStatusBars = !darkTheme
        }
    }

    // Provide both MaterialTheme colors and our CustomColors to the app
    CompositionLocalProvider(LocalCustomColors provides currentCustomColors) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            shapes = AppShapes,
            content = content
        )
    }
}
