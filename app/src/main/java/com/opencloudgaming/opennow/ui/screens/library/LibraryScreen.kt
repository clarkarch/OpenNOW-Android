package com.opencloudgaming.opennow.ui.screens.library

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.opencloudgaming.opennow.ActiveSessionInfo
import com.opencloudgaming.opennow.GameInfo
import com.opencloudgaming.opennow.OpenNowUiState
import com.opencloudgaming.opennow.OpenNowViewModel
import com.opencloudgaming.opennow.R
import com.opencloudgaming.opennow.NativeSearchField
import com.opencloudgaming.opennow.ui.components.UrlImage
import com.opencloudgaming.opennow.ui.screens.home.GameGrid
import com.opencloudgaming.opennow.ui.screens.home.SearchEmptyState
import com.opencloudgaming.opennow.ui.screens.home.SwipeToRefreshContainer
import com.opencloudgaming.opennow.ui.screens.home.gameMatchesSearch
import com.opencloudgaming.opennow.ui.theme.PanelAlt
import com.opencloudgaming.opennow.ui.theme.TextMuted

@Composable
internal fun LibraryScreen(
    state: OpenNowUiState,
    viewModel: OpenNowViewModel,
    tvProfile: Boolean,
    hideChromeWhenScrolled: Boolean,
    searchInTopBar: Boolean,
    onScrollChromeHiddenChange: (Boolean) -> Unit,
) {
    val favorites = state.libraryGames.filter { it.id in state.settings.favoriteGameIds }
    val orderedGames = if (favorites.isNotEmpty()) favorites + state.libraryGames.filterNot { it.id in state.settings.favoriteGameIds } else state.libraryGames
    val games = orderedGames.filter { gameMatchesSearch(it, state.librarySearch) }
    val gridState = rememberLazyGridState()
    val scrolledAwayFromTop = gridState.firstVisibleItemIndex > 0 || gridState.firstVisibleItemScrollOffset > 0
    val hideScrollChrome = hideChromeWhenScrolled && scrolledAwayFromTop
    LaunchedEffect(hideScrollChrome) {
        onScrollChromeHiddenChange(hideScrollChrome)
    }
    DisposableEffect(Unit) {
        onDispose { onScrollChromeHiddenChange(false) }
    }
    SwipeToRefreshContainer(
        refreshing = state.loadingGames,
        onRefresh = viewModel::refreshGames,
        modifier = Modifier.fillMaxSize(),
    ) {
        BoxWithConstraints(Modifier.fillMaxSize()) {
            Column(Modifier.fillMaxSize().padding(12.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(Modifier.weight(1f)) {
                        Text(stringResource(R.string.nav_library), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                        Text(stringResource(R.string.library_count, state.libraryGames.size), color = TextMuted)
                    }
                    if (state.activeSession != null) {
                        ElevatedButton(
                            onClick = viewModel::resumeActiveSession,
                        ) { Text(stringResource(R.string.action_resume)) }
                    }
                }
                ActiveSessionResumeCard(
                    state = state,
                    onResumeActiveSession = viewModel::resumeActiveSession,
                )
                AnimatedVisibility(visible = !searchInTopBar && !hideScrollChrome) {
                    NativeSearchField(
                        modifier = Modifier.fillMaxWidth(),
                        query = state.librarySearch,
                        onQueryChange = viewModel::setLibrarySearch,
                        placeholder = "Search library",
                    )
                }
                GameGrid(
                    games,
                    state.settings.favoriteGameIds,
                    state.settings,
                    tvProfile,
                    viewModel::selectGame,
                    viewModel::updateFavorites,
                    viewModel::play,
                    viewModel::chooseStore,
                    modifier = Modifier.weight(1f),
                    gridState = gridState,
                    emptyContent = {
                        if (state.librarySearch.isNotBlank() && state.libraryGames.isNotEmpty()) {
                            SearchEmptyState(
                                title = stringResource(R.string.library_empty_search_title),
                                message = stringResource(R.string.library_empty_search_body),
                                onClearSearch = { viewModel.setLibrarySearch("") },
                            )
                        } else {
                            Text(stringResource(R.string.no_games_loaded), color = TextMuted)
                        }
                    },
                )
            }
        }
    }
}

@Composable
internal fun ActiveSessionResumeCard(
    state: OpenNowUiState,
    onResumeActiveSession: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val active = state.activeSession ?: return
    val game = activeSessionGame(state, active)
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = PanelAlt.copy(alpha = 0.92f),
        tonalElevation = 3.dp,
    ) {
        Row(
            Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            UrlImage(
                game?.imageUrl,
                Modifier
                    .width(44.dp)
                    .height(58.dp)
                    .clip(RoundedCornerShape(10.dp)),
            )
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text("Resume cloud session", fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text(
                    game?.title ?: "App ${active.appId}",
                    color = TextMuted,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    activeSessionSummary(active),
                    color = TextMuted,
                    style = MaterialTheme.typography.labelSmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            Button(onClick = onResumeActiveSession, contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp)) {
                Text(stringResource(R.string.action_resume), maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
        }
    }
}

internal fun activeSessionGame(state: OpenNowUiState, active: ActiveSessionInfo): GameInfo? =
    (state.games + state.libraryGames).firstOrNull { game ->
        game.launchAppId == active.appId.toString() ||
            game.variants.any { variant -> variant.id == active.appId.toString() }
    }

internal fun activeSessionSummary(active: ActiveSessionInfo): String =
    listOfNotNull(
        when (active.status) {
            1 -> active.queuePosition?.takeIf { it > 0 }?.let { "Queue $it" } ?: "Starting"
            2, 3 -> "Ready"
            else -> "Active"
        },
        active.resolution,
        active.fps?.let { "${it} FPS" },
        active.gpuType,
        active.sessionId.take(8).takeIf { it.isNotBlank() }?.let { "Session $it" },
    ).joinToString(" - ")
