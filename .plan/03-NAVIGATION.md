# Phase 3: Navigation

**Goal:** Replace manual page switching with Navigation Compose.

**Skills to read first:** `compose-focus-navigation/SKILL.md`, `material-3/references/navigation-patterns.md`, `material-3/references/layout-and-responsive.md`

**MCP verification:** Use `android-docs_search_android_docs` and `android-docs_get_api_reference` to verify `NavHost`, `composable()`, `rememberNavController`, and `WindowSizeClass` APIs BEFORE writing code.

**INTEGRATION CHECK (MANDATORY before committing):**
1. Every `Route` sealed interface object MUST have a `composable<Route.Xxx>` destination in NavHost
2. Every `navController.navigate(Route.Xxx)` MUST reference a valid route
3. Every screen composable called from AppNavigation MUST have ALL parameters provided
4. Every `viewModel.xxx()` call MUST exist in OpenNowViewModel
5. AdaptiveScaffold MUST be invoked (not just defined)
6. ALL callback parameters MUST be wired (no empty `onClick = {}`)

---

### Step 3.1: Add dependencies

**Edit file:** `app/build.gradle.kts`

Add to dependencies block:
```kotlin
// Navigation Compose (type-safe) — NOT in Compose BOM, must add explicitly
implementation("androidx.navigation:navigation-compose:2.9.8")

// Coil for image loading — group ID changed in 3.x
implementation("io.coil-kt.coil3:coil-compose:3.5.0")
implementation("io.coil-kt.coil3:coil-network-okhttp:3.5.0")

// Window size class for adaptive layout — NOT in Compose BOM, must add explicitly
implementation("androidx.compose.material3:material3-window-size-class:1.4.0")
```

**Note:** `kotlinx-serialization-json` is already in the project.

### Step 3.2: Create navigation directory

```bash
mkdir -p app/src/main/java/com/opencloudgaming/opennow/ui/navigation
mkdir -p app/src/main/java/com/opencloudgaming/opennow/ui/navigation/adaptive
```

### Step 3.3: Create Routes.kt

**Create file:** `app/src/main/java/com/opencloudgaming/opennow/ui/navigation/Routes.kt`

```kotlin
package com.opencloudgaming.opennow.ui.navigation

import kotlinx.serialization.Serializable

sealed interface Route {
    @Serializable data object Login : Route
    @Serializable data object TvDeviceLogin : Route
    @Serializable data object Home : Route
    @Serializable data object Library : Route
    @Serializable data object Settings : Route
    @Serializable data object Stream : Route
}
```

### Step 3.4: Create PhoneNavigationBar.kt

**Create file:** `app/src/main/java/com/opencloudgaming/opennow/ui/navigation/PhoneNavigationBar.kt`

> **API NOTE:** Do NOT use `Icons.Default.Store` or `Icons.Default.LibraryBooks` — they require `material-icons-extended`. Use text labels instead.

```kotlin
package com.opencloudgaming.opennow.ui.navigation

import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.opencloudgaming.opennow.ui.theme.Primary
import com.opencloudgaming.opennow.ui.theme.OnSurfaceVariant
import com.opencloudgaming.opennow.ui.theme.Surface

data class NavItem(val route: Route, val label: String)

val navItems = listOf(
    NavItem(Route.Home, "Store"),
    NavItem(Route.Library, "Library"),
    NavItem(Route.Settings, "Settings"),
)

@Composable
fun PhoneNavigationBar(
    currentRoute: Route?,
    onNavigate: (Route) -> Unit,
) {
    NavigationBar(containerColor = Surface) {
        navItems.forEach { item ->
            NavigationBarItem(
                icon = { Text(item.label.take(1)) },
                label = { Text(item.label) },
                selected = currentRoute == item.route,
                onClick = { onNavigate(item.route) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Primary,
                    selectedTextColor = Primary,
                    unselectedIconColor = OnSurfaceVariant,
                    unselectedTextColor = OnSurfaceVariant,
                    indicatorColor = Primary.copy(alpha = 0.12f),
                ),
            )
        }
    }
}
```

### Step 3.5: Create TabletNavigationRail.kt

**Create file:** `app/src/main/java/com/opencloudgaming/opennow/ui/navigation/TabletNavigationRail.kt`

```kotlin
package com.opencloudgaming.opennow.ui.navigation

import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.opencloudgaming.opennow.ui.theme.Primary
import com.opencloudgaming.opennow.ui.theme.OnSurfaceVariant
import com.opencloudgaming.opennow.ui.theme.Surface

@Composable
fun TabletNavigationRail(
    currentRoute: Route?,
    onNavigate: (Route) -> Unit,
) {
    NavigationRail(containerColor = Surface) {
        navItems.forEach { item ->
            NavigationRailItem(
                icon = { Text(item.label.take(1)) },
                label = { Text(item.label) },
                selected = currentRoute == item.route,
                onClick = { onNavigate(item.route) },
                colors = NavigationRailItemDefaults.colors(
                    selectedIconColor = Primary,
                    selectedTextColor = Primary,
                    unselectedIconColor = OnSurfaceVariant,
                    unselectedTextColor = OnSurfaceVariant,
                    indicatorColor = Primary.copy(alpha = 0.12f),
                ),
            )
        }
    }
}
```

### Step 3.6: Create AdaptiveScaffold.kt

**Create file:** `app/src/main/java/com/opencloudgaming/opennow/ui/navigation/adaptive/AdaptiveScaffold.kt`

> **API NOTE:** `LocalActivity.current` returns `Activity?` (nullable). Must handle nullability.

```kotlin
package com.opencloudgaming.opennow.ui.navigation.adaptive

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import android.app.Activity
import android.content.res.Configuration
import androidx.activity.compose.LocalActivity
import com.opencloudgaming.opennow.ui.navigation.PhoneNavigationBar
import com.opencloudgaming.opennow.ui.navigation.TabletNavigationRail
import com.opencloudgaming.opennow.ui.navigation.Route
import com.opencloudgaming.opennow.ui.screens.home.HomeScreen
import com.opencloudgaming.opennow.ui.screens.library.LibraryScreen
import com.opencloudgaming.opennow.ui.screens.login.LoginScreen
import com.opencloudgaming.opennow.ui.screens.settings.SettingsScreen
import com.opencloudgaming.opennow.ui.screens.stream.StreamScreen

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun AdaptiveScaffold(
    currentRoute: Route?,
    onNavigate: (Route) -> Unit,
    content: @Composable () -> Unit,
) {
    val activity = LocalActivity.current as? Activity ?: return
    val windowSizeClass = calculateWindowSizeClass(activity)
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    val isExpanded = windowSizeClass.widthSizeClass == WindowWidthSizeClass.Expanded

    if (isExpanded || isLandscape) {
        Row(modifier = Modifier.fillMaxSize()) {
            TabletNavigationRail(currentRoute = currentRoute, onNavigate = onNavigate)
            content()
        }
    } else {
        Column(modifier = Modifier.fillMaxSize()) {
            content()
            PhoneNavigationBar(currentRoute = currentRoute, onNavigate = onNavigate)
        }
    }
}
```

### Step 3.7: Create AppNavigation.kt

**Create file:** `app/src/main/java/com/opencloudgaming/opennow/ui/navigation/AppNavigation.kt`

> **API NOTE:** Must import `kotlinx.coroutines.flow.update` for `MutableStateFlow.update {}`.

```kotlin
package com.opencloudgaming.opennow.ui.navigation

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.opencloudgaming.opennow.OpenNowViewModel
import com.opencloudgaming.opennow.ui.screens.home.HomeScreen
import com.opencloudgaming.opennow.ui.screens.library.LibraryScreen
import com.opencloudgaming.opennow.ui.screens.login.LoginScreen
import com.opencloudgaming.opennow.ui.screens.settings.SettingsScreen
import com.opencloudgaming.opennow.ui.screens.stream.StreamScreen

@Composable
fun AppNavigation(
    viewModel: OpenNowViewModel,
    navController: NavHostController,
) {
    val appState by viewModel.appState.collectAsStateWithLifecycle()

    NavHost(
        navController = navController,
        startDestination = if (appState.auth.session != null) Route.Home else Route.Login,
        enterTransition = { slideInHorizontally(initialOffsetX = { it }) },
        exitTransition = { slideOutHorizontally(targetOffsetX = { -it / 3 }) },
        popEnterTransition = { slideInHorizontally(initialOffsetX = { -it / 3 }) },
        popExitTransition = { slideOutHorizontally(targetOffsetX = { it }) },
    ) {
        composable<Route.Login> {
            LoginScreen(
                providers = appState.auth.providers,
                selectedProvider = appState.auth.selectedProvider,
                isLoading = appState.auth.isLoading,
                error = appState.auth.error,
                onProviderSelected = { viewModel.selectProvider(it) },
                onLogin = { viewModel.login() },
                onTvLogin = { navController.navigate(Route.TvDeviceLogin) },
            )
        }
        composable<Route.Home> {
            val lastPlayedGame = appState.catalog.games.maxByOrNull { it.lastPlayed.orEmpty() }
            HomeScreen(
                games = appState.catalog.games,
                lastPlayedGame = lastPlayedGame,
                search = appState.catalog.search,
                isLoading = appState.catalog.isLoading,
                onSearchChange = { viewModel.setCatalogSearch(it) },
                onGameClick = { viewModel.selectGame(it) },
                onPlayGame = { viewModel.play(it) },
                onRefresh = { viewModel.refreshGames() },
            )
        }
        composable<Route.Library> {
            LibraryScreen(
                games = appState.library.games,
                search = appState.library.search,
                isLoading = appState.library.isLoading,
                onSearchChange = { viewModel.setLibrarySearch(it) },
                onGameClick = { viewModel.selectGame(it) },
            )
        }
        composable<Route.Settings> {
            SettingsScreen(
                selectedCategory = appState.settings.selectedCategory,
                onCategorySelected = { cat -> viewModel.selectSettingsCategory(cat) },
            )
        }
        composable<Route.Stream> {
            StreamScreen(
                streamState = appState.stream,
                onDisconnect = { viewModel.stopStream() },
            )
        }
    }
}
```

### Step 3.8: Create stub screens (Phase 4 replaces these)

> **These stubs exist so Phase 3 compiles. Phase 4 replaces them with real implementations.**

**Create file:** `app/src/main/java/com/opencloudgaming/opennow/ui/screens/login/LoginScreen.kt`

```kotlin
package com.opencloudgaming.opennow.ui.screens.login

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun LoginScreen(
    providers: List<com.opencloudgaming.opennow.LoginProvider>,
    selectedProvider: com.opencloudgaming.opennow.LoginProvider?,
    isLoading: Boolean,
    error: String?,
    onProviderSelected: (com.opencloudgaming.opennow.LoginProvider) -> Unit,
    onLogin: () -> Unit,
    onTvLogin: () -> Unit,
) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Login — stub")
    }
}
```

**Create file:** `app/src/main/java/com/opencloudgaming/opennow/ui/screens/home/HomeScreen.kt`

```kotlin
package com.opencloudgaming.opennow.ui.screens.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.opencloudgaming.opennow.GameInfo

@Composable
fun HomeScreen(
    games: List<GameInfo>,
    lastPlayedGame: GameInfo?,
    search: String,
    isLoading: Boolean,
    onSearchChange: (String) -> Unit,
    onGameClick: (GameInfo) -> Unit,
    onPlayGame: (GameInfo) -> Unit,
    onRefresh: () -> Unit,
) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Home — stub")
    }
}
```

**Create file:** `app/src/main/java/com/opencloudgaming/opennow/ui/screens/library/LibraryScreen.kt`

```kotlin
package com.opencloudgaming.opennow.ui.screens.library

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.opencloudgaming.opennow.GameInfo

@Composable
fun LibraryScreen(
    games: List<GameInfo>,
    search: String,
    isLoading: Boolean,
    onSearchChange: (String) -> Unit,
    onGameClick: (GameInfo) -> Unit,
) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Library — stub")
    }
}
```

**Create file:** `app/src/main/java/com/opencloudgaming/opennow/ui/screens/settings/SettingsScreen.kt`

```kotlin
package com.opencloudgaming.opennow.ui.screens.settings

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.opencloudgaming.opennow.ui.state.SettingsCategory

@Composable
fun SettingsScreen(
    settings: com.opencloudgaming.opennow.AppSettings,
    selectedCategory: SettingsCategory,
    onCategorySelected: (SettingsCategory) -> Unit,
    onUpdateSettings: (com.opencloudgaming.opennow.AppSettings) -> Unit,
    onClearCache: () -> Unit,
    onResetSettings: () -> Unit,
    onLogout: () -> Unit,
    onLogoutAll: () -> Unit,
    onSwitchAccount: (String) -> Unit,
) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Settings — stub")
    }
}
```

**Create file:** `app/src/main/java/com/opencloudgaming/opennow/ui/screens/stream/StreamScreen.kt`

```kotlin
package com.opencloudgaming.opennow.ui.screens.stream

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.opencloudgaming.opennow.ui.state.StreamState

@Composable
fun StreamScreen(
    streamState: StreamState,
    onDisconnect: () -> Unit,
) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Stream — stub")
    }
}
```

### Step 3.9: Modify MainActivity.kt

**Edit file:** `app/src/main/java/com/opencloudgaming/opennow/MainActivity.kt`

Replace the existing `setContent` block. Find:
```kotlin
setContent {
    OpenNowApp(viewModel)
}
```

Replace with:
```kotlin
setContent {
    OpenNowTheme {
        val navController = rememberNavController()
        AppNavigation(
            viewModel = viewModel,
            navController = navController,
        )
    }
}
```

Add required imports:
```kotlin
import androidx.navigation.compose.rememberNavController
import com.opencloudgaming.opennow.ui.theme.OpenNowTheme
import com.opencloudgaming.opennow.ui.navigation.AppNavigation
```
