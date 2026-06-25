package com.opencloudgaming.opennow.ui.theme

import android.os.Build
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.opencloudgaming.opennow.AppSettings
import com.opencloudgaming.opennow.UiAccent
import androidx.compose.ui.unit.dp

private val Green = Color(0xff6af0a0)
val Background = Color(0xff090b0d)
val Panel = Color(0xff11161a)
val PanelAlt = Color(0xff171d22)
val TextPrimary = Color(0xffeef3f5)
val TextMuted = Color(0xff98a4aa)
val SettingsBackground = Color(0xff090b0d)
val SettingsPanel = Color(0xff11161a)
val SettingsPanelAlt = Color(0xff171d22)
val SettingsText = Color(0xffeef3f5)
val SettingsTextMuted = Color(0xff98a4aa)
val PhoneNavRailMaxSmallestWidth = 600.dp

val UiAccent.color: Color
    get() = when (this) {
        UiAccent.OpenNow -> Green
        UiAccent.Pixel -> Color(0xff8ab4f8)
        UiAccent.HotPink -> Color(0xffff4fb8)
        UiAccent.Lime -> Color(0xffc7ef6b)
        UiAccent.Coral -> Color(0xffff8d7a)
        UiAccent.Violet -> Color(0xffc7a4ff)
    }

@Composable
fun uiAccentLabel(accent: UiAccent): String = when (accent) {
    UiAccent.OpenNow -> "Open Now"
    UiAccent.Pixel -> "Pixel"
    UiAccent.HotPink -> "Hot Pink"
    UiAccent.Lime -> "Lime"
    UiAccent.Coral -> "Coral"
    UiAccent.Violet -> "Violet"
}

@Composable
fun OpenNowTheme(settings: AppSettings, content: @Composable () -> Unit) {
    val context = LocalContext.current
    val accent = settings.uiAccent.color
    val fallbackScheme = darkColorScheme(
        primary = accent,
        onPrimary = Color(0xff08090c),
        background = Background,
        surface = Panel,
        surfaceVariant = PanelAlt,
        onBackground = TextPrimary,
        onSurface = TextPrimary,
        onSurfaceVariant = TextMuted,
    )
    val colorScheme = if (settings.dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        dynamicDarkColorScheme(context)
    } else {
        fallbackScheme
    }
    MaterialTheme(
        colorScheme = colorScheme,
        content = content,
    )
}
