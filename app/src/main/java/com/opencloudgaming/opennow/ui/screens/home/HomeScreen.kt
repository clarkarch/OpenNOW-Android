package com.opencloudgaming.opennow.ui.screens.home

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items as gridItems
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableTransitionState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.opencloudgaming.opennow.AppSettings
import com.opencloudgaming.opennow.CatalogFilterGroup
import com.opencloudgaming.opennow.CatalogFilterOption
import com.opencloudgaming.opennow.GameInfo
import com.opencloudgaming.opennow.GameVariant
import com.opencloudgaming.opennow.OpenNowUiState
import com.opencloudgaming.opennow.OpenNowViewModel
import com.opencloudgaming.opennow.R
import com.opencloudgaming.opennow.ActiveSessionResumeCard
import com.opencloudgaming.opennow.NativeSearchField
import com.opencloudgaming.opennow.displayStoresForVariants
import com.opencloudgaming.opennow.gameStoreDisplayName
import com.opencloudgaming.opennow.isPhoneLandscape
import com.opencloudgaming.opennow.isPhonePortrait
import com.opencloudgaming.opennow.isTvActivateKey
import com.opencloudgaming.opennow.launchableGameVariants
import com.opencloudgaming.opennow.normalizeGameStore
import com.opencloudgaming.opennow.splitGameStoreKeys
import com.opencloudgaming.opennow.ui.components.UrlImage
import com.opencloudgaming.opennow.ui.theme.Green
import com.opencloudgaming.opennow.ui.theme.Panel
import com.opencloudgaming.opennow.ui.theme.PanelAlt
import com.opencloudgaming.opennow.ui.theme.PhoneNavRailMaxSmallestWidth
import com.opencloudgaming.opennow.ui.theme.TextMuted
import com.opencloudgaming.opennow.ui.theme.TextPrimary
import kotlinx.coroutines.launch
import java.util.Locale
import kotlin.math.min

internal data class LauncherBadge(
    val iconRes: Int,
    val name: String,
    val background: Color,
    val foreground: Color = TextPrimary,
)

@OptIn(ExperimentalComposeUiApi::class)
@Composable
internal fun HomeScreen(
    state: OpenNowUiState,
    viewModel: OpenNowViewModel,
    tvProfile: Boolean,
    hideChromeWhenScrolled: Boolean,
    searchInTopBar: Boolean,
    onScrollChromeHiddenChange: (Boolean) -> Unit,
) {
    val visibleGames = state.games.ifEmpty { state.catalogResult.games }
    val searchingCatalog = state.loadingGames && state.catalogSearch.isNotBlank()
    val gridState = rememberLazyGridState()
    val searchFocusRequester = remember { FocusRequester() }
    val scope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val showScrollActions = gridState.firstVisibleItemIndex > 0 || gridState.firstVisibleItemScrollOffset > 80
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
        showRefreshIndicator = !searchingCatalog,
        onRefresh = viewModel::refreshGames,
        modifier = Modifier.fillMaxSize(),
    ) {
        BoxWithConstraints(Modifier.fillMaxSize()) {
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                AnimatedVisibility(visible = !searchInTopBar && !hideScrollChrome) {
                    NativeSearchField(
                        modifier = Modifier.fillMaxWidth(),
                        query = state.catalogSearch,
                        onQueryChange = viewModel::setCatalogSearch,
                        placeholder = stringResource(R.string.search_games),
                        searching = searchingCatalog,
                        focusRequester = searchFocusRequester,
                        onOpen = {
                            if (gridState.firstVisibleItemIndex > 0 || gridState.firstVisibleItemScrollOffset > 0) {
                                scope.launch { gridState.animateScrollToItem(0) }
                            }
                        },
                    )
                }
                ActiveSessionResumeCard(
                    state = state,
                    onResumeActiveSession = viewModel::resumeActiveSession,
                )
                Box(
                    Modifier
                        .weight(1f)
                        .pointerInput(Unit) {
                            awaitEachGesture {
                                awaitFirstDown(requireUnconsumed = false, pass = PointerEventPass.Initial)
                                focusManager.clearFocus()
                                keyboardController?.hide()
                            }
                        },
                ) {
                    if (state.loadingGames && visibleGames.isEmpty()) {
                        Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            StoreScrollableControls(
                                state = state,
                                onSortChange = viewModel::setCatalogSort,
                                onFilterToggle = viewModel::toggleCatalogFilter,
                            )
                            RefreshingGamesPlaceholder(Modifier.weight(1f))
                        }
                    } else {
                        StoreGameGrid(
                            games = visibleGames,
                            favoriteIds = state.settings.favoriteGameIds,
                            settings = state.settings,
                            tvProfile = tvProfile,
                            state = state,
                            onSelect = viewModel::selectGame,
                            onFavorite = viewModel::updateFavorites,
                            onPlay = viewModel::play,
                            onChooseStore = viewModel::chooseStore,
                            onSortChange = viewModel::setCatalogSort,
                            onFilterToggle = viewModel::toggleCatalogFilter,
                            onClearSearch = { viewModel.setCatalogSearch("") },
                            onClearFilters = viewModel::clearCatalogFilters,
                            gridState = gridState,
                            modifier = Modifier.fillMaxSize(),
                        )
                    }
                    if (showScrollActions) {
                        Box(Modifier.align(Alignment.BottomEnd).padding(2.dp)) {
                            StoreScrollActionButton(
                                iconRes = R.drawable.ic_arrow_up,
                                contentDescription = stringResource(R.string.action_scroll_top),
                            ) {
                                scope.launch { gridState.animateScrollToItem(0) }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
internal fun StoreScrollableControls(
    state: OpenNowUiState,
    onSortChange: (String) -> Unit,
    onFilterToggle: (String) -> Unit,
) {
    val filterGroups = catalogVisibleFilterGroups(state.catalogResult.filterGroups)
    val filterOptions = catalogFilterOptions(filterGroups)
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            SortPicker(
                options = state.catalogResult.sortOptions,
                selected = state.catalogSortId,
                onSelect = onSortChange,
                modifier = Modifier.weight(1f),
            )
            if (filterOptions.isNotEmpty()) {
                FilterMenu(options = filterOptions, selectedIds = state.catalogFilterIds, onToggle = onFilterToggle)
            }
        }
        SelectedFilterChips(options = filterOptions, selectedIds = state.catalogFilterIds, onToggle = onFilterToggle)
        if (state.error != null) {
            Text(state.error.orEmpty(), color = Color(0xffff9f9f), modifier = Modifier.padding(horizontal = 4.dp))
        }
    }
}

@Composable
internal fun StoreScrollActionButton(iconRes: Int, contentDescription: String, onClick: () -> Unit) {
    Surface(
        shape = CircleShape,
        color = PanelAlt.copy(alpha = 0.96f),
        tonalElevation = 4.dp,
        shadowElevation = 4.dp,
    ) {
        IconButton(onClick = onClick, modifier = Modifier.size(44.dp)) {
            Icon(
                painter = painterResource(iconRes),
                contentDescription = contentDescription,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(22.dp),
            )
        }
    }
}

@Composable
internal fun SearchEmptyState(
    title: String,
    message: String,
    onClearSearch: (() -> Unit)? = null,
    onClearFilters: (() -> Unit)? = null,
) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 28.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Text(
            title,
            color = TextPrimary,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
        )
        Text(
            message,
            color = TextMuted,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            onClearSearch?.let { clearSearch ->
                OutlinedButton(onClick = clearSearch) {
                    Text(stringResource(R.string.search_clear), maxLines = 1, overflow = TextOverflow.Ellipsis)
                }
            }
            onClearFilters?.let { clearFilters ->
                OutlinedButton(onClick = clearFilters) {
                    Text(stringResource(R.string.action_clear_filters), maxLines = 1, overflow = TextOverflow.Ellipsis)
                }
            }
        }
    }
}

@Composable
internal fun RefreshingGamesPlaceholder(modifier: Modifier = Modifier) {
    Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Loading games", color = TextMuted)
    }
}

@Composable
internal fun SwipeToRefreshContainer(
    refreshing: Boolean,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
    showRefreshIndicator: Boolean = true,
    content: @Composable () -> Unit,
) {
    var dragDistance by remember { mutableFloatStateOf(0f) }
    Box(
        modifier.pointerInput(refreshing) {
            detectVerticalDragGestures(
                onDragEnd = {
                    if (!refreshing && dragDistance > 140f) onRefresh()
                    dragDistance = 0f
                },
                onDragCancel = { dragDistance = 0f },
                onVerticalDrag = { change, dragAmount ->
                    if (dragAmount > 0) {
                        dragDistance += dragAmount
                        change.consume()
                    }
                },
            )
        },
    ) {
        content()
        AnimatedVisibility(
            visible = showRefreshIndicator && (refreshing || dragDistance > 40f),
            modifier = Modifier.align(Alignment.TopCenter).padding(top = 8.dp),
        ) {
            Surface(
                shape = RoundedCornerShape(999.dp),
                color = PanelAlt.copy(alpha = 0.94f),
                tonalElevation = 3.dp,
            ) {
                Row(Modifier.padding(horizontal = 12.dp, vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                    if (refreshing) CircularProgressIndicator(Modifier.size(16.dp), strokeWidth = 2.dp)
                    Text(
                        if (refreshing) "Refreshing" else "Release to refresh",
                        color = TextMuted,
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier.padding(start = if (refreshing) 8.dp else 0.dp),
                    )
                }
            }
        }
    }
}

@Composable
internal fun GameGrid(
    games: List<GameInfo>,
    favoriteIds: List<String>,
    settings: AppSettings,
    tvProfile: Boolean,
    onSelect: (GameInfo) -> Unit,
    onFavorite: (String) -> Unit,
    onPlay: (GameInfo) -> Unit,
    onChooseStore: (GameInfo) -> Unit,
    modifier: Modifier = Modifier,
    gridState: androidx.compose.foundation.lazy.grid.LazyGridState = rememberLazyGridState(),
    emptyContent: (@Composable () -> Unit)? = null,
) {
    if (games.isEmpty()) {
        Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            if (emptyContent != null) {
                emptyContent()
            } else {
                Text(stringResource(R.string.no_games_loaded), color = TextMuted)
            }
        }
        return
    }
    val scale = settings.posterSizeScale.coerceIn(0.82f, 1.08f)
    val compact = settings.compactGameCards
    BoxWithConstraints(modifier.fillMaxSize()) {
        val columns = gameGridColumnCount(maxWidth)
        val phonePortrait = isPhonePortrait(maxWidth, maxHeight)
        val phoneLandscape = isPhoneLandscape(maxWidth, maxHeight)
        val cardHeight = when {
            phoneLandscape && compact -> 158.dp
            phoneLandscape -> 178.dp
            phonePortrait && compact -> 218.dp
            phonePortrait -> 246.dp
            compact -> 252.dp
            else -> 286.dp
        }
        LazyVerticalGrid(
            modifier = Modifier.fillMaxSize(),
            state = gridState,
            columns = GridCells.Fixed(columns),
            contentPadding = PaddingValues(4.dp),
            horizontalArrangement = Arrangement.spacedBy(if (compact) 8.dp else 10.dp),
            verticalArrangement = Arrangement.spacedBy(if (compact) 10.dp else 12.dp),
        ) {
            gridItems(games, key = { it.id }) { game ->
                GameCard(
                    game = game,
                    favorite = game.id in favoriteIds,
                    settings = settings,
                    tvProfile = tvProfile,
                    cardHeight = cardHeight * scale,
                    thumbnailPlayOverlay = !tvProfile,
                    onSelect = onSelect,
                    onFavorite = onFavorite,
                    onPlay = onPlay,
                    onChooseStore = onChooseStore,
                )
            }
        }
    }
}

@Composable
internal fun StoreGameGrid(
    games: List<GameInfo>,
    favoriteIds: List<String>,
    settings: AppSettings,
    tvProfile: Boolean,
    state: OpenNowUiState,
    onSelect: (GameInfo) -> Unit,
    onFavorite: (String) -> Unit,
    onPlay: (GameInfo) -> Unit,
    onChooseStore: (GameInfo) -> Unit,
    onSortChange: (String) -> Unit,
    onFilterToggle: (String) -> Unit,
    onClearSearch: () -> Unit,
    onClearFilters: () -> Unit,
    gridState: androidx.compose.foundation.lazy.grid.LazyGridState,
    modifier: Modifier = Modifier,
) {
    if (games.isEmpty()) {
        Column(modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            StoreScrollableControls(state, onSortChange, onFilterToggle)
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                val hasSearch = state.catalogSearch.isNotBlank()
                val hasFilters = state.catalogFilterIds.isNotEmpty()
                if (hasSearch || hasFilters) {
                    SearchEmptyState(
                        title = stringResource(R.string.store_empty_search_title),
                        message = when {
                            hasSearch && hasFilters -> stringResource(R.string.store_empty_search_filters_body)
                            hasSearch -> stringResource(R.string.store_empty_search_body)
                            else -> stringResource(R.string.store_empty_filters_body)
                        },
                        onClearSearch = if (hasSearch) onClearSearch else null,
                        onClearFilters = if (hasFilters) onClearFilters else null,
                    )
                } else {
                    Text(stringResource(R.string.no_games_loaded), color = TextMuted)
                }
            }
        }
        return
    }
    val scale = settings.posterSizeScale.coerceIn(0.82f, 1.08f)
    val compact = settings.compactGameCards
    BoxWithConstraints(modifier.fillMaxSize()) {
        val columns = gameGridColumnCount(maxWidth)
        val phonePortrait = isPhonePortrait(maxWidth, maxHeight)
        val phoneLandscape = isPhoneLandscape(maxWidth, maxHeight)
        val cardHeight = when {
            phoneLandscape && compact -> 158.dp
            phoneLandscape -> 178.dp
            phonePortrait && compact -> 218.dp
            phonePortrait -> 246.dp
            compact -> 252.dp
            else -> 286.dp
        }
        LazyVerticalGrid(
            modifier = Modifier.fillMaxSize(),
            state = gridState,
            columns = GridCells.Fixed(columns),
            contentPadding = PaddingValues(0.dp),
            horizontalArrangement = Arrangement.spacedBy(if (compact) 8.dp else 10.dp),
            verticalArrangement = Arrangement.spacedBy(if (compact) 10.dp else 12.dp),
        ) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                StoreScrollableControls(state, onSortChange, onFilterToggle)
            }
            gridItems(games, key = { it.id }) { game ->
                GameCard(
                    game = game,
                    favorite = game.id in favoriteIds,
                    settings = settings,
                    tvProfile = tvProfile,
                    cardHeight = cardHeight * scale,
                    thumbnailPlayOverlay = !tvProfile,
                    onSelect = onSelect,
                    onFavorite = onFavorite,
                    onPlay = onPlay,
                    onChooseStore = onChooseStore,
                )
            }
        }
    }
}

internal fun gameGridColumnCount(maxWidth: androidx.compose.ui.unit.Dp): Int =
    when {
        maxWidth >= 1100.dp -> 5
        maxWidth >= 840.dp -> 4
        maxWidth >= 600.dp -> 3
        else -> 2
    }

@Composable
internal fun GameCard(
    game: GameInfo,
    favorite: Boolean,
    settings: AppSettings,
    tvProfile: Boolean,
    cardHeight: androidx.compose.ui.unit.Dp,
    thumbnailPlayOverlay: Boolean,
    onSelect: (GameInfo) -> Unit,
    onFavorite: (String) -> Unit,
    onPlay: (GameInfo) -> Unit,
    onChooseStore: (GameInfo) -> Unit,
) {
    var focused by remember { mutableStateOf(false) }
    val cardShape = RoundedCornerShape(if (settings.expressiveUi) 12.dp else 8.dp)
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(cardHeight)
            .onFocusChanged { focused = it.isFocused || it.hasFocus }
            .then(
                if (tvProfile) {
                    Modifier
                        .border(
                            width = if (focused) 2.dp else 1.dp,
                            color = if (focused) MaterialTheme.colorScheme.primary else Color.Transparent,
                            shape = cardShape,
                        )
                        .onPreviewKeyEvent { event ->
                            if (isTvActivateKey(event)) {
                                onSelect(game)
                                true
                            } else {
                                false
                            }
                        }
                        .focusable()
                } else {
                    Modifier
                },
            ),
        colors = CardDefaults.cardColors(
            containerColor = if (settings.expressiveUi) MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.72f) else Panel,
        ),
        shape = cardShape,
    ) {
        Box(
            Modifier
                .weight(1f)
                .clickable { onSelect(game) },
        ) {
            UrlImage(game.imageUrl, Modifier.fillMaxSize())
            FavoriteIconButton(
                favorite = favorite,
                onClick = { onFavorite(game.id) },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp),
            )
            if (thumbnailPlayOverlay) {
                ThumbnailStoreButton(
                    badge = launcherBadgeForGame(game),
                    onClick = { onChooseStore(game) },
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(8.dp),
                )
                ThumbnailPlayButton(
                    onClick = { onPlay(game) },
                    onLongClick = { onChooseStore(game) },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(8.dp),
                )
            }
        }
        if (!thumbnailPlayOverlay) {
            Column(
                Modifier
                    .clickable { onSelect(game) }
                    .padding(9.dp),
                verticalArrangement = Arrangement.spacedBy(5.dp),
            ) {
                /*
                Text(
                    game.title,
                    fontWeight = FontWeight.Bold,
                    minLines = 2,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodyMedium,
                )
                */
                if (settings.showGameStoreLabels) {
                    Text(displayStoresForGame(game), color = TextMuted, maxLines = 1, overflow = TextOverflow.Ellipsis, style = MaterialTheme.typography.labelMedium)
                }
            }
        }
        if (!thumbnailPlayOverlay) {
            Box(Modifier.padding(start = 9.dp, end = 9.dp, bottom = 9.dp)) {
                LongPressPlayButton(
                    onClick = { onPlay(game) },
                    onLongClick = { onChooseStore(game) },
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}

@Composable
internal fun ThumbnailStoreButton(badge: LauncherBadge, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val chooseLauncherLabel = stringResource(R.string.store_selector_choose_launcher)
    Surface(
        modifier = modifier.size(44.dp),
        shape = RoundedCornerShape(12.dp),
        color = badge.background.copy(alpha = 0.94f),
        tonalElevation = 3.dp,
        shadowElevation = 3.dp,
    ) {
        IconButton(
            onClick = onClick,
            modifier = Modifier.semantics {
                contentDescription = "${badge.name} $chooseLauncherLabel"
            },
        ) {
            Icon(
                painter = painterResource(badge.iconRes),
                contentDescription = null,
                tint = badge.foreground,
                modifier = Modifier.size(24.dp),
            )
        }
    }
}

internal fun launcherBadgeForGame(game: GameInfo): LauncherBadge {
    val candidateStores = mutableListOf<String>()
    game.variants.getOrNull(game.selectedVariantIndex)?.let { selectedVariant ->
        candidateStores += splitGameStoreKeys(selectedVariant.store)
    }
    game.variants.forEach { variant ->
        candidateStores += splitGameStoreKeys(variant.store)
    }
    game.availableStores.forEach { store ->
        candidateStores += splitGameStoreKeys(store)
    }
    val key = candidateStores.firstOrNull { it.isNotBlank() }
    return launcherBadgeForStoreKey(key)
}

internal fun launcherBadgeForStoreKey(storeKey: String?): LauncherBadge =
    when (storeKey) {
        "STEAM" -> LauncherBadge(R.drawable.ic_store_steam, "Steam", Color(0xff17324d))
        "EPIC", "EGS", "EPIC_GAMES_STORE" -> LauncherBadge(R.drawable.ic_store_epic, "Epic", Color(0xff111111))
        "HOYO", "HOYOVERSE", "HOYOPLAY", "HOYO_PLAY", "MIHOYO" -> LauncherBadge(R.drawable.ic_store_hoyo, "HoYo", Color(0xff2b62d9))
        "XBOX", "XBOX_GAME_PASS", "GAME_PASS" -> LauncherBadge(R.drawable.ic_store_xbox, "Xbox", Color(0xff107c10))
        "MICROSOFT", "MICROSOFT_STORE" -> LauncherBadge(R.drawable.ic_store_microsoft, "Microsoft Store", Color(0xff0067b8))
        "UBISOFT", "UBISOFT_CONNECT" -> LauncherBadge(R.drawable.ic_store_ubisoft, "Ubisoft Connect", Color(0xff006efc))
        "EA", "EA_APP", "ORIGIN" -> LauncherBadge(R.drawable.ic_store_ea, "EA app", Color(0xffff4747))
        "GOG", "GOG.COM", "GOG_COM" -> LauncherBadge(R.drawable.ic_store_gog, "GOG", Color(0xff6a35a8))
        "BATTLENET", "BATTLE.NET", "BATTLE_NET", "BLIZZARD" -> LauncherBadge(R.drawable.ic_store_battlenet, "Battle.net", Color(0xff148eff))
        "RIOT", "RIOT_CLIENT", "RIOT_GAMES" -> LauncherBadge(R.drawable.ic_store_riot, "Riot", Color(0xffd13639))
        "ROCKSTAR", "ROCKSTAR_GAMES", "ROCKSTAR_GAMES_LAUNCHER" -> LauncherBadge(R.drawable.ic_store_rockstar, "Rockstar", Color(0xffffc400), Color(0xff111111))
        "NCSOFT", "NC_SOFT", "PURPLE" -> LauncherBadge(R.drawable.ic_tab_store, "NCSOFT", Color(0xffb4822d), Color(0xff111111))
        "GOOGLE_PLAY", "PLAY_STORE", "ANDROID" -> LauncherBadge(R.drawable.ic_store_google_play, "Google Play", Color(0xff0f9d58))
        "AMAZON", "AMAZON_GAMES" -> LauncherBadge(R.drawable.ic_store_amazon, "Amazon Games", Color(0xffff9900), Color(0xff111111))
        else -> LauncherBadge(R.drawable.ic_tab_store, "GeForce NOW", Color.Black.copy(alpha = 0.72f))
    }

internal fun displayStoresForGame(game: GameInfo): String {
    val stores = displayStoresForVariants(game.variants).ifEmpty {
        game.availableStores.map(::gameStoreDisplayName)
    }.distinctBy { normalizeGameStore(it) }
    return stores.joinToString(", ").ifBlank { "GeForce NOW" }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun ThumbnailPlayButton(onClick: () -> Unit, onLongClick: () -> Unit, modifier: Modifier = Modifier) {
    val playColor = MaterialTheme.colorScheme.onPrimary
    Surface(
        modifier = modifier
            .size(44.dp)
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick,
                onLongClickLabel = stringResource(R.string.store_selector_play_long_press),
            ),
        shape = CircleShape,
        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.94f),
        tonalElevation = 3.dp,
        shadowElevation = 3.dp,
    ) {
        Canvas(Modifier.fillMaxSize().padding(14.dp)) {
            val path = Path().apply {
                moveTo(size.width * 0.32f, size.height * 0.18f)
                lineTo(size.width * 0.32f, size.height * 0.82f)
                lineTo(size.width * 0.84f, size.height * 0.5f)
                close()
            }
            drawPath(path, playColor)
        }
    }
}

@Composable
internal fun AnimatedLaunchOverlay(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    val visibleState = remember {
        MutableTransitionState(false).apply {
            targetState = true
        }
    }
    AnimatedVisibility(
        visibleState = visibleState,
        enter = fadeIn() + slideInVertically(initialOffsetY = { it / 4 }) + scaleIn(initialScale = 0.94f),
        exit = fadeOut() + slideOutVertically(targetOffsetY = { it / 4 }) + scaleOut(targetScale = 0.94f),
        modifier = modifier,
    ) {
        content()
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun GameDetailsSheet(
    game: GameInfo,
    favorite: Boolean,
    defaultVariantId: String?,
    onPlay: (GameInfo) -> Unit,
    onChooseStore: (GameInfo) -> Unit,
    onFavorite: (String) -> Unit,
    onDismiss: () -> Unit,
) {
    val playFocusRequester = remember(game.id) { FocusRequester() }
    LaunchedEffect(game.id) {
        runCatching { playFocusRequester.requestFocus() }
    }
    Box(
        Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.72f))
            .clickable(onClick = onDismiss),
        contentAlignment = Alignment.BottomCenter,
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.92f)
                .clickable(onClick = {}),
            shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
            color = Panel,
            tonalElevation = 8.dp,
        ) {
            BoxWithConstraints(Modifier.fillMaxSize()) {
                val aspect = if (maxHeight.value > 0f) maxWidth.value / maxHeight.value else 1f
                val landscapeTvLayout = maxWidth >= 720.dp && aspect >= 1.35f
                val phoneLandscapeLayout = landscapeTvLayout && minOf(maxWidth, maxHeight) < PhoneNavRailMaxSmallestWidth
                if (landscapeTvLayout) {
                    GameDetailsLandscapeContent(
                        game = game,
                        favorite = favorite,
                        defaultVariantId = defaultVariantId,
                        onPlay = onPlay,
                        onChooseStore = onChooseStore,
                        onFavorite = onFavorite,
                        onDismiss = onDismiss,
                        playFocusRequester = playFocusRequester,
                        shortHeight = maxHeight <= 620.dp,
                        imageActionsOverlay = phoneLandscapeLayout,
                    )
                } else {
                    GameDetailsScrollableContent(
                        game = game,
                        favorite = favorite,
                        defaultVariantId = defaultVariantId,
                        onPlay = onPlay,
                        onChooseStore = onChooseStore,
                        onFavorite = onFavorite,
                        onDismiss = onDismiss,
                        playFocusRequester = playFocusRequester,
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun GameDetailsLandscapeContent(
    game: GameInfo,
    favorite: Boolean,
    defaultVariantId: String?,
    onPlay: (GameInfo) -> Unit,
    onChooseStore: (GameInfo) -> Unit,
    onFavorite: (String) -> Unit,
    onDismiss: () -> Unit,
    playFocusRequester: FocusRequester,
    shortHeight: Boolean,
    imageActionsOverlay: Boolean,
) {
    val description = gameDescriptionForDetails(game)
    val chips = gameDetailChips(game)
    val launchStores = displayStoresForVariants(game.variants).ifEmpty { game.availableStores }.map(::gameStoreDisplayName).distinct()
    val sideScrollState = rememberScrollState()
    Row(
        Modifier
            .fillMaxSize()
            .padding(horizontal = if (shortHeight) 18.dp else 24.dp, vertical = if (shortHeight) 16.dp else 22.dp),
        horizontalArrangement = Arrangement.spacedBy(if (shortHeight) 16.dp else 22.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            Modifier
                .weight(0.92f)
                .fillMaxHeight()
                .clip(RoundedCornerShape(20.dp)),
        ) {
            UrlImage(game.screenshotUrl ?: game.imageUrl, Modifier.fillMaxSize())
            Box(Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.3f)))
            if (imageActionsOverlay) {
                ImageCloseButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(10.dp),
                )
            }
            FavoriteIconButton(
                favorite = favorite,
                onClick = { onFavorite(game.id) },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(10.dp),
            )
            Column(
                Modifier
                    .align(Alignment.BottomStart)
                    .fillMaxWidth()
                    .padding(
                        start = if (shortHeight) 16.dp else 20.dp,
                        top = if (shortHeight) 16.dp else 20.dp,
                        end = if (imageActionsOverlay) 152.dp else if (shortHeight) 16.dp else 20.dp,
                        bottom = if (shortHeight) 16.dp else 20.dp,
                    ),
                verticalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                Text(
                    game.title,
                    style = if (shortHeight) MaterialTheme.typography.titleLarge else MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(displayStoresForGame(game), color = TextMuted, maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
            if (imageActionsOverlay) {
                LongPressPlayButton(
                    onClick = {
                        onDismiss()
                        onPlay(game)
                    },
                    onLongClick = {
                        onDismiss()
                        onChooseStore(game)
                    },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(14.dp)
                        .width(126.dp)
                        .focusRequester(playFocusRequester),
                )
            }
        }

        Column(
            Modifier
                .weight(1.08f)
                .fillMaxHeight()
                .then(if (imageActionsOverlay) Modifier.verticalScroll(sideScrollState) else Modifier),
            verticalArrangement = Arrangement.spacedBy(if (shortHeight) 8.dp else 10.dp),
        ) {
            Text(
                description ?: "No description is available for this game yet.",
                color = if (description == null) TextMuted else TextPrimary,
                style = MaterialTheme.typography.bodyMedium,
                lineHeight = if (shortHeight) MaterialTheme.typography.bodyMedium.lineHeight * 0.92f else MaterialTheme.typography.bodyMedium.lineHeight,
                maxLines = if (shortHeight) 6 else 8,
                overflow = TextOverflow.Ellipsis,
            )
            if (chips.isNotEmpty()) {
                FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    chips.take(if (shortHeight) 8 else 12).forEach { label ->
                        AssistChip(onClick = {}, label = { Text(label, maxLines = 1, overflow = TextOverflow.Ellipsis) })
                    }
                }
            }
            CompactDetailRows(game)
            if (launchStores.isNotEmpty()) {
                Text(
                    "Launchers: ${launchStores.joinToString(", ")}",
                    color = TextMuted,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            LaunchOptionsList(
                game = game,
                defaultVariantId = defaultVariantId,
                compact = true,
            )
            if (!imageActionsOverlay) {
                Spacer(Modifier.weight(1f))
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    OutlinedButton(onClick = onDismiss, modifier = Modifier.weight(0.8f)) {
                        Text("Dismiss", maxLines = 1, overflow = TextOverflow.Ellipsis)
                    }
                    LongPressPlayButton(
                        onClick = {
                            onDismiss()
                            onPlay(game)
                        },
                        onLongClick = {
                            onDismiss()
                            onChooseStore(game)
                        },
                        modifier = Modifier
                            .weight(1.2f)
                            .focusRequester(playFocusRequester),
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun GameDetailsScrollableContent(
    game: GameInfo,
    favorite: Boolean,
    defaultVariantId: String?,
    onPlay: (GameInfo) -> Unit,
    onChooseStore: (GameInfo) -> Unit,
    onFavorite: (String) -> Unit,
    onDismiss: () -> Unit,
    playFocusRequester: FocusRequester,
) {
    Column(Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(bottom = 18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            item {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(220.dp),
                ) {
                    UrlImage(
                        game.screenshotUrl ?: game.imageUrl,
                        Modifier.fillMaxSize(),
                    )
                    Box(Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.28f)))
                    FavoriteIconButton(
                        favorite = favorite,
                        onClick = { onFavorite(game.id) },
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(10.dp),
                    )
                    Column(
                        Modifier
                            .align(Alignment.BottomStart)
                            .padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                    ) {
                        Text(
                            game.title,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                        )
                        Text(displayStoresForGame(game), color = TextMuted, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    }
                }
            }
            item {
                Column(Modifier.padding(horizontal = 18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    val description = gameDescriptionForDetails(game)
                    if (description != null) {
                        Text(description, color = TextPrimary, style = MaterialTheme.typography.bodyMedium)
                    } else {
                        Text("No description is available for this game yet.", color = TextMuted)
                    }
                    val chips = gameDetailChips(game)
                    if (chips.isNotEmpty()) {
                        FlowRow(horizontalArrangement = Arrangement.spacedBy(7.dp), verticalArrangement = Arrangement.spacedBy(7.dp)) {
                            chips.take(12).forEach { label ->
                                AssistChip(onClick = {}, label = { Text(label, maxLines = 1, overflow = TextOverflow.Ellipsis) })
                            }
                        }
                    }
                    DetailRows(game)
                    LaunchOptionsList(
                        game = game,
                        defaultVariantId = defaultVariantId,
                        compact = false,
                    )
                }
            }
        }
        Surface(color = Panel.copy(alpha = 0.98f), tonalElevation = 8.dp) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                OutlinedButton(onClick = onDismiss, modifier = Modifier.weight(0.8f)) {
                    Text("Dismiss", maxLines = 1, overflow = TextOverflow.Ellipsis)
                }
                LongPressPlayButton(
                    onClick = {
                        onDismiss()
                        onPlay(game)
                    },
                    onLongClick = {
                        onDismiss()
                        onChooseStore(game)
                    },
                    modifier = Modifier
                        .weight(1.2f)
                        .focusRequester(playFocusRequester),
                )
            }
        }
    }
}

@Composable
internal fun LaunchOptionsList(
    game: GameInfo,
    defaultVariantId: String?,
    compact: Boolean,
) {
    val variants = launchableGameVariants(game.variants)
    if (variants.size <= 1) return
    Column(verticalArrangement = Arrangement.spacedBy(if (compact) 6.dp else 8.dp)) {
        Text(
            stringResource(R.string.store_selector_launchers),
            color = TextMuted,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
        )
        variants.take(if (compact) 3 else variants.size).forEach { variant ->
            val isDefault = variant.id == defaultVariantId
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(if (compact) 12.dp else 14.dp),
                color = if (isDefault) MaterialTheme.colorScheme.primary.copy(alpha = 0.18f) else PanelAlt,
            ) {
                Row(
                    Modifier.padding(horizontal = if (compact) 10.dp else 12.dp, vertical = if (compact) 8.dp else 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    Column(Modifier.weight(1f)) {
                        Text(gameStoreDisplayName(variant.store), fontWeight = FontWeight.SemiBold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                        val details = variantDetailsText(variant)
                        Text(
                            if (isDefault) {
                                listOf(stringResource(R.string.store_selector_default), details).filter { it.isNotBlank() }.joinToString(" - ")
                            } else {
                                details.ifBlank { stringResource(R.string.store_selector_available_launcher) }
                            },
                            color = TextMuted,
                            style = MaterialTheme.typography.bodySmall,
                            maxLines = if (compact) 1 else 2,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun LongPressPlayButton(
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var focused by remember { mutableStateOf(false) }
    val shape = RoundedCornerShape(999.dp)
    Surface(
        modifier = modifier
            .height(48.dp)
            .onFocusChanged { focused = it.isFocused }
            .onPreviewKeyEvent { event ->
                if (isTvActivateKey(event)) {
                    onClick()
                    true
                } else {
                    false
                }
            }
            .focusable()
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick,
                onLongClickLabel = stringResource(R.string.store_selector_play_long_press),
            )
            .border(
                width = if (focused) 2.dp else 0.dp,
                color = if (focused) TextPrimary else Color.Transparent,
                shape = shape,
            ),
        shape = shape,
        color = MaterialTheme.colorScheme.primary,
        tonalElevation = 2.dp,
    ) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(
                stringResource(R.string.action_play),
                color = MaterialTheme.colorScheme.onPrimary,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

internal fun variantDetailsText(variant: GameVariant): String =
    listOfNotNull(
        variant.libraryStatus?.takeIf { it.isNotBlank() }?.let(::formatGameMetadataLabel),
        variant.supportedControls.takeIf { it.isNotEmpty() }?.joinToString(", ") { formatGameMetadataLabel(it) },
        variant.lastPlayedDate?.takeIf { it.isNotBlank() }?.let { "Last played $it" },
    ).joinToString(" - ")

@Composable
internal fun ImageCloseButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.size(44.dp),
        shape = CircleShape,
        color = Color.Black.copy(alpha = 0.58f),
        tonalElevation = 3.dp,
    ) {
        IconButton(onClick = onClick) {
            Icon(
                painter = painterResource(R.drawable.ic_clear),
                contentDescription = stringResource(R.string.action_cancel),
                tint = TextPrimary,
                modifier = Modifier.size(20.dp),
            )
        }
    }
}

@Composable
internal fun FavoriteIconButton(favorite: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val label = stringResource(if (favorite) R.string.action_saved else R.string.action_save)
    Surface(
        modifier = modifier.size(44.dp),
        shape = CircleShape,
        color = Color.Black.copy(alpha = 0.52f),
        tonalElevation = 3.dp,
    ) {
        IconButton(onClick = onClick) {
            Icon(
                painter = painterResource(if (favorite) R.drawable.ic_save_filled else R.drawable.ic_save),
                contentDescription = label,
                tint = if (favorite) MaterialTheme.colorScheme.primary else TextPrimary,
                modifier = Modifier.size(22.dp),
            )
        }
    }
}

internal fun gameDescriptionForDetails(game: GameInfo): String? =
    game.longDescription?.takeIf { it.isNotBlank() }
        ?: game.description?.takeIf { it.isNotBlank() }

internal fun gameDetailChips(game: GameInfo): List<String> =
    (game.featureLabels + game.genres + listOfNotNull(game.playType, game.membershipTierLabel))
        .map { it.trim() }
        .filter { it.isNotBlank() }
        .map(::formatGameMetadataLabel)
        .filterNot(::isNoisyGameTag)
        .distinctBy { it.lowercase(Locale.US) }
        .take(10)

internal fun formatGameMetadataLabel(raw: String): String {
    val compact = raw.trim()
        .removePrefix("GFN_")
        .removePrefix("GAME_")
        .replace(Regex("[_-]+"), " ")
        .replace(Regex("\\s+"), " ")
        .trim()
    if (compact.isBlank()) return ""
    val lower = compact.lowercase(Locale.US)
    return when (lower) {
        "full game" -> "Full game"
        "single player" -> "Single-player"
        "multi player", "multiplayer" -> "Multiplayer"
        "controller", "gamepad" -> "Controller"
        "keyboard mouse", "mouse keyboard" -> "Mouse and keyboard"
        else -> compact.split(" ").joinToString(" ") { word ->
            if (word.length <= 3 && word.all { it.isUpperCase() || it.isDigit() }) {
                word
            } else {
                word.lowercase(Locale.US).replaceFirstChar { char -> char.titlecase(Locale.US) }
            }
        }
    }
}

internal fun isNoisyGameTag(label: String): Boolean {
    val normalized = label.trim().lowercase(Locale.US)
    return normalized.isBlank() ||
        normalized == "unknown" ||
        normalized == "gfn" ||
        normalized == "nvidia" ||
        normalized.contains("sku based tag") ||
        normalized.contains("catalog")
}

@Composable
internal fun CompactDetailRows(game: GameInfo) {
    val rows = listOfNotNull(
        game.publisherName?.takeIf { it.isNotBlank() }?.let { "Publisher" to it },
        game.playabilityState?.takeIf { it.isNotBlank() }?.let { "Status" to it },
        game.contentRatings.takeIf { it.isNotEmpty() }?.joinToString(", ")?.let { "Rating" to it },
        game.lastPlayed?.takeIf { it.isNotBlank() }?.let { "Last played" to it },
        game.availableStores.takeIf { it.isNotEmpty() }?.map(::gameStoreDisplayName)?.distinct()?.joinToString(", ")?.let { "Stores" to it },
    ).take(3)
    if (rows.isEmpty()) return
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        rows.forEach { (label, value) ->
            Row(
                Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(PanelAlt)
                    .padding(horizontal = 10.dp, vertical = 7.dp),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                Text(label, color = TextMuted, style = MaterialTheme.typography.bodySmall, modifier = Modifier.width(82.dp), maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text(value, color = TextPrimary, style = MaterialTheme.typography.bodySmall, modifier = Modifier.weight(1f), maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
        }
    }
}

@Composable
internal fun DetailRows(game: GameInfo) {
    val rows = listOfNotNull(
        game.publisherName?.takeIf { it.isNotBlank() }?.let { "Publisher" to it },
        game.playabilityState?.takeIf { it.isNotBlank() }?.let { "Status" to it },
        game.contentRatings.takeIf { it.isNotEmpty() }?.joinToString(", ")?.let { "Rating" to it },
        game.lastPlayed?.takeIf { it.isNotBlank() }?.let { "Last played" to it },
        game.availableStores.takeIf { it.isNotEmpty() }?.map(::gameStoreDisplayName)?.distinct()?.joinToString(", ")?.let { "Stores" to it },
    )
    if (rows.isEmpty()) return
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        rows.forEach { (label, value) ->
            Row(
                Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(PanelAlt)
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Text(label, color = TextMuted, style = MaterialTheme.typography.bodySmall, modifier = Modifier.width(92.dp))
                Text(value, color = TextPrimary, style = MaterialTheme.typography.bodySmall, modifier = Modifier.weight(1f))
            }
        }
    }
}

internal fun gameMatchesSearch(game: GameInfo, query: String): Boolean {
    val normalized = query.trim().lowercase()
    if (normalized.isBlank()) return true
    val haystack = buildString {
        append(game.title).append(' ')
        append(game.description.orEmpty()).append(' ')
        append(game.longDescription.orEmpty()).append(' ')
        append(game.publisherName.orEmpty()).append(' ')
        append(game.genres.joinToString(" ")).append(' ')
        append(game.featureLabels.joinToString(" ")).append(' ')
        append(displayStoresForGame(game))
    }.lowercase()
    return normalized.split(Regex("\\s+")).all { it in haystack }
}

@Composable
@OptIn(ExperimentalLayoutApi::class)
internal fun StoreLaunchSelector(
    game: GameInfo,
    defaultVariantId: String?,
    onLaunch: (GameInfo, GameVariant) -> Unit,
    onSetDefaultStore: (String, String?) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val variants = remember(game) { launchableGameVariants(game.variants) }
    val context = LocalContext.current
    val initialVariantId = remember(game.id, defaultVariantId, variants) {
        defaultVariantId?.takeIf { savedId -> variants.any { it.id == savedId } }
            ?: variants.firstOrNull()?.id
    }
    var selectedVariantId by remember(game.id, initialVariantId) { mutableStateOf(initialVariantId) }
    var rememberDefaultStore by remember(game.id, defaultVariantId) { mutableStateOf(defaultVariantId != null) }
    val selectedVariant = variants.firstOrNull { it.id == selectedVariantId }
    val continueFocusRequester = remember(game.id) { FocusRequester() }
    BackHandler(onBack = onDismiss)
    LaunchedEffect(game.id, variants.size) {
        if (variants.isNotEmpty()) {
            runCatching { continueFocusRequester.requestFocus() }
        }
    }
    BoxWithConstraints(
        Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.72f))
            .clickable(enabled = false) {},
        contentAlignment = Alignment.Center,
    ) {
        val landscape = maxWidth > maxHeight
        Card(
            modifier = modifier
                .fillMaxWidth(if (landscape) 0.78f else 0.92f)
                .fillMaxHeight(if (landscape) 0.86f else 0.64f),
            colors = CardDefaults.cardColors(containerColor = Panel),
            shape = RoundedCornerShape(22.dp),
        ) {
            Column(Modifier.fillMaxSize().padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    UrlImage(
                        game.imageUrl,
                        Modifier
                            .width(58.dp)
                            .height(76.dp)
                            .clip(RoundedCornerShape(12.dp)),
                    )
                    Spacer(Modifier.width(12.dp))
                    Column(Modifier.weight(1f)) {
                        Text(game.title, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                        Text(stringResource(R.string.store_selector_choose_launcher), color = TextMuted, style = MaterialTheme.typography.bodySmall)
                    }
                }

                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    items(variants, key = { it.id }) { variant ->
                        val isSelected = variant.id == selectedVariantId
                        val isSavedDefault = variant.id == defaultVariantId
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedVariantId = variant.id },
                            shape = RoundedCornerShape(14.dp),
                            color = if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.18f) else PanelAlt,
                        ) {
                            Row(
                                Modifier.padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                            ) {
                                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                    Text(gameStoreDisplayName(variant.store), fontWeight = FontWeight.Bold)
                                    val details = listOf(
                                        if (isSavedDefault) stringResource(R.string.store_selector_default) else "",
                                        variantDetailsText(variant),
                                    ).filter { it.isNotBlank() }.joinToString(" - ")
                                    if (details.isNotBlank()) {
                                        Text(details, color = TextMuted, style = MaterialTheme.typography.bodySmall, maxLines = 1, overflow = TextOverflow.Ellipsis)
                                    }
                                }
                                if (isSelected) {
                                    Text(
                                        stringResource(R.string.store_selector_selected),
                                        color = MaterialTheme.colorScheme.primary,
                                        style = MaterialTheme.typography.labelLarge,
                                        fontWeight = FontWeight.Bold,
                                        maxLines = 1,
                                    )
                                }
                            }
                        }
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .clickable { rememberDefaultStore = !rememberDefaultStore }
                        .padding(vertical = 2.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Checkbox(
                        checked = rememberDefaultStore,
                        onCheckedChange = { rememberDefaultStore = it },
                    )
                    Text(
                        stringResource(R.string.store_selector_default_checkbox),
                        color = TextPrimary,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.weight(1f),
                    )
                }

                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    TextButton(onClick = onDismiss, modifier = Modifier.weight(0.85f)) {
                        Text(stringResource(R.string.action_cancel))
                    }
                    Button(
                        onClick = {
                            val variant = selectedVariant ?: return@Button
                            if (rememberDefaultStore || defaultVariantId != null) {
                                onSetDefaultStore(game.id, if (rememberDefaultStore) variant.id else null)
                            }
                            if (rememberDefaultStore) {
                                Toast.makeText(context, context.getString(R.string.store_selector_long_press_tip), Toast.LENGTH_LONG).show()
                            }
                            onLaunch(game, variant)
                        },
                        enabled = selectedVariant != null,
                        modifier = Modifier
                            .weight(1.15f)
                            .focusRequester(continueFocusRequester),
                    ) {
                        Text(stringResource(R.string.action_continue), maxLines = 1, overflow = TextOverflow.Ellipsis)
                    }
                }
            }
        }
    }
}

@Composable
internal fun SortPicker(options: List<CatalogSortOption>, selected: String, onSelect: (String) -> Unit, modifier: Modifier = Modifier) {
    val labels = options.ifEmpty { listOf(CatalogSortOption("relevance", "Relevance", "")) }
    val selectedLabel = labels.firstOrNull { it.id == selected }?.label ?: labels.first().label
    var expanded by remember { mutableStateOf(false) }
    val controlShape = RoundedCornerShape(999.dp)
    val controlColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.46f)
    Box(modifier) {
        OutlinedButton(
            onClick = { expanded = true },
            modifier = Modifier.fillMaxWidth().height(40.dp),
            shape = controlShape,
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = controlColor,
                contentColor = TextPrimary,
            ),
            contentPadding = PaddingValues(horizontal = 12.dp),
        ) {
            Text("Sort: $selectedLabel", maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            labels.forEach { option ->
                DropdownMenuItem(
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(if (option.id == selected) "✓" else "", modifier = Modifier.width(24.dp))
                            Text(option.label)
                        }
                    },
                    onClick = {
                        expanded = false
                        onSelect(option.id)
                    },
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun SelectedFilterChips(options: List<CatalogFilterOption>, selectedIds: List<String>, onToggle: (String) -> Unit) {
    val selectedOptions = options.filter { it.id in selectedIds }
    if (selectedOptions.isEmpty()) return
    FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
        selectedOptions.take(4).forEach { option ->
            AssistChip(onClick = { onToggle(option.id) }, label = { Text(option.label, maxLines = 1, overflow = TextOverflow.Ellipsis) })
        }
        if (selectedOptions.size > 4) {
            AssistChip(onClick = {}, label = { Text("+${selectedOptions.size - 4}") })
        }
    }
}

internal fun catalogVisibleFilterGroups(groups: List<CatalogFilterGroup>): List<CatalogFilterGroup> =
    groups.filter { it.id in setOf("digital_store", "genre", "subscriptions") }

internal fun catalogFilterOptions(groups: List<CatalogFilterGroup>): List<CatalogFilterOption> =
    groups.flatMap { group -> group.options.take(if (group.id == "genre") 10 else group.options.size) }

@Composable
internal fun FilterMenu(options: List<CatalogFilterOption>, selectedIds: List<String>, onToggle: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val filterControlShape = RoundedCornerShape(999.dp)
    val filterControlColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.46f)
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box {
            OutlinedButton(
                onClick = { expanded = true },
                modifier = Modifier.height(40.dp),
                shape = filterControlShape,
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = filterControlColor,
                    contentColor = TextPrimary,
                ),
                contentPadding = PaddingValues(horizontal = 12.dp),
            ) {
                Text(if (selectedIds.isEmpty()) "Filters" else "Filters (${selectedIds.size})", maxLines = 1)
            }
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(if (option.id in selectedIds) "✓" else "", modifier = Modifier.width(24.dp))
                                Text(option.label)
                            }
                        },
                        onClick = { onToggle(option.id) },
                    )
                }
            }
        }
        Box(
            Modifier
                .height(40.dp)
                .clip(filterControlShape)
                .background(filterControlColor)
                .padding(horizontal = 12.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                "${options.size} available",
                color = TextMuted,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 1,
            )
        }
    }
}
