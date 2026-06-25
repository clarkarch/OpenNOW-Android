# Phase 1: Extract Theme

**Goal:** Move theme code from `OpenNowScreens.kt` into `ui/theme/Theme.kt`. Zero logic changes — pure code motion.

**MCP:** No API calls needed — this is pure code motion.
**YAGNI:** No new abstractions. Just move existing code.
**KISS:** Copy-paste exact code, change package declaration.
**DO NOT RUN:** `./gradlew` or any build command.

---

### Step 1.1: Create directory

```bash
mkdir -p app/src/main/java/com/opencloudgaming/opennow/ui/theme
```

### Step 1.2: Create Theme.kt

**Create:** `app/src/main/java/com/opencloudgaming/opennow/ui/theme/Theme.kt`

Copy these exact blocks from `OpenNowScreens.kt` into the new file:

1. **Color constants** (lines 189-200):
```kotlin
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
```

2. **UiAccent.color extension** (lines 268-276):
```kotlin
val UiAccent.color: Color
    get() = when (this) {
        UiAccent.OpenNow -> Green
        UiAccent.Pixel -> Color(0xff8ab4f8)
        UiAccent.HotPink -> Color(0xffff4fb8)
        UiAccent.Lime -> Color(0xffc7ef6b)
        UiAccent.Coral -> Color(0xffff8d7a)
        UiAccent.Violet -> Color(0xffc7a4ff)
    }
```

3. **uiAccentLabel** (lines 278-286):
```kotlin
@Composable
fun uiAccentLabel(accent: UiAccent): String = when (accent) {
    UiAccent.OpenNow -> "Open Now"
    UiAccent.Pixel -> "Pixel"
    UiAccent.HotPink -> "Hot Pink"
    UiAccent.Lime -> "Lime"
    UiAccent.Coral -> "Coral"
    UiAccent.Violet -> "Violet"
}
```

> **NOTE:** Replace `stringResource(R.string.accent_xxx)` with hardcoded strings. The string resources are only used in this one place and YAGNI says don't add complexity for one use.

4. **OpenNowTheme** (lines 288-311):
```kotlin
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
```

### Step 1.3: Update OpenNowScreens.kt

In `OpenNowScreens.kt`, replace the old private declarations with imports:

**Delete** these lines from `OpenNowScreens.kt`:
- Lines 189-200 (color constants)
- Lines 268-286 (UiAccent.color + uiAccentLabel)
- Lines 288-311 (OpenNowTheme)

**Add** at top of file:
```kotlin
import com.opencloudgaming.opennow.ui.theme.OpenNowTheme
import com.opencloudgaming.opennow.ui.theme.Background
import com.opencloudgaming.opennow.ui.theme.Panel
import com.opencloudgaming.opennow.ui.theme.PanelAlt
import com.opencloudgaming.opennow.ui.theme.TextPrimary
import com.opencloudgaming.opennow.ui.theme.TextMuted
import com.opencloudgaming.opennow.ui.theme.SettingsBackground
import com.opencloudgaming.opennow.ui.theme.SettingsPanel
import com.opencloudgaming.opennow.ui.theme.SettingsPanelAlt
import com.opencloudgaming.opennow.ui.theme.SettingsText
import com.opencloudgaming.opennow.ui.theme.SettingsTextMuted
import com.opencloudgaming.opennow.ui.theme.color
import com.opencloudgaming.opennow.ui.theme.uiAccentLabel
```

### Step 1.4: Verify

```bash
# All color references in OpenNowScreens.kt should now resolve via imports
grep -n "private val Green\|private val Background\|private val Panel\|private fun OpenNowTheme\|private fun uiAccentLabel" app/src/main/java/com/opencloudgaming/opennow/OpenNowScreens.kt
# Should return ZERO results
```
