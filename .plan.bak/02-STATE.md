# Phase 2: State Decomposition

**Goal:** Add `appState` to ViewModel. Old screens stay on old state.

**YAGNI:** No bridge layer — old screens keep using `_state`, new screens use `appState`. No fan-out code.

---

### Step 2.1: Create state directory

```bash
mkdir -p app/src/main/java/com/opencloudgaming/opennow/ui/state
```

### Step 2.2: Create state files

**Create:** `app/src/main/java/com/opencloudgaming/opennow/ui/state/AppState.kt`

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

**Create:** `app/src/main/java/com/opencloudgaming/opennow/ui/state/AuthState.kt`

```kotlin
package com.opencloudgaming.opennow.ui.state

import com.opencloudgaming.opennow.AuthSession
import com.opencloudgaming.opennow.DeviceLoginPrompt
import com.opencloudgaming.opennow.LoginProvider
import com.opencloudgaming.opennow.SavedAccount

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

**Create:** `app/src/main/java/com/opencloudgaming/opennow/ui/state/CatalogState.kt`

```kotlin
package com.opencloudgaming.opennow.ui.state

import com.opencloudgaming.opennow.CatalogBrowseResult
import com.opencloudgaming.opennow.GameInfo

data class CatalogState(
    val games: List<GameInfo> = emptyList(),
    val catalogResult: CatalogBrowseResult = CatalogBrowseResult(emptyList()),
    val search: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val selectedGame: GameInfo? = null,
)
```

**Create:** `app/src/main/java/com/opencloudgaming/opennow/ui/state/LibraryState.kt`

```kotlin
package com.opencloudgaming.opennow.ui.state

import com.opencloudgaming.opennow.GameInfo

data class LibraryState(
    val games: List<GameInfo> = emptyList(),
    val search: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
)
```

**Create:** `app/src/main/java/com/opencloudgaming/opennow/ui/state/SettingsState.kt`

```kotlin
package com.opencloudgaming.opennow.ui.state

import com.opencloudgaming.opennow.AppSettings

data class SettingsState(
    val settings: AppSettings = AppSettings(),
    val selectedCategory: SettingsCategory = SettingsCategory.Stream,
)

enum class SettingsCategory(val label: String) {
    Stream("Stream"),
    Input("Input"),
    Interface("Interface"),
    Account("Account"),
    Debug("Debug"),
}
```

**Create:** `app/src/main/java/com/opencloudgaming/opennow/ui/state/StreamState.kt`

```kotlin
package com.opencloudgaming.opennow.ui.state

import com.opencloudgaming.opennow.GameInfo
import com.opencloudgaming.opennow.SessionInfo

data class StreamState(
    val session: SessionInfo? = null,
    val streamGame: GameInfo? = null,
    val queuePosition: Int? = null,
    val isMinimized: Boolean = false,
    val streamStatus: String = "idle",
    val launchPhase: String = "",
    val error: String? = null,
)
```

### Step 2.3: Modify OpenNowViewModel.kt

**Edit:** `app/src/main/java/com/opencloudgaming/opennow/OpenNowViewModel.kt`

Add imports:
```kotlin
import com.opencloudgaming.opennow.ui.state.*
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.SharingStarted
```

Add properties (keep existing `_state` and `state`):
```kotlin
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

Add methods:
```kotlin
fun selectSettingsCategory(category: SettingsCategory) {
    settingsState.update { it.copy(selectedCategory = category) }
}

fun clearCache() {
    catalogState.update { CatalogState() }
    libraryState.update { LibraryState() }
}
```

> **Note:** Keep existing `_state` for old screens. New screens use `appState`. No bridge code needed.
