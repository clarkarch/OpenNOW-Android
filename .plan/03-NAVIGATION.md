# Phase 3: Navigation

**Goal:** Add Navigation Compose with type-safe routes, bottom bar (phone), NavigationRail (tablet). Replace manual `state.page` switching.

**MCP:** Before writing, verify:
```
android-docs_search_android_docs("navigation compose type safe routes serializable")
android-docs_get_api_reference("androidx.navigation.compose.NavHost")
android-docs_get_api_reference("androidx.navigation.NavHostController")
android-docs_search_android_docs("material3 NavigationBar")
android-docs_search_android_docs("material3 NavigationRail")
```
**YAGNI:** No animation transitions. No deep link handling. No nested navigation graphs.
**KISS:** Simple NavHost with 4 routes. Bottom bar calls `navController.navigate`. No custom transition lambdas.
**DO NOT RUN:** `./gradlew` or any build command.

---

### Step 3.1: Create directory

```bash
mkdir -p app/src/main/java/com/opencloudgaming/opennow/ui/navigation
```

### Step 3.2: Create Routes.kt

**Create:** `app/src/main/java/com/opencloudgaming/opennow/ui/navigation/Routes.kt`

```kotlin
package com.opencloudgaming.opennow.ui.navigation

import kotlinx.serialization.Serializable

sealed interface Route {
    @Serializable data object Login : Route
    @Serializable data object Home : Route
    @Serializable data object Library : Route
    @Serializable data object Settings : Route
    @Serializable data object Stream : Route
}
```

> **NOTE:** No TvDeviceLogin route — that screen is part of LoginScreen's internal state. No Queue route — queue is shown inside StreamScreen.

### Step 3.3: Create NavBar.kt

**Create:** `app/src/main/java/com/opencloudgaming/opennow/ui/navigation/NavBar.kt`

```kotlin
package com.opencloudgaming.opennow.ui.navigation

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.opencloudgaming.opennow.R

data class NavItem(val route: Route, val label: String, val iconRes: Int)

val navItems = listOf(
    NavItem(Route.Home, "Store", R.drawable.ic_tab_store),
    NavItem(Route.Library, "Library", R.drawable.ic_tab_library),
    NavItem(Route.Settings, "Settings", R.drawable.ic_tab_settings),
)

@Composable
fun AppBottomBar(
    currentRoute: Route?,
    onNavigate: (Route) -> Unit,
) {
    NavigationBar {
        navItems.forEach { item ->
            NavigationBarItem(
                icon = { Icon(painterResource(item.iconRes), contentDescription = item.label) },
                label = { Text(item.label) },
                selected = currentRoute == item.route,
                onClick = { onNavigate(item.route) },
            )
        }
    }
}

@Composable
fun AppNavRail(
    currentRoute: Route?,
    onNavigate: (Route) -> Unit,
) {
    NavigationRail {
        Spacer(Modifier.height(8.dp))
        navItems.forEach { item ->
            NavigationRailItem(
                icon = { Icon(painterResource(item.iconRes), contentDescription = item.label) },
                label = { Text(item.label) },
                selected = currentRoute == item.route,
                onClick = { onNavigate(item.route) },
            )
        }
    }
}
```

### Step 3.4: Create AppNavigation.kt

**Create:** `app/src/main/java/com/opencloudgaming/opennow/ui/navigation/AppNavigation.kt`

```kotlin
package com.opencloudgaming.opennow.ui.navigation

import android.app.Activity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.opencloudgaming.opennow.OpenNowViewModel
import com.opencloudgaming.opennow.ui.screens.home.HomeScreen
import com.opencloudgaming.opennow.ui.screens.library.LibraryScreen
import com.opencloudgaming.opennow.ui.screens.login.LoginScreen
import com.opencloudgaming.opennow.ui.screens.settings.SettingsScreen
import com.opencloudgaming.opennow.ui.screens.stream.StreamScreen
import com.opencloudgaming.opennow.ui.theme.PhoneNavRailMaxSmallestWidth
import com.opencloudgaming.opennow.ui.screens.login.LoadingScreen

@Composable
fun AppNavigation(viewModel: OpenNowViewModel) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val navController = rememberNavController()
    val tvProfile = state.codecReport?.androidTvProfile == true

    if (state.initializing) {
        LoadingScreen(state.launchPhase.ifBlank { "Starting OpenNOW" })
        return
    }

    if (state.authSession == null) {
        LoginScreen(state = state, viewModel = viewModel)
        return
    }

    // Determine nav mode
    val context = LocalContext.current
    val metrics = context.resources.displayMetrics
    val smallestWidthDp = minOf(metrics.widthPixels, metrics.heightPixels) / metrics.density
    val useNavRail = tvProfile || smallestWidthDp > PhoneNavRailMaxSmallestWidth

    Scaffold(
        contentWindowInsets = ScaffoldDefaults.contentWindowInsets,
        bottomBar = {
            if (!useNavRail) {
                val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
                val selected = when {
                    currentRoute?.contains("Home") == true -> Route.Home
                    currentRoute?.contains("Library") == true -> Route.Library
                    currentRoute?.contains("Settings") == true -> Route.Settings
                    else -> null
                }
                AppBottomBar(currentRoute = selected) { route ->
                    navigateToRoute(navController, route)
                }
            }
        },
    ) { padding ->
        Row(Modifier.fillMaxSize().padding(padding)) {
            if (useNavRail) {
                val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
                val selected = when {
                    currentRoute?.contains("Home") == true -> Route.Home
                    currentRoute?.contains("Library") == true -> Route.Library
                    currentRoute?.contains("Settings") == true -> Route.Settings
                    else -> null
                }
                AppNavRail(currentRoute = selected) { route ->
                    navigateToRoute(navController, route)
                }
            }

            NavHost(
                navController = navController,
                startDestination = Route.Home,
                modifier = Modifier.weight(1f),
            ) {
                composable<Route.Home> {
                    HomeScreen(
                        state = state,
                        viewModel = viewModel,
                        tvProfile = tvProfile,
                        hideChromeWhenScrolled = false,
                        searchInTopBar = false,
                        onScrollChromeHiddenChange = {},
                    )
                }
                composable<Route.Library> {
                    LibraryScreen(
                        state = state,
                        viewModel = viewModel,
                        tvProfile = tvProfile,
                        hideChromeWhenScrolled = false,
                        searchInTopBar = false,
                        onScrollChromeHiddenChange = {},
                    )
                }
                composable<Route.Settings> {
                    SettingsScreen(state = state, viewModel = viewModel, tvProfile = tvProfile)
                }
                composable<Route.Stream> {
                    StreamScreen(state = state, viewModel = viewModel)
                }
            }

            // Game details overlay — outside NavHost, always available
            AnimatedVisibility(
                visible = state.selectedGame != null && state.page != com.opencloudgaming.opennow.AppPage.Stream,
                enter = fadeIn() + slideInVertically(initialOffsetY = { it / 3 }) + scaleIn(initialScale = 0.96f),
                exit = fadeOut() + slideOutVertically(targetOffsetY = { it / 3 }) + scaleOut(targetScale = 0.96f),
            ) {
                state.selectedGame?.let { game ->
                    com.opencloudgaming.opennow.ui.screens.home.GameDetailsSheet(
                        game = game,
                        favorite = game.id in state.settings.favoriteGameIds,
                        defaultVariantId = state.settings.defaultGameVariantIds[game.id],
                        onPlay = viewModel::play,
                        onChooseStore = viewModel::chooseStore,
                        onFavorite = viewModel::updateFavorites,
                        onDismiss = viewModel::clearSelectedGame,
                    )
                }
            }
        }
    }
}

private fun navigateToRoute(navController: NavHostController, route: Route) {
    navController.navigate(route) {
        popUpTo(Route.Home) { saveState = true }
        launchSingleTop = true
        restoreState = true
    }
}
```

> **IMPORTANT:** The GameDetailsSheet, PrintedWasteSelector, StoreLaunchSelector, and MinimizedQueuePill stay in the orchestration layer (either here or in a wrapper composable). They are overlays that exist outside of route navigation.

### Step 3.5: Update OpenNowViewModel.kt

**Edit:** `app/src/main/java/com/opencloudgaming/opennow/OpenNowViewModel.kt`

The `setPage` method is still needed for internal navigation (stream launch, back navigation). No changes needed to ViewModel for this phase.

### Step 3.6: Update MainActivity.kt

**Edit:** `app/src/main/java/com/opencloudgaming/opennow/MainActivity.kt`

Replace:
```kotlin
import com.opencloudgaming.opennow.OpenNowApp
```
With:
```kotlin
import com.opencloudgaming.opennow.ui.navigation.AppNavigation
import com.opencloudgaming.opennow.ui.theme.OpenNowTheme
```

Replace `setContent` block:
```kotlin
setContent {
    OpenNowTheme(viewModel.state.value.settings) {
        AppNavigation(viewModel = viewModel)
    }
}
```

> **NOTE:** `viewModel.state.value.settings` reads the current settings for the theme. This is fine because theme is applied at the top level and recomposes when settings change via `collectAsStateWithLifecycle` inside `AppNavigation`.

### Step 3.7: Verify

```bash
# 1. Routes exist
grep -rn "Route\." app/src/main/java/com/opencloudgaming/opennow/ui/navigation/ | head -10

# 2. NavHost has all destinations
grep -n "composable<" app/src/main/java/com/opencloudgaming/opennow/ui/navigation/AppNavigation.kt

# 3. MainActivity uses AppNavigation
grep -n "AppNavigation" app/src/main/java/com/opencloudgaming/opennow/MainActivity.kt
```
