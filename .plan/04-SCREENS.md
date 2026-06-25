# Phase 4: Extract Screens

**Goal:** Move Login, Home, Library, Settings composables from `OpenNowScreens.kt` into separate files. Copy-paste exact code, update package, fix imports.

**MCP:** Before writing each screen, verify:
```
android-docs_search_android_docs("material3 ExposedDropdownMenuBox")
android-docs_get_api_reference("androidx.compose.material3.ExposedDropdownMenuBox")
android-docs_search_android_docs("material3 PullToRefreshBox")
android-docs_search_android_docs("coil3 compose AsyncImage")
```
**YAGNI Checklist (per screen):**
- No generic components used once → inline
- No "reusable" search bar → inline OutlinedTextField
- No wrapper composables → write directly
- No animation code → literal values only
**KISS Checklist:**
- Copy exact code from OpenNowScreens.kt
- Change package declaration
- Add needed imports
- Make function public (remove `private`)
- That's it. Don't "improve" anything.
**DO NOT RUN:** `./gradlew` or any build command.

---

### Step 4.1: Create directories

```bash
mkdir -p app/src/main/java/com/opencloudgaming/opennow/ui/screens/login
mkdir -p app/src/main/java/com/opencloudgaming/opennow/ui/screens/home
mkdir -p app/src/main/java/com/opencloudgaming/opennow/ui/screens/library
mkdir -p app/src/main/java/com/opencloudgaming/opennow/ui/screens/settings
mkdir -p app/src/main/java/com/opencloudgaming/opennow/ui/components
```

### Step 4.2: Create UrlImage.kt (Coil 3 wrapper)

**Create:** `app/src/main/java/com/opencloudgaming/opennow/ui/components/UrlImage.kt`

```kotlin
package com.opencloudgaming.opennow.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage

@Composable
fun UrlImage(url: String?, modifier: Modifier = Modifier) {
    AsyncImage(
        model = url,
        contentDescription = null,
        modifier = modifier,
        contentScale = ContentScale.Crop,
    )
}
```

**Replace** existing `UrlImage` in `OpenNowScreens.kt` (line 5719) with:
```kotlin
import com.opencloudgaming.opennow.ui.components.UrlImage
```

Delete the old `private fun UrlImage` (lines 5719-5750).

### Step 4.3: Extract LoginScreen

**Create:** `app/src/main/java/com/opencloudgaming/opennow/ui/screens/login/LoginScreen.kt`

Copy from `OpenNowScreens.kt`:
- `LoginScreen` (lines 341-401)
- `TvDeviceLoginScreen` (lines 404-425)
- `DeviceLoginPanel` (lines 428-?)
- `DeviceLoginQr` (lines ?)
- `DeviceLoginControls` (lines ?)
- `QrCodeView` (lines ?)
- `openExternalUrl` (lines ?)
- `secondsUntil` (lines 624-625)

**Package:** `com.opencloudgaming.opennow.ui.screens.login`

**Imports needed:**
```kotlin
package com.opencloudgaming.opennow.ui.screens.login

import com.opencloudgaming.opennow.OpenNowUiState
import com.opencloudgaming.opennow.OpenNowViewModel
import com.opencloudgaming.opennow.DeviceLoginPrompt
import com.opencloudgaming.opennow.LoginProvider
import com.opencloudgaming.opennow.QrCode
import com.opencloudgaming.opennow.ui.theme.TextPrimary
import com.opencloudgaming.opennow.ui.theme.TextMuted
import com.opencloudgaming.opennow.ui.components.UrlImage
// ... all other standard Compose imports
```

**Make these functions public** (remove `private`):
- `LoginScreen`
- `TvDeviceLoginScreen`
- `LoadingScreen` — also move here since it's used in LoginScreen and AppNavigation

### Step 4.4: Extract HomeScreen

**Create:** `app/src/main/java/com/opencloudgaming/opennow/ui/screens/home/HomeScreen.kt`

Copy from `OpenNowScreens.kt`:
- `HomeScreen` (line ~850-?)
- `StoreScrollableControls`
- `StoreScrollActionButton`
- `GameGrid`
- `StoreGameGrid`
- `gameGridColumnCount`
- `GameCard`
- `ThumbnailStoreButton`
- `launcherBadgeForGame`
- `launcherBadgeForStoreKey`
- `displayStoresForGame`
- `ThumbnailPlayButton`
- `AnimatedLaunchOverlay`
- `GameDetailsSheet`
- `GameDetailsLandscapeContent`
- `GameDetailsScrollableContent`
- `LaunchOptionsList`
- `LongPressPlayButton`
- `variantDetailsText`
- `ImageCloseButton`
- `FavoriteIconButton`
- `gameDescriptionForDetails`
- `gameDetailChips`
- `formatGameMetadataLabel`
- `isNoisyGameTag`
- `CompactDetailRows`
- `DetailRows`
- `gameMatchesSearch`
- `StoreLaunchSelector`
- `SearchEmptyState`
- `RefreshingGamesPlaceholder`
- `SwipeToRefreshContainer`

**Package:** `com.opencloudgaming.opennow.ui.screens.home`

**Imports needed:**
```kotlin
package com.opencloudgaming.opennow.ui.screens.home

import com.opencloudgaming.opennow.OpenNowUiState
import com.opencloudgaming.opennow.OpenNowViewModel
import com.opencloudgaming.opennow.GameInfo
import com.opencloudgaming.opennow.GameVariant
import com.opencloudgaming.opennow.ui.components.UrlImage
import com.opencloudgaming.opennow.ui.theme.Background
import com.opencloudgaming.opennow.ui.theme.Panel
import com.opencloudgaming.opennow.ui.theme.PanelAlt
import com.opencloudgaming.opennow.ui.theme.TextPrimary
import com.opencloudgaming.opennow.ui.theme.TextMuted
import com.opencloudgaming.opennow.ui.theme.color
// ... all other standard Compose imports
```

**Make these functions public** (remove `private`):
- `HomeScreen`
- `GameDetailsSheet`
- `StoreLaunchSelector`

### Step 4.5: Extract LibraryScreen

**Create:** `app/src/main/java/com/opencloudgaming/opennow/ui/screens/library/LibraryScreen.kt`

Copy from `OpenNowScreens.kt`:
- `LibraryScreen` (line ~?)
- `ActiveSessionResumeCard`
- `activeSessionGame`
- `activeSessionSummary`

**Package:** `com.opencloudgaming.opennow.ui.screens.library`

**Make these functions public:**
- `LibraryScreen`

### Step 4.6: Extract SettingsScreen

**Create:** `app/src/main/java/com/opencloudgaming/opennow/ui/screens/settings/SettingsScreen.kt`

Copy from `OpenNowScreens.kt`:
- `SettingsScreen` (line ~?)
- `SettingsContent`
- `AppDataSettingsPanel`
- `AccountSettingsPanel`
- `AuthSession.toSavedAccount()` extension
- `CodecDiagnosticsPanel`
- `formatCodecDiagnosticReport`
- `CodecSummaryChip`
- `CodecCapabilityRow`
- `AppVersionPanel`
- `DebugLogsPanel`
- `SessionProxyWarningDialog`
- All settings helper composables: `SettingsSection`, `SettingSwitch`, `NumberSlider`, `ChoiceRow`, `ChoiceMenuRow`, `ChoiceOptionRow`, `SortPicker`, `SelectedFilterChips`, `FilterMenu`, `PrintedWasteSelector`, and related helpers

**Package:** `com.opencloudgaming.opennow.ui.screens.settings`

**Data classes to move:**
- `SettingsChoiceOption` (line 202)
- `ChoiceMenuOption` (lines 203-208)
- `LauncherBadge` (lines 210-215) — also used by HomeScreen, so keep in a shared location or duplicate
- `keyboardLayoutOptions` (lines 217-234)
- `gameLanguageOptions` (lines 236-267)

**Make these functions public:**
- `SettingsScreen`

### Step 4.7: Move shared helpers

Some functions are used across multiple screens. These go into `ui/components/common.kt` or stay where they're used:

**Shared helpers used by multiple screens:**
- `gameMatchesSearch` — used by HomeScreen and LibraryScreen
- `isPhoneLandscape` / `isPhonePortrait` — used by MainShell
- `formatRuntimeBitrate` — used by StreamScreen
- `shouldHideStreamStatusText` — used by StreamScreen

**Decision:** Keep `gameMatchesSearch` in `HomeScreen.kt` and import from there in LibraryScreen (or duplicate — YAGNI says don't create a shared module for 2 uses).

### Step 4.8: Update OpenNowScreens.kt imports

After extracting, `OpenNowScreens.kt` should be left with only:
- `OpenNowApp` (entry point)
- `MainShell` (orchestration shell)
- `TopStatusBar`
- `AppNavigationRail` / `AppNavigationRailItem` / `BottomNavItem`
- `NativeSearchField`
- Various DPad/TV focus helpers

These stay because they're the orchestration layer that connects everything.

### Step 4.9: Verify

```bash
# 1. No duplicate function definitions
grep -rn "private fun LoginScreen\|private fun HomeScreen\|private fun LibraryScreen\|private fun SettingsScreen" app/src/main/java/com/opencloudgaming/opennow/OpenNowScreens.kt
# Should return ZERO (they're now in separate files as public)

# 2. All screen files compile (check imports resolve)
# Done via CI

# 3. Function signatures match callers
grep -n "LoginScreen(" app/src/main/java/com/opencloudgaming/opennow/OpenNowScreens.kt
grep -n "HomeScreen(" app/src/main/java/com/opencloudgaming/opennow/ui/navigation/AppNavigation.kt
```
