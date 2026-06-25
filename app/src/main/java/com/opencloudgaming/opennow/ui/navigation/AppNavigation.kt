package com.opencloudgaming.opennow.ui.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.opencloudgaming.opennow.AppPage
import com.opencloudgaming.opennow.OpenNowViewModel
import com.opencloudgaming.opennow.R
import com.opencloudgaming.opennow.StreamScreen
import com.opencloudgaming.opennow.isPhoneLandscape
import com.opencloudgaming.opennow.ui.screens.home.GameDetailsSheet
import com.opencloudgaming.opennow.ui.screens.home.HomeScreen
import com.opencloudgaming.opennow.ui.screens.library.LibraryScreen
import com.opencloudgaming.opennow.LoadingScreen
import com.opencloudgaming.opennow.ui.screens.login.LoginScreen
import com.opencloudgaming.opennow.ui.screens.settings.SettingsScreen

@Composable
fun AppNavigation(viewModel: OpenNowViewModel) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val startingText = stringResource(R.string.status_starting_opennow)
    val tvProfile = state.codecReport?.androidTvProfile == true

    when {
        state.initializing -> {
            Surface(Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                LoadingScreen(state.launchPhase.ifBlank { startingText })
            }
        }
        state.authSession == null -> {
            Surface(Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                LoginScreen(state, viewModel)
            }
        }
        state.page == AppPage.Stream -> {
            StreamScreen(state, viewModel)
        }
        else -> MainAppcaffold(state, viewModel, tvProfile)
    }
}

@Composable
private fun MainAppcaffold(
    state: com.opencloudgaming.opennow.OpenNowUiState,
    viewModel: OpenNowViewModel,
    tvProfile: Boolean,
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = when (navBackStackEntry?.destination?.route) {
        Route.Home::class.qualifiedName -> Route.Home
        Route.Library::class.qualifiedName -> Route.Library
        Route.Settings::class.qualifiedName -> Route.Settings
        Route.Login::class.qualifiedName -> Route.Login
        Route.Stream::class.qualifiedName -> Route.Stream
        else -> null
    }

    BoxWithConstraints(Modifier.fillMaxSize()) {
        val phoneLandscapeChrome = !tvProfile && isPhoneLandscape(maxWidth, maxHeight)
        val showNavigationRail = tvProfile || phoneLandscapeChrome

        Scaffold(
            contentWindowInsets = ScaffoldDefaults.contentWindowInsets,
            bottomBar = {
                if (!showNavigationRail) {
                    AppBottomBar(
                        currentRoute = currentRoute,
                        onNavigate = { route ->
                            navController.navigate(route) {
                                popUpTo(Route.Home) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                    )
                }
            },
        ) { padding ->
            Box(Modifier.fillMaxSize().padding(padding)) {
                androidx.compose.foundation.layout.Row(Modifier.fillMaxSize()) {
                    if (showNavigationRail) {
                        AppNavRail(
                            currentRoute = currentRoute,
                            onNavigate = { route ->
                                navController.navigate(route) {
                                    popUpTo(Route.Home) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                        )
                    }
                    androidx.compose.foundation.layout.Column(Modifier.fillMaxSize()) {
                        Box(Modifier.weight(1f)) {
                            NavHost(
                                navController = navController,
                                startDestination = Route.Home,
                            ) {
                                composable<Route.Home> {
                                    HomeScreen(
                                        state = state,
                                        viewModel = viewModel,
                                        tvProfile = tvProfile,
                                        hideChromeWhenScrolled = phoneLandscapeChrome,
                                        searchInTopBar = phoneLandscapeChrome,
                                        onScrollChromeHiddenChange = {},
                                    )
                                }
                                composable<Route.Library> {
                                    LibraryScreen(
                                        state = state,
                                        viewModel = viewModel,
                                        tvProfile = tvProfile,
                                        hideChromeWhenScrolled = phoneLandscapeChrome,
                                        searchInTopBar = phoneLandscapeChrome,
                                        onScrollChromeHiddenChange = {},
                                    )
                                }
                                composable<Route.Settings> {
                                    SettingsScreen(state, viewModel, tvProfile)
                                }
                                composable<Route.Stream> {
                                    StreamScreen(state, viewModel)
                                }
                            }
                        }
                    }
                }
                AnimatedVisibility(
                    visible = state.selectedGame != null && state.page != AppPage.Stream,
                    enter = fadeIn() + slideInVertically(initialOffsetY = { it / 3 }) + scaleIn(initialScale = 0.96f),
                    exit = fadeOut() + slideOutVertically(targetOffsetY = { it / 3 }) + scaleOut(targetScale = 0.96f),
                    modifier = Modifier.align(Alignment.Center),
                ) {
                    state.selectedGame?.let { game ->
                        GameDetailsSheet(
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
}
