# Phase 3: Navigation

**Goal:** Replace manual page switching with Navigation Compose.

**YAGNI:** No adaptive scaffold — phone-only. No tablet rail. No stub screens — Phase 4 creates real ones.

---

### Step 3.1: Add dependencies

**Edit:** `app/build.gradle.kts`

Add to dependencies block:
```kotlin
implementation("androidx.navigation:navigation-compose:2.9.8")
implementation("io.coil-kt.coil3:coil-compose:3.5.0")
implementation("io.coil-kt.coil3:coil-network-okhttp:3.5.0")
```

### Step 3.2: Create navigation directory

```bash
mkdir -p app/src/main/java/com/opencloudgaming/opennow/ui/navigation
```

### Step 3.3: Create Routes.kt

**Create:** `app/src/main/java/com/opencloudgaming/opennow/ui/navigation/Routes.kt`

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

### Step 3.4: Create AppNavigation.kt

**Create:** `app/src/main/java/com/opencloudgaming/opennow/ui/navigation/AppNavigation.kt`

```kotlin
package com.opencloudgaming.opennow.ui.navigation

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.opencloudgaming.opennow.OpenNowViewModel
import com.opencloudgaming.opennow.ui.screens.home.HomeScreen
import com.opencloudgaming.opennow.ui.screens.library.LibraryScreen
import com.opencloudgaming.opennow.ui.screens.login.LoginScreen
import com.opencloudgaming.opennow.ui.screens.settings.SettingsScreen
import com.opencloudgaming.opennow.ui.screens.login.TvDeviceLoginScreen
import com.opencloudgaming.opennow.ui.screens.stream.StreamScreen

@Composable
fun AppNavigation(
    viewModel: OpenNowViewModel,
    navController: NavHostController,
) {
    val appState by viewModel.appState.collectAsStateWithLifecycle()
    val currentRoute = navController.currentBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            if (currentRoute in listOf("Route.Home", "Route.Library", "Route.Settings")) {
                NavigationBar {
                    navItems.forEach { item ->
                        NavigationBarItem(
                            icon = { Text(item.label.take(1)) },
                            label = { Text(item.label) },
                            selected = currentRoute == item.route,
                            onClick = {
                                if (currentRoute != item.route) {
                                    navController.navigate(item.route) {
                                        popUpTo(Route.Home) { saveState = true }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            },
                        )
                    }
                }
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = if (appState.auth.session != null) Route.Home else Route.Login,
            modifier = Modifier.padding(padding),
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
            composable<Route.TvDeviceLogin> {
                TvDeviceLoginScreen(onBack = { navController.popBackStack() })
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
                    settings = appState.settings.settings,
                    selectedCategory = appState.settings.selectedCategory,
                    onCategorySelected = { viewModel.selectSettingsCategory(it) },
                    onUpdateSettings = { viewModel.updateSettings(it) },
                    onClearCache = { viewModel.clearCache() },
                    onResetSettings = { viewModel.resetSettings() },
                    onLogout = { viewModel.logout() },
                    onLogoutAll = { viewModel.logoutAll() },
                    onSwitchAccount = { viewModel.switchAccount(it) },
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
}

private data class NavItem(val route: Any, val label: String)

private val navItems = listOf(
    NavItem(Route.Home, "Store"),
    NavItem(Route.Library, "Library"),
    NavItem(Route.Settings, "Settings"),
)
```

### Step 3.5: Modify MainActivity.kt

**Edit:** `app/src/main/java/com/opencloudgaming/opennow/MainActivity.kt`

Replace `setContent` block:
```kotlin
setContent {
    OpenNowTheme {
        val navController = rememberNavController()
        AppNavigation(viewModel = viewModel, navController = navController)
    }
}
```

Add imports:
```kotlin
import androidx.navigation.compose.rememberNavController
import com.opencloudgaming.opennow.ui.theme.OpenNowTheme
import com.opencloudgaming.opennow.ui.navigation.AppNavigation
```
