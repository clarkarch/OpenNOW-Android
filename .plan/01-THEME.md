# Phase 1: Design System + Theme

**Goal:** Create new theme files with blue accent, typography, shapes. No existing code changes yet.

**Skills to read first:** `material-3/SKILL.md`, `material-3/references/color-system.md`, `material-3/references/typography-and-shape.md`

**MCP verification:** Use `android-docs_search_android_docs` and `android-docs_get_api_reference` to verify Material3 `darkColorScheme()`, `Typography`, and `Shapes` parameter signatures BEFORE writing code.

**INTEGRATION CHECK (MANDATORY after creating theme):**
1. Verify `darkColorScheme()` parameters match M3 API (EXACT parameter names)
2. Verify `Typography()` parameters match M3 API
3. Verify `Shapes()` only uses stable parameters (extraSmall through extraLarge)
4. Verify all color constants are used by screens/components
5. Verify `AccentPreset.toAccentPreset()` mapper covers all `UiAccent` values
6. Verify `OpenNowTheme` wraps content correctly

---

### Step 1.1: Create directory structure

```bash
mkdir -p app/src/main/java/com/opencloudgaming/opennow/ui/theme
```

### Step 1.2: Create Color.kt

**Create file:** `app/src/main/java/com/opencloudgaming/opennow/ui/theme/Color.kt`

```kotlin
package com.opencloudgaming.opennow.ui.theme

import androidx.compose.ui.graphics.Color

// Backgrounds
val Background = Color(0xFF090B0D)
val Surface = Color(0xFF11161A)
val SurfaceVariant = Color(0xFF171D22)
val SurfaceElevated = Color(0xFF1E252B)

// Text
val OnBackground = Color(0xFFEEF3F5)
val OnSurface = Color(0xFFEEF3F5)
val OnSurfaceVariant = Color(0xFF98A4AA)

// Accent (Pixel Blue)
val Primary = Color(0xFF8AB4F8)
val OnPrimary = Color(0xFF090B0D)
val PrimaryContainer = Color(0xFF1A2A3D)
val OnPrimaryContainer = Color(0xFF8AB4F8)

// Secondary
val Secondary = Color(0xFFBBC7DB)
val OnSecondary = Color(0xFF253140)
val SecondaryContainer = Color(0xFF3B4858)
val OnSecondaryContainer = Color(0xFFD7E3F7)

// Tertiary
val Tertiary = Color(0xFFD6BEE4)
val OnTertiary = Color(0xFF3B2948)
val TertiaryContainer = Color(0xFF523F5F)
val OnTertiaryContainer = Color(0xFFF2DAFF)

// Error
val Error = Color(0xFFFF6B6B)
val OnError = Color(0xFFFFFFFF)
val ErrorContainer = Color(0xFF3D1515)
val OnErrorContainer = Color(0xFFFFB4AB)

// Semantic
val Success = Color(0xFF6AF0A0)
val Warning = Color(0xFFFFD93D)

// Stream-specific
val StreamOverlay = Color(0xCC090B0D)
val StreamControlBg = Color(0xE611161A)

// Accent presets for user selection
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

**Create file:** `app/src/main/java/com/opencloudgaming/opennow/ui/theme/Type.kt`

```kotlin
package com.opencloudgaming.opennow.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val OpenNowTypography = Typography(
    displayLarge = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 40.sp,
        lineHeight = 44.sp,
        letterSpacing = (-0.5).sp,
    ),
    displayMedium = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        lineHeight = 36.sp,
        letterSpacing = (-0.25).sp,
    ),
    displaySmall = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 28.sp,
        lineHeight = 32.sp,
    ),
    headlineLarge = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        lineHeight = 28.sp,
    ),
    headlineMedium = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp,
        lineHeight = 24.sp,
    ),
    headlineSmall = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 18.sp,
        lineHeight = 22.sp,
    ),
    titleLarge = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 18.sp,
        lineHeight = 24.sp,
    ),
    titleMedium = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 22.sp,
        letterSpacing = 0.15.sp,
    ),
    titleSmall = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 18.sp,
        letterSpacing = 0.1.sp,
    ),
    bodyLarge = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
    ),
    bodyMedium = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
    ),
    bodySmall = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp,
    ),
    labelLarge = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp,
    ),
    labelMedium = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp,
    ),
    labelSmall = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 10.sp,
        lineHeight = 14.sp,
        letterSpacing = 0.5.sp,
    ),
)
```

### Step 1.4: Create Shape.kt

**Create file:** `app/src/main/java/com/opencloudgaming/opennow/ui/theme/Shape.kt`

> **API NOTE:** Only 5 parameters are stable in `Shapes()` constructor. `extraExtraLarge` requires `@Material3ExpressiveApi` (alpha). Do NOT use it.

```kotlin
package com.opencloudgaming.opennow.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

val OpenNowShapes = Shapes(
    extraSmall = RoundedCornerShape(4.dp),
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(12.dp),
    large = RoundedCornerShape(16.dp),
    extraLarge = RoundedCornerShape(24.dp),
)

// Component-specific shapes
val CardShape = RoundedCornerShape(12.dp)
val ButtonShape = RoundedCornerShape(999.dp)
val SheetShape = RoundedCornerShape(28.dp)
val SearchBarShape = RoundedCornerShape(999.dp)
val DialogShape = RoundedCornerShape(20.dp)
val StreamCtrlShape = RoundedCornerShape(18.dp)
val ChipShape = RoundedCornerShape(8.dp)
```

### Step 1.5: Create Motion.kt

**Create file:** `app/src/main/java/com/opencloudgaming/opennow/ui/theme/Motion.kt`

```kotlin
package com.opencloudgaming.opennow.ui.theme

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.Spring

// Duration tokens (milliseconds)
const val DurationInstant = 100
const val DurationShort = 200
const val DurationMedium = 300
const val DurationLong = 500
const val DurationExtraLong = 700

// Easing tokens
val EasingStandard = CubicBezierEasing(0.2f, 0.0f, 0.0f, 1.0f)
val EasingDecelerate = CubicBezierEasing(0.0f, 0.0f, 0.0f, 1.0f)
val EasingAccelerate = CubicBezierEasing(0.3f, 0.0f, 0.8f, 0.15f)

// Spring specs
fun <T> springDefault() = spring<T>(
    stiffness = Spring.StiffnessMediumLow,
    dampingRatio = Spring.DampingRatioNoBouncy,
)

fun <T> springSnappy() = spring<T>(
    stiffness = Spring.StiffnessMedium,
    dampingRatio = Spring.DampingRatioNoBouncy,
)

fun <T> springBouncy() = spring<T>(
    stiffness = Spring.StiffnessLow,
    dampingRatio = 0.4f,
)
```

### Step 1.6: Create Theme.kt

**Create file:** `app/src/main/java/com/opencloudgaming/opennow/ui/theme/Theme.kt`

> **API NOTE:** The private helper is named `createOpenNowColorScheme` (not `darkColorScheme`) to avoid shadowing the M3 `darkColorScheme` function.

```kotlin
package com.opencloudgaming.opennow.ui.theme

import android.os.Build
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private fun createOpenNowColorScheme(accent: AccentPreset = AccentPreset.Pixel) = darkColorScheme(
    primary = accent.color,
    onPrimary = OnPrimary,
    primaryContainer = PrimaryContainer,
    onPrimaryContainer = OnPrimaryContainer,
    secondary = Secondary,
    onSecondary = OnSecondary,
    secondaryContainer = SecondaryContainer,
    onSecondaryContainer = OnSecondaryContainer,
    tertiary = Tertiary,
    onTertiary = OnTertiary,
    tertiaryContainer = TertiaryContainer,
    onTertiaryContainer = OnTertiaryContainer,
    background = Background,
    onBackground = OnBackground,
    surface = Surface,
    onSurface = OnSurface,
    surfaceVariant = SurfaceVariant,
    onSurfaceVariant = OnSurfaceVariant,
    surfaceTint = accent.color,
    error = Error,
    onError = OnError,
    errorContainer = ErrorContainer,
    onErrorContainer = OnErrorContainer,
)

@Composable
fun OpenNowTheme(
    accent: AccentPreset = AccentPreset.Pixel,
    useDynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    val colorScheme = when {
        useDynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            dynamicDarkColorScheme(LocalContext.current)
        }
        else -> createOpenNowColorScheme(accent)
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = OpenNowTypography,
        shapes = OpenNowShapes,
        content = content,
    )
}
```

### Accent Color Migration

The existing theme reads `UiAccent` from `AppSettings` and maps it to colors. The new `OpenNowTheme` uses `AccentPreset` enum. Add a mapper in `Theme.kt`:

```kotlin
// Add to Theme.kt
fun UiAccent.toAccentPreset(): AccentPreset = when (this) {
    UiAccent.OpenNow -> AccentPreset.OpenNow
    UiAccent.Pixel -> AccentPreset.Pixel
    UiAccent.HotPink -> AccentPreset.HotPink
    UiAccent.Lime -> AccentPreset.Lime
    UiAccent.Coral -> AccentPreset.Coral
    UiAccent.Violet -> AccentPreset.Violet
}
```

Then in `AppNavigation.kt`, read the accent from settings:

```kotlin
val appState by viewModel.appState.collectAsStateWithLifecycle()
val accent = appState.settings.uiAccent.toAccentPreset()

OpenNowTheme(accent = accent, useDynamicColor = appState.settings.dynamicColor) {
    // NavHost here
}
```

### Step 1.7: Update default accent in Models.kt

**Edit file:** `app/src/main/java/com/opencloudgaming/opennow/Models.kt`

Find:
```kotlin
val uiAccent: UiAccent = UiAccent.OpenNow,
```

Replace with:
```kotlin
val uiAccent: UiAccent = UiAccent.Pixel,
```

This forces Pixel Blue as the new default accent for all users (existing users keep their saved setting, only new installs get the new default).
