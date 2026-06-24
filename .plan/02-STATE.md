# Phase 2: State Decomposition

**Goal:** Break the 68-field god object into feature-scoped states.

**Skills to read first:** `compose-state-authoring/SKILL.md`, `compose-state-hoisting/SKILL.md`, `kotlin-flow-state-event-modeling/SKILL.md`

**MCP verification:** Use `android-docs_search_android_docs` and `android-docs_get_api_reference` to verify `MutableStateFlow`, `StateFlow`, `combine`, and `collectAsStateWithLifecycle` signatures BEFORE writing code.

**INTEGRATION CHECK (MANDATORY after modifying ViewModel):**
1. Verify ALL legacy field names in the bridge code match `OpenNowUiState` fields
2. Verify `appState` combine includes ALL sub-flows
3. Verify `stateIn` uses correct `SharingStarted.WhileSubscribed(5000)`
4. Verify `selectSettingsCategory()` method exists and is callable
5. Verify streamState bridge includes `activeStreamSettings` and `phoneRumbleFallback`

---

### Step 2.1: Create state directory

```bash
mkdir -p app/src/main/java/com/opencloudgaming/opennow/ui/state
```

### Step 2.2: Create AppState.kt

**Create file:** `app/src/main/java/com/opencloudgaming/opennow/ui/state/AppState.kt`

```kotlin
package com.opencloudgaming.opennow.ui.state

data class AppState(
    val auth: AuthState = AuthState(),
    val catalog: CatalogState = CatalogState(),
    val library: LibraryState = LibraryState(),
    val settings: SettingsState = SettingsState(),
    val stream: StreamState = StreamState(),
)
```

### Step 2.3: Create AuthState.kt

**Create file:** `app/src/main/java/com/opencloudgaming/opennow/ui/state/AuthState.kt`

> **API NOTE:** Use actual model types from `Models.kt`: `AuthSession`, `LoginProvider`, `SavedAccount`, `DeviceLoginPrompt`.

```kotlin
package com.opencloudgaming.opennow.ui.state

import com.opencloudgaming.opennow.AuthSession
import com.opencloudgaming.opennow.DeviceLoginPrompt
import com.opencloudgaming.opennow.LoginProvider
import com.opencloudgaming.opennow.SavedAccount

// error = login/auth failures (wrong password, provider down, etc.)
data class AuthState(
    val session: AuthSession? = null,
    val providers: List<LoginProvider> = emptyList(),
    val selectedProvider: LoginProvider? = null,
    val savedAccounts: List<SavedAccount> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val deviceLoginPrompt: DeviceLoginPrompt? = null,
)
```

### Step 2.4: Create CatalogState.kt

**Create file:** `app/src/main/java/com/opencloudgaming/opennow/ui/state/CatalogState.kt`

> **API NOTE:** Use `GameInfo` (not `CatalogGame`) and `CatalogBrowseResult` from `Models.kt`.

```kotlin
package com.opencloudgaming.opennow.ui.state

import com.opencloudgaming.opennow.CatalogBrowseResult
import com.opencloudgaming.opennow.GameInfo

// error = catalog browse/search failures (network, API errors)
data class CatalogState(
    val games: List<GameInfo> = emptyList(),
    val catalogResult: CatalogBrowseResult = CatalogBrowseResult(emptyList()),
    val search: String = "",
    val sortId: String = "relevance",
    val filterIds: List<String> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val selectedGame: GameInfo? = null,
)
```

### Step 2.5: Create LibraryState.kt

**Create file:** `app/src/main/java/com/opencloudgaming/opennow/ui/state/LibraryState.kt`

```kotlin
package com.opencloudgaming.opennow.ui.state

import com.opencloudgaming.opennow.GameInfo

// error = library fetch/playback failures
data class LibraryState(
    val games: List<GameInfo> = emptyList(),
    val favorites: List<String> = emptyList(),
    val search: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
)
```

### Step 2.6: Create SettingsState.kt

**Create file:** `app/src/main/java/com/opencloudgaming/opennow/ui/state/SettingsState.kt`

> **API NOTE:** Use `AppSettings` and `RuntimeCodecReport` from `Models.kt`.

```kotlin
package com.opencloudgaming.opennow.ui.state

import com.opencloudgaming.opennow.AppSettings
import com.opencloudgaming.opennow.RuntimeCodecReport

data class SettingsState(
    val settings: AppSettings = AppSettings(),
    val selectedCategory: SettingsCategory = SettingsCategory.Stream,
    val codecReport: RuntimeCodecReport? = null,
)

enum class SettingsCategory(val label: String) {
    Stream("Stream"),
    Input("Input"),
    Interface("Interface"),
    Account("Account"),
    Debug("Debug"),
}
```

### Step 2.7: Create StreamState.kt

**Create file:** `app/src/main/java/com/opencloudgaming/opennow/ui/state/StreamState.kt`

> **API NOTE:** Use `SessionInfo` from `Models.kt`. Default `launchPhase` to `""` (not `null.toString()`).

```kotlin
package com.opencloudgaming.opennow.ui.state

import com.opencloudgaming.opennow.GameInfo
import com.opencloudgaming.opennow.SessionInfo

// Stream state from legacy OpenNowUiState.
// UI-only toggles (showStats, controlsVisible, touchEnabled, fingerMouseEnabled)
// are local composable state — NOT bridged from the ViewModel.
data class StreamState(
    val session: SessionInfo? = null,
    val streamGame: GameInfo? = null,
    val queuePosition: Int? = null,
    val queueAdActiveId: String? = null,
    val isMinimized: Boolean = false,
    val streamStatus: String = "idle",
    val launchPhase: String = "",
    val error: String? = null,
)
```

### Step 2.8: Modify OpenNowViewModel.kt

**Edit file:** `app/src/main/java/com/opencloudgaming/opennow/OpenNowViewModel.kt`

Add imports at top:
```kotlin
import com.opencloudgaming.opennow.ui.state.AppState
import com.opencloudgaming.opennow.ui.state.AuthState
import com.opencloudgaming.opennow.ui.state.CatalogState
import com.opencloudgaming.opennow.ui.state.LibraryState
import com.opencloudgaming.opennow.ui.state.SettingsState
import com.opencloudgaming.opennow.ui.state.SettingsCategory
import com.opencloudgaming.opennow.ui.state.StreamState
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.SharingStarted
```

Add state holders as properties (keep existing properties, add new ones):
```kotlin
// Add these new properties to OpenNowViewModel class
val authState = MutableStateFlow(AuthState())
val catalogState = MutableStateFlow(CatalogState())
val libraryState = MutableStateFlow(LibraryState())
val settingsState = MutableStateFlow(SettingsState())
val streamState = MutableStateFlow(StreamState())

val appState = combine(
    authState, catalogState, libraryState, settingsState, streamState
) { auth, catalog, library, settings, stream ->
    AppState(auth, catalog, library, settings, stream)
}.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), AppState())
```

Add ViewModel methods to replace direct state updates:
```kotlin
fun selectSettingsCategory(category: SettingsCategory) {
    settingsState.update { it.copy(selectedCategory = category) }
}
```

> **Note:** Stream UI toggles (showStats, controlsVisible, touchEnabled) are local composable state, not ViewModel-managed. No ViewModel methods needed for these.

**Bridge code (CRITICAL):** Add an `init {}` block that collects from `_state` (the existing monolithic state) and fans out to the new sub-flows. This keeps old screens working while new screens use the feature-scoped states.

```kotlin
init {
    // Bridge: fan out legacy _state into feature-scoped sub-flows.
    // This keeps old screens (OpenNowScreens.kt) working while new
    // screens read from appState instead.
    viewModelScope.launch {
        _state.collect { legacy ->
            authState.update { it.copy(
                session = legacy.authSession,
                providers = legacy.providers,
                selectedProvider = legacy.selectedProvider,
                savedAccounts = legacy.savedAccounts,
                isLoading = legacy.initializing,
                error = legacy.error,
                deviceLoginPrompt = legacy.deviceLoginPrompt,
            ) }
            catalogState.update { it.copy(
                games = legacy.games,
                catalogResult = legacy.catalogResult,
                search = legacy.catalogSearch,
                sortId = legacy.catalogSortId,
                filterIds = legacy.catalogFilterIds,
                isLoading = legacy.loadingGames,
                error = legacy.error,
                selectedGame = legacy.selectedGame,
            ) }
            libraryState.update { it.copy(
                games = legacy.libraryGames,
                favorites = legacy.settings.favoriteGameIds,
                search = legacy.librarySearch,
                isLoading = legacy.loadingGames,
                error = legacy.error,
            ) }
            settingsState.update { it.copy(
                settings = legacy.settings,
                codecReport = legacy.codecReport,
            ) }
            streamState.update { it.copy(
                session = legacy.streamSession,
                streamGame = legacy.streamGame,
                queuePosition = legacy.queuePosition,
                queueAdActiveId = legacy.queueAdActiveId,
                isMinimized = legacy.streamLaunchMinimized,
                streamStatus = legacy.streamStatus,
                launchPhase = legacy.launchPhase,
                error = legacy.error,
            ) }
        }
    }
}
```

> **NOTE:** The legacy field names above are verified against `OpenNowUiState` in `OpenNowViewModel.kt` (lines 29-68). Key mappings:
> - `legacy.authSession` (not `session`)
> - `legacy.catalogSearch` (not `search`)
> - `legacy.catalogSortId` (not `sortId`)
> - `legacy.loadingGames` (not `isLoading`)
> - `legacy.error` — single error field for all domains (auth, catalog, stream)
> - `legacy.settings.favoriteGameIds` — favorites live in AppSettings
> - `legacy.streamLaunchMinimized` (not `isMinimized`)
> - `showStats`, `controlsVisible`, `touchEnabled`, `fingerMouseEnabled` do NOT exist in legacy state — they are local UI state in composables

**Note:** Keep existing `_state` and `state` properties for now. The new `appState` will be used by new screens. Old screens continue using old state until they're rewritten.
