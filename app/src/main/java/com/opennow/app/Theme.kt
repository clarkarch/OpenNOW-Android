package com.opennow.app

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val BlueAccent = Color(0xFF1E88E5)
private val DarkBackground = Color(0xFF0D1117)
private val DarkSurface = Color(0xFF161B22)
private val DarkPanel = Color(0xFF21262D)
private val DarkMuted = Color(0xFF8B949E)
private val DarkError = Color(0xFFF85149)

private val DarkColorScheme = darkColorScheme(
    primary = BlueAccent,
    onPrimary = Color.White,
    background = DarkBackground,
    surface = DarkSurface,
    surfaceVariant = DarkPanel,
    onBackground = Color(0xFFE6EDF3),
    onSurface = Color(0xFFE6EDF3),
    onSurfaceVariant = DarkMuted,
    secondary = BlueAccent,
    onSecondary = Color.White,
    error = DarkError,
    onError = Color.White,
    outline = DarkMuted,
)

@Composable
fun OpenNowTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        content = content,
    )
}
