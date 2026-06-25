# Phase 1: Design System + Theme

**Goal:** Create theme files. No existing code changes yet.

**YAGNI:** No Motion.kt — use literal `200f` values. No component-specific shapes — use inline `RoundedCornerShape(12.dp)`.

---

### Step 1.1: Create directory

```bash
mkdir -p app/src/main/java/com/opencloudgaming/opennow/ui/theme
```

### Step 1.2: Create Color.kt

**Create:** `app/src/main/java/com/opencloudgaming/opennow/ui/theme/Color.kt`

```kotlin
package com.opencloudgaming.opennow.ui.theme

import androidx.compose.ui.graphics.Color

val Background = Color(0xFF090B0D)
val Surface = Color(0xFF11161A)
val SurfaceVariant = Color(0xFF171D22)
val OnBackground = Color(0xFFEEF3F5)
val OnSurface = Color(0xFFEEF3F5)
val OnSurfaceVariant = Color(0xFF98A4AA)
val Primary = Color(0xFF8AB4F8)
val OnPrimary = Color(0xFF090B0D)
val Error = Color(0xFFFF6B6B)

enum class AccentPreset(val label: String, val color: Color) {
    OpenNow("Open Now", Color(0xFF6AF0A0)),
    Pixel("Pixel", Color(0xFF8AB4F8)),
    HotPink("Hot Pink", Color(0xFFFF4FB8)),
    Lime("Lime", Color(0xFFC7EF6B)),
    Coral("Coral", Color(0xFFFF8D7A)),
    Violet("Violet", Color(0xFFC7A4FF)),
}
```

### Step 1.3: Create Type.kt

**Create:** `app/src/main/java/com/opencloudgaming/opennow/ui/theme/Type.kt`

```kotlin
package com.opencloudgaming.opennow.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val OpenNowTypography = Typography(
    displayLarge = TextStyle(fontWeight = FontWeight.Bold, fontSize = 40.sp, lineHeight = 44.sp),
    displayMedium = TextStyle(fontWeight = FontWeight.Bold, fontSize = 32.sp, lineHeight = 36.sp),
    headlineLarge = TextStyle(fontWeight = FontWeight.Bold, fontSize = 24.sp, lineHeight = 28.sp),
    headlineMedium = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 20.sp, lineHeight = 24.sp),
    titleLarge = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 18.sp, lineHeight = 24.sp),
    titleMedium = TextStyle(fontWeight = FontWeight.Medium, fontSize = 16.sp, lineHeight = 22.sp),
    bodyLarge = TextStyle(fontSize = 16.sp, lineHeight = 24.sp),
    bodyMedium = TextStyle(fontSize = 14.sp, lineHeight = 20.sp),
    bodySmall = TextStyle(fontSize = 12.sp, lineHeight = 16.sp),
    labelLarge = TextStyle(fontWeight = FontWeight.Medium, fontSize = 14.sp, lineHeight = 20.sp),
    labelMedium = TextStyle(fontWeight = FontWeight.Medium, fontSize = 12.sp, lineHeight = 16.sp),
)
```

### Step 1.4: Create Theme.kt

**Create:** `app/src/main/java/com/opencloudgaming/opennow/ui/theme/Theme.kt`

```kotlin
package com.opencloudgaming.opennow.ui.theme

import android.os.Build
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.opencloudgaming.opennow.UiAccent

private val OpenNowShapes = Shapes(
    extraSmall = androidx.compose.foundation.shape.RoundedCornerShape(4.dp),
    small = androidx.compose.foundation.shape.RoundedCornerShape(8.dp),
    medium = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
    large = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
    extraLarge = androidx.compose.foundation.shape.RoundedCornerShape(24.dp),
)

private fun createColorScheme(accent: AccentPreset) = darkColorScheme(
    primary = accent.color,
    onPrimary = OnPrimary,
    background = Background,
    onBackground = OnBackground,
    surface = Surface,
    onSurface = OnSurface,
    surfaceVariant = SurfaceVariant,
    onSurfaceVariant = OnSurfaceVariant,
    error = Error,
)

@Composable
fun OpenNowTheme(
    accent: AccentPreset = AccentPreset.Pixel,
    content: @Composable () -> Unit,
) {
    val colorScheme = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        dynamicDarkColorScheme(LocalContext.current)
    } else {
        createColorScheme(accent)
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = OpenNowTypography,
        shapes = OpenNowShapes,
        content = content,
    )
}

fun UiAccent.toAccentPreset(): AccentPreset = when (this) {
    UiAccent.OpenNow -> AccentPreset.OpenNow
    UiAccent.Pixel -> AccentPreset.Pixel
    UiAccent.HotPink -> AccentPreset.HotPink
    UiAccent.Lime -> AccentPreset.Lime
    UiAccent.Coral -> AccentPreset.Coral
    UiAccent.Violet -> AccentPreset.Violet
}
```

### Step 1.5: Update default accent in Models.kt

**Edit:** `app/src/main/java/com/opencloudgaming/opennow/Models.kt`

Find: `val uiAccent: UiAccent = UiAccent.OpenNow,`
Replace: `val uiAccent: UiAccent = UiAccent.Pixel,`
