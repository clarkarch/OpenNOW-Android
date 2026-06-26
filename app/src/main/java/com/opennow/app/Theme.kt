package com.opennow.app

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val BlueAccent = androidx.compose.ui.graphics.Color(0xFF1E88E5)
private val DarkBackground = androidx.compose.ui.graphics.Color(0xFF0D1117)
private val DarkSurface = androidx.compose.ui.graphics.Color(0xFF161B22)
private val DarkPanel = androidx.compose.ui.graphics.Color(0xFF21262D)

private val DarkColorScheme = darkColorScheme(
    primary = BlueAccent,
    onPrimary = androidx.compose.ui.graphics.Color.White,
    background = DarkBackground,
    surface = DarkSurface,
    surfaceVariant = DarkPanel,
    onBackground = androidx.compose.ui.graphics.Color(0xFFE6EDF3),
    onSurface = androidx.compose.ui.graphics.Color(0xFFE6EDF3),
)

@Composable
fun OpenNowTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        content = content,
    )
}
