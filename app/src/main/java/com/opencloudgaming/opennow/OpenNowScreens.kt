package com.opencloudgaming.opennow

import android.app.Activity
import android.content.Intent
import android.graphics.Color as AndroidColor
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.speech.RecognizerIntent
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.PointerIcon
import android.view.Surface
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items as gridItems
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.key
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale
import java.net.URL
import kotlin.math.min
import kotlin.math.floor
import kotlin.math.roundToInt
import kotlin.math.sqrt
import com.opencloudgaming.opennow.ui.theme.OpenNowTheme
import com.opencloudgaming.opennow.ui.theme.Background
import com.opencloudgaming.opennow.ui.theme.Panel
import com.opencloudgaming.opennow.ui.theme.PanelAlt
import com.opencloudgaming.opennow.ui.theme.TextPrimary
import com.opencloudgaming.opennow.ui.theme.TextMuted
import com.opencloudgaming.opennow.ui.theme.SettingsBackground
import com.opencloudgaming.opennow.ui.theme.color
import com.opencloudgaming.opennow.ui.theme.PhoneNavRailMaxSmallestWidth
import com.opencloudgaming.opennow.ui.theme.Green
import com.opencloudgaming.opennow.ui.components.UrlImage
import com.opencloudgaming.opennow.ui.screens.home.AnimatedLaunchOverlay
import com.opencloudgaming.opennow.ui.screens.home.GameDetailsSheet
import com.opencloudgaming.opennow.ui.screens.home.GameGrid
import com.opencloudgaming.opennow.ui.screens.home.HomeScreen
import com.opencloudgaming.opennow.ui.screens.home.SearchEmptyState
import com.opencloudgaming.opennow.ui.screens.home.StoreLaunchSelector
import com.opencloudgaming.opennow.ui.screens.home.SwipeToRefreshContainer
import com.opencloudgaming.opennow.ui.screens.home.gameMatchesSearch
import com.opencloudgaming.opennow.ui.screens.settings.SettingsScreen

@Composable
fun OpenNowApp(viewModel: OpenNowViewModel) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    OpenNowTheme(state.settings) {
        Surface(Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            val startingText = stringResource(R.string.status_starting_opennow)
            when {
                state.initializing -> LoadingScreen(state.launchPhase.ifBlank { startingText })
                state.authSession == null -> LoginScreen(state, viewModel)
                else -> MainShell(state, viewModel)
            }
        }
    }
}

@Composable
internal fun LoadingScreen(text: String) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(16.dp)) {
            OpenNowMark(72.dp)
            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            Text(text, color = TextMuted)
        }
    }
}

internal fun isPhoneLandscape(width: androidx.compose.ui.unit.Dp, height: androidx.compose.ui.unit.Dp): Boolean =
    width > height && minOf(width, height) < PhoneNavRailMaxSmallestWidth

internal fun isPhonePortrait(width: androidx.compose.ui.unit.Dp, height: androidx.compose.ui.unit.Dp): Boolean =
    height >= width && minOf(width, height) < PhoneNavRailMaxSmallestWidth

@Composable
private fun MainShell(state: OpenNowUiState, viewModel: OpenNowViewModel) {
    val inStream = state.page == AppPage.Stream
    val streamingActive = inStream && state.streamStatus != "idle"
    val modalPickerOpen = state.pendingPrintedWasteGame != null || state.pendingStoreChoiceGame != null
    val tvProfile = state.codecReport?.androidTvProfile == true
    BackHandler(enabled = state.selectedGame != null && !inStream) {
        viewModel.clearSelectedGame()
    }
    BackHandler(enabled = tvProfile && !inStream && state.selectedGame == null && state.page != AppPage.Home) {
        viewModel.setPage(AppPage.Home)
    }
    BoxWithConstraints(Modifier.fillMaxSize()) {
        var phoneLandscapeScrollChromeHidden by remember { mutableStateOf(false) }
        val phoneLandscapeChrome = !tvProfile && !inStream && isPhoneLandscape(maxWidth, maxHeight)
        val showNavigationRail = !inStream && (tvProfile || phoneLandscapeChrome)
        val scrollChromePage = state.page == AppPage.Home || state.page == AppPage.Library
        LaunchedEffect(phoneLandscapeChrome, scrollChromePage) {
            if (!phoneLandscapeChrome || !scrollChromePage) {
                phoneLandscapeScrollChromeHidden = false
            }
        }

        Scaffold(
            contentWindowInsets = if (streamingActive) WindowInsets(0, 0, 0, 0) else ScaffoldDefaults.contentWindowInsets,
            bottomBar = {
                if (!inStream && !showNavigationRail) {
                    NavigationBar(
                        containerColor = if (state.page == AppPage.Settings) SettingsBackground else MaterialTheme.colorScheme.background,
                        tonalElevation = 0.dp,
                    ) {
                        BottomNavItem(
                            selected = state.page == AppPage.Home,
                            onClick = { viewModel.setPage(AppPage.Home) },
                            iconRes = R.drawable.ic_tab_store,
                            label = stringResource(R.string.nav_store),
                        )
                        BottomNavItem(
                            selected = state.page == AppPage.Library,
                            onClick = { viewModel.setPage(AppPage.Library) },
                            iconRes = R.drawable.ic_tab_library,
                            label = stringResource(R.string.nav_library),
                        )
                        BottomNavItem(
                            selected = state.page == AppPage.Settings,
                            onClick = { viewModel.setPage(AppPage.Settings) },
                            iconRes = R.drawable.ic_tab_settings,
                            label = stringResource(R.string.nav_settings),
                        )
                    }
                }
            },
        ) { padding ->
            Box(Modifier.fillMaxSize().padding(padding)) {
                Row(Modifier.fillMaxSize()) {
                    if (showNavigationRail) {
                        AppNavigationRail(state = state, viewModel = viewModel)
                    }
                    Column(Modifier.fillMaxSize()) {
                        AnimatedVisibility(visible = !inStream && !(phoneLandscapeChrome && scrollChromePage && phoneLandscapeScrollChromeHidden)) {
                            if (!inStream) {
                                TopStatusBar(
                                    state = state,
                                    onResumeActiveSession = viewModel::resumeActiveSession,
                                    landscapeSearchQuery = when {
                                        phoneLandscapeChrome && state.page == AppPage.Home -> state.catalogSearch
                                        phoneLandscapeChrome && state.page == AppPage.Library -> state.librarySearch
                                        else -> null
                                    },
                                    onLandscapeSearchChange = when {
                                        phoneLandscapeChrome && state.page == AppPage.Home -> viewModel::setCatalogSearch
                                        phoneLandscapeChrome && state.page == AppPage.Library -> viewModel::setLibrarySearch
                                        else -> null
                                    },
                                    landscapeSearchPlaceholder = when (state.page) {
                                        AppPage.Library -> "Search library"
                                        else -> stringResource(R.string.search_games)
                                    },
                                    landscapeSearchBusy = state.page == AppPage.Home && state.loadingGames && state.catalogSearch.isNotBlank(),
                                )
                            }
                        }
                        Box(Modifier.fillMaxSize()) {
                            when (state.page) {
                                AppPage.Home -> HomeScreen(
                                    state = state,
                                    viewModel = viewModel,
                                    tvProfile = tvProfile,
                                    hideChromeWhenScrolled = phoneLandscapeChrome,
                                    searchInTopBar = phoneLandscapeChrome,
                                    onScrollChromeHiddenChange = { phoneLandscapeScrollChromeHidden = it },
                                )
                                AppPage.Library -> LibraryScreen(
                                    state = state,
                                    viewModel = viewModel,
                                    tvProfile = tvProfile,
                                    hideChromeWhenScrolled = phoneLandscapeChrome,
                                    searchInTopBar = phoneLandscapeChrome,
                                    onScrollChromeHiddenChange = { phoneLandscapeScrollChromeHidden = it },
                                )
                                AppPage.Settings -> SettingsScreen(state, viewModel, tvProfile)
                                AppPage.Stream -> StreamScreen(state, viewModel)
                            }
                        }
                    }
                }
                AnimatedVisibility(
                    visible = state.selectedGame != null && !inStream && !modalPickerOpen,
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
                state.pendingPrintedWasteGame?.let { game ->
                    AnimatedLaunchOverlay(Modifier.align(Alignment.Center)) {
                        PrintedWasteSelector(state, game, viewModel)
                    }
                }
                state.pendingStoreChoiceGame?.let { game ->
                    AnimatedLaunchOverlay(Modifier.align(Alignment.Center)) {
                        StoreLaunchSelector(
                            game = game,
                            defaultVariantId = state.settings.defaultGameVariantIds[game.id],
                            onLaunch = viewModel::playVariant,
                            onSetDefaultStore = viewModel::setDefaultGameVariant,
                            onDismiss = viewModel::dismissStoreChoice,
                        )
                    }
                }
                if (state.streamLaunchMinimized && shouldShowQueueLaunchStatus(state)) {
                    MinimizedQueuePill(
                        state = state,
                        onRestore = viewModel::restoreStreamLaunch,
                        onCancel = viewModel::stopStream,
                        modifier = Modifier.align(Alignment.BottomCenter),
                    )
                }
            }
        }
    }
}

@Composable
private fun AppNavigationRail(state: OpenNowUiState, viewModel: OpenNowViewModel) {
    NavigationRail(
        modifier = Modifier.fillMaxHeight(),
        containerColor = if (state.page == AppPage.Settings) SettingsBackground else MaterialTheme.colorScheme.background,
    ) {
        Spacer(Modifier.height(8.dp))
        AppNavigationRailItem(
            selected = state.page == AppPage.Home,
            onClick = { viewModel.setPage(AppPage.Home) },
            iconRes = R.drawable.ic_tab_store,
            label = stringResource(R.string.nav_store),
        )
        AppNavigationRailItem(
            selected = state.page == AppPage.Library,
            onClick = { viewModel.setPage(AppPage.Library) },
            iconRes = R.drawable.ic_tab_library,
            label = stringResource(R.string.nav_library),
        )
        AppNavigationRailItem(
            selected = state.page == AppPage.Settings,
            onClick = { viewModel.setPage(AppPage.Settings) },
            iconRes = R.drawable.ic_tab_settings,
            label = stringResource(R.string.nav_settings),
        )
    }
}

@Composable
private fun AppNavigationRailItem(selected: Boolean, onClick: () -> Unit, iconRes: Int, label: String) {
    NavigationRailItem(
        selected = selected,
        onClick = onClick,
        icon = {
            Icon(
                painter = painterResource(iconRes),
                contentDescription = label,
                modifier = Modifier.size(24.dp),
            )
        },
        label = { Text(label, maxLines = 1, overflow = TextOverflow.Ellipsis) },
    )
}

@Composable
private fun RowScope.BottomNavItem(selected: Boolean, onClick: () -> Unit, iconRes: Int, label: String) {
    NavigationBarItem(
        selected = selected,
        onClick = onClick,
        icon = {
            Icon(
                painter = painterResource(iconRes),
                contentDescription = null,
                modifier = Modifier.size(24.dp),
            )
        },
        label = { Text(label, maxLines = 1, overflow = TextOverflow.Ellipsis) },
    )
}

@Composable
internal fun TopStatusBar(
    state: OpenNowUiState,
    onResumeActiveSession: () -> Unit,
    landscapeSearchQuery: String? = null,
    onLandscapeSearchChange: ((String) -> Unit)? = null,
    landscapeSearchPlaceholder: String = "",
    landscapeSearchBusy: Boolean = false,
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.background,
        tonalElevation = 0.dp,
        shadowElevation = 0.dp,
    ) {
        Row(
            Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            OpenNowMark(34.dp)
            Spacer(Modifier.width(10.dp))
            Column(Modifier.weight(1f)) {
                Text(state.authSession?.user?.displayName ?: "OpenNOW", fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                val tier = state.subscriptionInfo?.membershipTier ?: state.authSession?.user?.membershipTier ?: "GFN"
                Text("$tier ${state.activeSession?.let { "  Active session ${it.sessionId.take(8)}" } ?: ""}", color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodySmall)
            }
            if (landscapeSearchQuery != null && onLandscapeSearchChange != null) {
                Spacer(Modifier.width(10.dp))
                NativeSearchField(
                    query = landscapeSearchQuery,
                    onQueryChange = onLandscapeSearchChange,
                    placeholder = landscapeSearchPlaceholder,
                    searching = landscapeSearchBusy,
                    modifier = Modifier.width(300.dp),
                )
            }
            if (state.activeSession != null) {
                Spacer(Modifier.width(6.dp))
                ElevatedButton(onClick = onResumeActiveSession) {
                    Text(stringResource(R.string.action_resume))
                }
            }
        }
    }
}

@Composable
internal fun NativeSearchField(
    query: String,
    onQueryChange: (String) -> Unit,
    placeholder: String,
    searching: Boolean = false,
    modifier: Modifier = Modifier,
    focusRequester: FocusRequester? = null,
    onOpen: (() -> Unit)? = null,
) {
    val focusManager = LocalFocusManager.current
    val speechLauncher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val spoken = result.data
                ?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                ?.firstOrNull()
            if (!spoken.isNullOrBlank()) onQueryChange(spoken)
        }
    }
    val voiceSearchIntent = remember(placeholder) {
        Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            .putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            .putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
            .putExtra(RecognizerIntent.EXTRA_PROMPT, placeholder)
    }
    Surface(
        modifier = modifier.height(56.dp),
        shape = RoundedCornerShape(28.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.72f),
        tonalElevation = 2.dp,
    ) {
        Row(
            Modifier
                .fillMaxSize()
                .padding(start = 18.dp, end = if (query.isBlank()) 18.dp else 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_search),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(22.dp),
            )
            BasicTextField(
                value = query,
                onValueChange = onQueryChange,
                singleLine = true,
                textStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurface),
                cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                modifier = Modifier
                    .weight(1f)
                    .then(focusRequester?.let { Modifier.focusRequester(it) } ?: Modifier)
                    .onFocusChanged { if (it.isFocused) onOpen?.invoke() }
                    .onPreviewKeyEvent { handleDpadFocusMove(it, focusManager) },
                decorationBox = { innerTextField ->
                    Box(Modifier.fillMaxWidth()) {
                        if (query.isBlank()) {
                            Text(
                                placeholder,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                            )
                        }
                        innerTextField()
                    }
                },
            )
            if (query.isNotBlank()) {
                if (searching) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(18.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.primary,
                    )
                }
                IconButton(onClick = { onQueryChange("") }) {
                    Icon(
                        painter = painterResource(R.drawable.ic_clear),
                        contentDescription = stringResource(R.string.search_clear),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(26.dp),
                    )
                }
            } else {
                if (searching) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(18.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.primary,
                    )
                }
                IconButton(onClick = { runCatching { speechLauncher.launch(voiceSearchIntent) } }) {
                    Icon(
                        painter = painterResource(R.drawable.ic_mic),
                        contentDescription = stringResource(R.string.search_voice),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(22.dp),
                    )
                }
            }
        }
    }
}

internal fun handleDpadFocusMove(event: androidx.compose.ui.input.key.KeyEvent, focusManager: FocusManager): Boolean {
    if (event.type != KeyEventType.KeyDown) return false
    val direction = when (event.key) {
        Key.DirectionUp -> FocusDirection.Up
        Key.DirectionDown -> FocusDirection.Down
        Key.DirectionLeft -> FocusDirection.Left
        Key.DirectionRight -> FocusDirection.Right
        else -> return false
    }
    return focusManager.moveFocus(direction)
}

internal fun handleVerticalDpadFocusMove(event: androidx.compose.ui.input.key.KeyEvent, focusManager: FocusManager): Boolean {
    if (event.type != KeyEventType.KeyDown) return false
    val direction = when (event.key) {
        Key.DirectionUp -> FocusDirection.Up
        Key.DirectionDown -> FocusDirection.Down
        else -> return false
    }
    focusManager.moveFocus(direction)
    return true
}

internal fun isTvActivateKey(event: androidx.compose.ui.input.key.KeyEvent): Boolean =
    event.type == KeyEventType.KeyUp &&
        event.key in setOf(
            Key.DirectionCenter,
            Key.Enter,
            Key.NumPadEnter,
        )

@Composable
internal fun StreamScreen(state: OpenNowUiState, viewModel: OpenNowViewModel) {
    val context = LocalContext.current
    val activity = context as? Activity
    val session = state.streamSession
    val game = state.streamGame
    var streamState by remember { mutableStateOf("Preparing") }
    var controlsOpen by remember { mutableStateOf(false) }
    var exitConfirmOpen by remember { mutableStateOf(false) }
    var keyboardOpen by remember { mutableStateOf(false) }
    var keyboardText by remember { mutableStateOf("") }
    var audioMuted by remember { mutableStateOf(false) }
    var touchLayoutEditing by remember { mutableStateOf(false) }
    var statsVisible by remember(state.settings.showStatsOnLaunch) { mutableStateOf(state.settings.showStatsOnLaunch) }
    var streamStats by remember { mutableStateOf(StreamRuntimeStats()) }
    val streamReady = session?.status in setOf(2, 3)
    val launchStreamSettings = state.activeStreamSettings ?: state.settings.stream
    val streamSettings = launchStreamSettings.copy(
        mouseSensitivity = state.settings.stream.mouseSensitivity,
        mouseAcceleration = state.settings.stream.mouseAcceleration,
        streamSharpeningEnabled = state.settings.stream.streamSharpeningEnabled,
        streamSharpeningAmount = state.settings.stream.streamSharpeningAmount,
        streamClarityEnabled = state.settings.stream.streamClarityEnabled,
        streamClarityAmount = state.settings.stream.streamClarityAmount,
        streamContrastEnabled = state.settings.stream.streamContrastEnabled,
        streamContrastAmount = state.settings.stream.streamContrastAmount,
    )
    val streamOverlayOpen = controlsOpen || exitConfirmOpen || keyboardOpen
    val externalMousePassthroughActive = streamReady && !streamOverlayOpen
    val handleStreamBack = {
        when {
            exitConfirmOpen -> exitConfirmOpen = false
            keyboardOpen -> keyboardOpen = false
            controlsOpen -> controlsOpen = false
            else -> controlsOpen = true
        }
    }
    BackHandler(enabled = streamReady) {
        handleStreamBack()
    }
    val client = remember {
        NativeStreamClient(
            context = context.applicationContext,
            onState = {
                streamState = it
                if (it == "Streaming") viewModel.markStreamConnected()
            },
            onError = {
                streamState = it
                viewModel.markStreamError(it)
            },
            onSafeVideoFallbackRequired = {
                streamState = it
                viewModel.restartStreamWithSafeVideoProfile(it)
            },
            onStats = { streamStats = it },
        )
    }

    DisposableEffect(Unit) {
        val window = activity?.window
        val decor = activity?.window?.decorView
        val oldStatusBarColor = window?.statusBarColor
        val oldNavigationBarColor = window?.navigationBarColor
        window?.statusBarColor = AndroidColor.BLACK
        window?.navigationBarColor = AndroidColor.BLACK
        NativeStreamInputRouter.attach(client)
        onDispose {
            if (oldStatusBarColor != null) window.statusBarColor = oldStatusBarColor
            if (oldNavigationBarColor != null) window.navigationBarColor = oldNavigationBarColor
            if (Build.VERSION.SDK_INT >= 26) {
                decor?.releasePointerCapture()
            }
            NativeStreamInputRouter.clearUiTouchPassthroughBounds()
            NativeStreamInputRouter.clearStreamPanelTouchPassthroughBounds()
            NativeStreamInputRouter.setSystemMenuHandler(null)
            NativeStreamInputRouter.setSystemBackHandler(null)
            NativeStreamInputRouter.setStreamUiActive(false)
            NativeStreamInputRouter.detach(client)
            client.release()
        }
    }

    LaunchedEffect(streamReady, streamOverlayOpen) {
        NativeStreamInputRouter.setStreamUiActive(streamReady && streamOverlayOpen)
        NativeStreamInputRouter.setSystemMenuHandler {
            keyboardOpen = false
            exitConfirmOpen = false
            controlsOpen = true
        }
        NativeStreamInputRouter.setSystemBackHandler {
            handleStreamBack()
        }
    }

    LaunchedEffect(streamReady, state.settings.androidTouch.mousePad) {
        NativeStreamInputRouter.setTouchMouseEnabled(streamReady && state.settings.androidTouch.mousePad)
    }
    LaunchedEffect(streamReady, state.settings.androidTouch.mousePad, controlsOpen, exitConfirmOpen, keyboardOpen, state.settings.androidTouch.enabled) {
        NativeStreamInputRouter.setCaptureAllTouch(
            streamReady &&
                state.settings.androidTouch.mousePad &&
                !controlsOpen &&
                !exitConfirmOpen &&
                !keyboardOpen,
        )
    }
    DisposableEffect(Unit) {
        onDispose {
            NativeStreamInputRouter.setCaptureAllTouch(false)
        }
    }
    LaunchedEffect(state.settings.phoneRumbleFallback) {
        client.updateHapticsSettings(state.settings.phoneRumbleFallback)
    }
    LaunchedEffect(session?.sessionId, session?.status, launchStreamSettings) {
        if (session != null && streamReady) {
            client.start(session, launchStreamSettings)
        }
    }

    Box(Modifier.fillMaxSize().background(Color.Black)) {
        if (session == null && state.streamStatus != "idle") {
            QueueLoadingScreen(state, viewModel)
        } else if (session == null) {
            NoActiveStreamScreen(
                canResumeSession = state.activeSession != null,
                canEndSession = state.authSession != null,
                onBack = { viewModel.setPage(AppPage.Home) },
                onResumeSession = viewModel::resumeActiveSession,
                onEndSession = viewModel::stopStream,
            )
        } else if (!streamReady) {
            QueueLoadingScreen(state, viewModel)
        } else {
            StreamVideoSurface(
                client = client,
                settings = streamSettings,
                hideExternalMousePointer = externalMousePassthroughActive,
                touchMouseEnabled = state.settings.androidTouch.mousePad,
                externalMouseRoot = activity?.window?.decorView,
                onMouseCaptureInput = { (activity as? MainActivity)?.enforceStreamSystemUiFromInput() },
            )
            if (statsVisible) {
                StreamStatsPill(
                    gameTitle = game?.title ?: "Stream",
                    status = streamState,
                    streamStats = streamStats,
                    audioMuted = audioMuted,
                    style = state.settings.streamStatsStyle,
                    modifier = Modifier.align(Alignment.TopStart),
                )
            }
            if (state.settings.androidTouch.enabled) {
                TouchOverlay(
                    client = client,
                    touch = state.settings.androidTouch,
                    layoutEditing = touchLayoutEditing,
                    onLeftOffsetChange = { x, y ->
                        viewModel.updateSettings(
                            state.settings.copy(
                                androidTouch = state.settings.androidTouch.copy(leftOffsetXDp = x, leftOffsetYDp = y),
                            ),
                        )
                    },
                    onRightOffsetChange = { x, y ->
                        viewModel.updateSettings(
                            state.settings.copy(
                                androidTouch = state.settings.androidTouch.copy(rightOffsetXDp = x, rightOffsetYDp = y),
                            ),
                        )
                    },
                    modifier = Modifier.align(Alignment.BottomCenter),
                )
            }
            AnimatedVisibility(
                visible = controlsOpen,
                enter = fadeIn() + slideInVertically(initialOffsetY = { it / 4 }) + scaleIn(initialScale = 0.96f),
                exit = fadeOut() + slideOutVertically(targetOffsetY = { it / 4 }) + scaleOut(targetScale = 0.96f),
                modifier = Modifier.align(Alignment.BottomEnd),
            ) {
                StreamControlsPanel(
                    gameTitle = game?.title ?: "Stream",
                    status = (state.queuePosition?.let { "Queue $it" } ?: streamState).takeUnless(::shouldHideStreamStatusText),
                    settings = state.settings,
                    audioMuted = audioMuted,
                    statsVisible = statsVisible,
                    touchLayoutEditing = touchLayoutEditing,
                    keyboardOpen = keyboardOpen,
                    onAudioToggle = {
                        audioMuted = !audioMuted
                        client.setAudioMuted(audioMuted)
                    },
                    onStatsToggle = {
                        statsVisible = !statsVisible
                        viewModel.updateSettings(state.settings.copy(showStatsOnLaunch = statsVisible))
                    },
                    onStatsStyleCycle = {
                        viewModel.updateSettings(state.settings.copy(streamStatsStyle = state.settings.streamStatsStyle.next()))
                    },
                    onPhoneRumbleFallbackToggle = {
                        viewModel.updateSettings(state.settings.copy(phoneRumbleFallback = !state.settings.phoneRumbleFallback))
                    },
                    onTouchLayoutEditingToggle = {
                        touchLayoutEditing = !touchLayoutEditing
                    },
                    onKeyboardToggle = { keyboardOpen = !keyboardOpen },
                    onEsc = { client.sendKeyCode(KeyEvent.KEYCODE_ESCAPE) },
                    onEnter = { client.sendKeyCode(KeyEvent.KEYCODE_ENTER) },
                    onBackspace = { client.sendKeyCode(KeyEvent.KEYCODE_DEL) },
                    onExit = {
                        controlsOpen = false
                        exitConfirmOpen = true
                    },
                    onTouchControlsToggle = {
                        viewModel.updateSettings(
                            state.settings.copy(
                                androidTouch = state.settings.androidTouch.copy(enabled = !state.settings.androidTouch.enabled),
                            ),
                        )
                    },
                    onMousePadToggle = {
                        viewModel.updateSettings(
                            state.settings.copy(
                                androidTouch = state.settings.androidTouch.copy(mousePad = !state.settings.androidTouch.mousePad),
                            ),
                        )
                    },
                    onSharpeningToggle = {
                        viewModel.updateStreamSettings { settings ->
                            settings.copy(streamSharpeningEnabled = !settings.streamSharpeningEnabled)
                        }
                    },
                    onSharpeningAmountChange = { value ->
                        viewModel.updateStreamSettings { settings ->
                            settings.copy(streamSharpeningAmount = value)
                        }
                    },
                    onClarityToggle = {
                        viewModel.updateStreamSettings { settings ->
                            settings.copy(streamClarityEnabled = !settings.streamClarityEnabled)
                        }
                    },
                    onClarityAmountChange = { value ->
                        viewModel.updateStreamSettings { settings ->
                            settings.copy(streamClarityAmount = value)
                        }
                    },
                    onContrastToggle = {
                        viewModel.updateStreamSettings { settings ->
                            settings.copy(streamContrastEnabled = !settings.streamContrastEnabled)
                        }
                    },
                    onContrastAmountChange = { value ->
                        viewModel.updateStreamSettings { settings ->
                            settings.copy(streamContrastAmount = value)
                        }
                    },
                    onTouchScaleChange = { value ->
                        viewModel.updateSettings(state.settings.copy(androidTouch = state.settings.androidTouch.copy(scale = value)))
                    },
                    onButtonScaleChange = { value ->
                        viewModel.updateSettings(state.settings.copy(androidTouch = state.settings.androidTouch.copy(buttonScale = value)))
                    },
                    onStickScaleChange = { value ->
                        viewModel.updateSettings(state.settings.copy(androidTouch = state.settings.androidTouch.copy(stickScale = value)))
                    },
                    onOpacityChange = { value ->
                        viewModel.updateSettings(state.settings.copy(androidTouch = state.settings.androidTouch.copy(opacity = value)))
                    },
                    onTouchEdgePaddingChange = { value ->
                        viewModel.updateSettings(state.settings.copy(androidTouch = state.settings.androidTouch.copy(edgePaddingDp = value)))
                    },
                    onTouchBottomPaddingChange = { value ->
                        viewModel.updateSettings(state.settings.copy(androidTouch = state.settings.androidTouch.copy(bottomPaddingDp = value)))
                    },
                    onTouchLeftOffsetChange = { value ->
                        viewModel.updateSettings(state.settings.copy(androidTouch = state.settings.androidTouch.copy(leftOffsetYDp = value)))
                    },
                    onTouchRightOffsetChange = { value ->
                        viewModel.updateSettings(state.settings.copy(androidTouch = state.settings.androidTouch.copy(rightOffsetYDp = value)))
                    },
                    onClose = { controlsOpen = false },
                )
            }
            if (keyboardOpen) {
                AnimatedLaunchOverlay(Modifier.align(Alignment.BottomCenter)) {
                    StreamKeyboardBar(
                        text = keyboardText,
                        onTextChange = { keyboardText = it },
                        onSend = {
                            client.sendText(keyboardText)
                            keyboardText = ""
                        },
                        onBackspace = { client.sendKeyCode(KeyEvent.KEYCODE_DEL) },
                        onEnter = { client.sendKeyCode(KeyEvent.KEYCODE_ENTER) },
                        onEsc = { client.sendKeyCode(KeyEvent.KEYCODE_ESCAPE) },
                        onDone = { keyboardOpen = false },
                    )
                }
            }
            if (exitConfirmOpen) {
                AnimatedLaunchOverlay(Modifier.align(Alignment.Center)) {
                    StreamExitConfirmation(
                        gameTitle = game?.title ?: "this game",
                        onKeepPlaying = { exitConfirmOpen = false },
                        onExit = {
                            exitConfirmOpen = false
                            viewModel.stopStream()
                        },
                    )
                }
            }
        }
    }
}

@Composable
private fun StreamVideoSurface(
    client: NativeStreamClient,
    settings: StreamSettings,
    hideExternalMousePointer: Boolean,
    touchMouseEnabled: Boolean,
    externalMouseRoot: android.view.View?,
    onMouseCaptureInput: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val rootView = LocalView.current
    val pointerRootView = externalMouseRoot ?: rootView
    val (streamWidth, streamHeight) = streamResolutionPixels(settings)
    val streamAspect = (streamWidth.toFloat() / streamHeight.toFloat()).takeIf { it.isFinite() && it > 0f } ?: (16f / 9f)
    val currentStreamFps by rememberUpdatedState(settings.fps)
    val currentOnMouseCaptureInput by rememberUpdatedState(onMouseCaptureInput)
    var zoomScale by remember { mutableFloatStateOf(1f) }
    var zoomOffset by remember { mutableStateOf(Offset.Zero) }
    var viewportSize by remember { mutableStateOf(IntSize.Zero) }
    DisposableEffect(client, rootView, pointerRootView, hideExternalMousePointer) {
        pointerRootView.configureAndroidMousePointerCapture(hideExternalMousePointer, { currentOnMouseCaptureInput() }) { event ->
            client.dispatchMotion(event)
        }
        if (hideExternalMousePointer) {
            pointerRootView.hideAndroidPointerTree()
        } else {
            pointerRootView.showAndroidPointerTree()
        }
        onDispose {
            pointerRootView.clearAndroidMousePointerCapture()
            pointerRootView.showAndroidPointerTree()
        }
    }
    BoxWithConstraints(
        modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center,
    ) {
        val containerAspect = if (maxHeight.value > 0f) maxWidth.value / maxHeight.value else streamAspect
        val viewportModifier = if (containerAspect > streamAspect) {
            Modifier
                .fillMaxHeight()
                .aspectRatio(streamAspect)
        } else {
            Modifier
                .fillMaxWidth()
                .aspectRatio(streamAspect)
        }
        Box(
            viewportModifier
                .onSizeChanged {
                    viewportSize = it
                    zoomOffset = clampStreamZoomOffset(zoomOffset, zoomScale, it)
                }
                .clipToBounds()
                .background(Color.Black),
        ) {
            Box(
                Modifier
                    .matchParentSize()
                    .graphicsLayer {
                        scaleX = zoomScale
                        scaleY = zoomScale
                        translationX = zoomOffset.x
                        translationY = zoomOffset.y
                    },
            ) {
                key(settings.streamSharpeningEnabled, settings.streamClarityEnabled, settings.streamContrastEnabled) {
                    AndroidView(
                        modifier = Modifier.fillMaxSize(),
                        factory = { ctx ->
                            client.createRenderer(ctx, settings).apply {
                                isFocusable = false
                                isFocusableInTouchMode = false
                                hideAndroidPointerTree()
                                setPreferredStreamFrameRate(settings.fps)
                                holder.addCallback(
                                    object : SurfaceHolder.Callback {
                                        override fun surfaceCreated(holder: SurfaceHolder) {
                                            holder.surface.setPreferredStreamFrameRate(currentStreamFps)
                                        }

                                        override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
                                            holder.surface.setPreferredStreamFrameRate(currentStreamFps)
                                        }

                                        override fun surfaceDestroyed(holder: SurfaceHolder) = Unit
                                    },
                                )
                            }
                        },
                        update = { renderer ->
                            client.updateRendererSettings(settings)
                            renderer.isFocusable = false
                            renderer.isFocusableInTouchMode = false
                            renderer.setPreferredStreamFrameRate(settings.fps)
                            pointerRootView.configureAndroidMousePointerCapture(hideExternalMousePointer, { currentOnMouseCaptureInput() }) { event ->
                                client.dispatchMotion(event)
                            }
                            if (hideExternalMousePointer) {
                                pointerRootView.hideAndroidPointerTree()
                                renderer.hideAndroidPointerTree()
                            } else {
                                pointerRootView.showAndroidPointerTree()
                                renderer.showAndroidPointerTree()
                            }
                            renderer.setOnKeyListener(null)
                            renderer.setOnGenericMotionListener { _, event ->
                                if (hideExternalMousePointer) pointerRootView.hideAndroidPointerTree()
                                client.dispatchMotion(event)
                            }
                            renderer.setOnTouchListener { view, event ->
                                NativeStreamInputRouter.dispatchTouch(event, view.width, view.height)
                            }
                        },
                    )
                }
            }
            FingerMouseInputLayer(
                enabled = touchMouseEnabled,
                onZoomGesture = { scaleChange, pan ->
                    val nextScale = (zoomScale * scaleChange).coerceIn(1f, 3f)
                    zoomScale = nextScale
                    zoomOffset = if (nextScale <= 1.001f) {
                        Offset.Zero
                    } else {
                        clampStreamZoomOffset(zoomOffset + pan, nextScale, viewportSize)
                    }
                },
                modifier = Modifier.matchParentSize(),
            )
        }
    }
}

private fun clampStreamZoomOffset(offset: Offset, zoomScale: Float, viewportSize: IntSize): Offset {
    if (zoomScale <= 1.001f || viewportSize.width <= 0 || viewportSize.height <= 0) return Offset.Zero
    val maxX = viewportSize.width * (zoomScale - 1f) / 2f
    val maxY = viewportSize.height * (zoomScale - 1f) / 2f
    return Offset(
        x = offset.x.coerceIn(-maxX, maxX),
        y = offset.y.coerceIn(-maxY, maxY),
    )
}

private fun SurfaceView.setPreferredStreamFrameRate(fps: Int) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) return
    holder.surface.setPreferredStreamFrameRate(fps)
}

private fun Surface.setPreferredStreamFrameRate(fps: Int) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R || !isValid) return
    setFrameRate(normalizedStreamDisplayFps(fps), Surface.FRAME_RATE_COMPATIBILITY_FIXED_SOURCE)
}

private fun androidNullPointerIcon(view: android.view.View): PointerIcon? =
    if (Build.VERSION.SDK_INT >= 24) {
        PointerIcon.getSystemIcon(view.context, PointerIcon.TYPE_NULL)
    } else {
        null
    }

private fun View.configureAndroidMousePointerCapture(enabled: Boolean, onCaptureInput: () -> Unit = {}, onMotion: (MotionEvent) -> Boolean) {
    if (Build.VERSION.SDK_INT < 26) return
    if (!enabled) {
        clearAndroidMousePointerCapture()
        return
    }
    setOnCapturedPointerListener { _, event ->
        onCaptureInput()
        onMotion(event)
    }
    post {
        if (isAttachedToWindow && hasWindowFocus() && !hasPointerCapture()) {
            isFocusable = true
            isFocusableInTouchMode = true
            requestFocus()
            onCaptureInput()
            requestPointerCapture()
        }
    }
}

private fun View.clearAndroidMousePointerCapture() {
    if (Build.VERSION.SDK_INT < 26) return
    setOnCapturedPointerListener(null)
    releasePointerCapture()
}

private fun android.view.View.hideAndroidPointerTree() {
    if (Build.VERSION.SDK_INT < 24) return
    val icon = androidNullPointerIcon(this)
    applyAndroidPointerIconTree(icon)
}

private fun android.view.View.showAndroidPointerTree() {
    if (Build.VERSION.SDK_INT < 24) return
    applyAndroidPointerIconTree(null)
}

private fun android.view.View.applyAndroidPointerIconTree(icon: PointerIcon?) {
    if (Build.VERSION.SDK_INT < 24) return
    pointerIcon = icon
    if (this is ViewGroup) {
        for (index in 0 until childCount) {
            getChildAt(index).applyAndroidPointerIconTree(icon)
        }
    }
}

@Composable
private fun FingerMouseInputLayer(
    enabled: Boolean,
    onZoomGesture: (scaleChange: Float, pan: Offset) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (!enabled) return
    var width by remember { mutableStateOf(0) }
    var height by remember { mutableStateOf(0) }
    var pinchActive by remember { mutableStateOf(false) }
    var lastPinchDistance by remember { mutableFloatStateOf(0f) }
    var lastPinchCentroid by remember { mutableStateOf(Offset.Zero) }
    Box(
        modifier
            .onSizeChanged {
                width = it.width
                height = it.height
            }
            .pointerInteropFilter { event ->
                if (NativeStreamInputRouter.isNativeUiTouchGestureActive()) {
                    pinchActive = false
                    lastPinchDistance = 0f
                    lastPinchCentroid = Offset.Zero
                    return@pointerInteropFilter true
                }
                if (event.pointerCount >= 2) {
                    NativeStreamInputRouter.cancelTouchMouse()
                    val distance = event.firstTwoPointerDistance()
                    val centroid = event.firstTwoPointerCentroid()
                    if (pinchActive && lastPinchDistance > 0f && distance > 0f) {
                        onZoomGesture(
                            (distance / lastPinchDistance).coerceIn(0.82f, 1.22f),
                            centroid - lastPinchCentroid,
                        )
                    }
                    pinchActive = true
                    lastPinchDistance = distance
                    lastPinchCentroid = centroid
                    return@pointerInteropFilter true
                }
                if (pinchActive) {
                    if (event.actionMasked == MotionEvent.ACTION_UP || event.actionMasked == MotionEvent.ACTION_CANCEL) {
                        pinchActive = false
                        lastPinchDistance = 0f
                        lastPinchCentroid = Offset.Zero
                    }
                    return@pointerInteropFilter true
                }
                if (event.actionMasked == MotionEvent.ACTION_DOWN) {
                    NativeInputDiagnostics.add("compose finger layer down size=${width}x$height")
                }
                NativeStreamInputRouter.dispatchTouch(event, width, height)
            },
    )
}

private fun MotionEvent.firstTwoPointerDistance(): Float {
    if (pointerCount < 2) return 0f
    val dx = getX(1) - getX(0)
    val dy = getY(1) - getY(0)
    return sqrt(dx * dx + dy * dy)
}

private fun MotionEvent.firstTwoPointerCentroid(): Offset =
    if (pointerCount >= 2) {
        Offset((getX(0) + getX(1)) / 2f, (getY(0) + getY(1)) / 2f)
    } else {
        Offset.Zero
    }

@Composable
private fun NoActiveStreamScreen(
    canResumeSession: Boolean,
    canEndSession: Boolean,
    onBack: () -> Unit,
    onResumeSession: () -> Unit,
    onEndSession: () -> Unit,
) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text("No active stream", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))
        Text(
            "OpenNOW does not have a local stream attached right now.",
            color = TextMuted,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(18.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            OutlinedButton(onClick = onBack) { Text("Back to library") }
            if (canResumeSession) {
                Button(onClick = onResumeSession) { Text(stringResource(R.string.action_resume)) }
            }
            if (canEndSession) {
                Button(onClick = onEndSession) { Text("End cloud session") }
            }
        }
    }
}

@Composable
private fun StreamControlLauncher(
    controlsOpen: Boolean,
    status: String?,
    onToggle: () -> Unit,
    onExit: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier
            .padding(top = 10.dp, end = 10.dp)
            .onGloballyPositioned { coordinates ->
                val bounds = coordinates.boundsInRoot()
                NativeStreamInputRouter.setUiTouchPassthroughBounds(
                    bounds.left.roundToInt(),
                    bounds.top.roundToInt(),
                    bounds.right.roundToInt(),
                    bounds.bottom.roundToInt(),
                )
            },
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (status != null) {
            Surface(
                shape = RoundedCornerShape(999.dp),
                color = Panel.copy(alpha = 0.8f),
                tonalElevation = 3.dp,
            ) {
                Text(
                    status,
                    color = TextMuted,
                    style = MaterialTheme.typography.labelMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                )
            }
        }
        Button(onClick = onToggle, contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)) {
            Text(if (controlsOpen) "Close" else "Controls")
        }
        OutlinedButton(onClick = onExit, contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)) {
            Text("Exit")
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            NativeStreamInputRouter.clearUiTouchPassthroughBounds()
        }
    }
}

@Composable
private fun StreamControlsPanel(
    gameTitle: String,
    status: String?,
    settings: AppSettings,
    audioMuted: Boolean,
    statsVisible: Boolean,
    touchLayoutEditing: Boolean,
    keyboardOpen: Boolean,
    onAudioToggle: () -> Unit,
    onStatsToggle: () -> Unit,
    onStatsStyleCycle: () -> Unit,
    onPhoneRumbleFallbackToggle: () -> Unit,
    onTouchLayoutEditingToggle: () -> Unit,
    onKeyboardToggle: () -> Unit,
    onEsc: () -> Unit,
    onEnter: () -> Unit,
    onBackspace: () -> Unit,
    onExit: () -> Unit,
    onTouchControlsToggle: () -> Unit,
    onMousePadToggle: () -> Unit,
    onSharpeningToggle: () -> Unit,
    onSharpeningAmountChange: (Float) -> Unit,
    onClarityToggle: () -> Unit,
    onClarityAmountChange: (Float) -> Unit,
    onContrastToggle: () -> Unit,
    onContrastAmountChange: (Float) -> Unit,
    onTouchScaleChange: (Float) -> Unit,
    onButtonScaleChange: (Float) -> Unit,
    onStickScaleChange: (Float) -> Unit,
    onOpacityChange: (Float) -> Unit,
    onTouchEdgePaddingChange: (Float) -> Unit,
    onTouchBottomPaddingChange: (Float) -> Unit,
    onTouchLeftOffsetChange: (Float) -> Unit,
    onTouchRightOffsetChange: (Float) -> Unit,
    onClose: () -> Unit,
) {
    val doneFocusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    LaunchedEffect(Unit) {
        delay(80)
        runCatching { doneFocusRequester.requestFocus() }
    }
    Surface(
        modifier = Modifier
            .padding(14.dp)
            .fillMaxWidth(0.94f)
            .fillMaxHeight(0.72f)
            .onGloballyPositioned { coordinates ->
                val bounds = coordinates.boundsInRoot()
                NativeStreamInputRouter.setStreamPanelTouchPassthroughBounds(
                    bounds.left.roundToInt(),
                    bounds.top.roundToInt(),
                    bounds.right.roundToInt(),
                    bounds.bottom.roundToInt(),
                )
            },
        shape = RoundedCornerShape(18.dp),
        color = Panel.copy(alpha = 0.93f),
        tonalElevation = 6.dp,
    ) {
        LazyColumn(
            modifier = Modifier.onPreviewKeyEvent { handleVerticalDpadFocusMove(it, focusManager) },
            contentPadding = PaddingValues(14.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            item {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Column(Modifier.weight(1f)) {
                        Text("Stream Controls", fontWeight = FontWeight.Bold)
                        Text(gameTitle, color = TextMuted, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    }
                    if (status != null) {
                        Text(status, color = TextMuted, style = MaterialTheme.typography.labelMedium)
                    }
                    OutlinedButton(
                        onClick = onExit,
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                    ) {
                        Text("Exit")
                    }
                    OutlinedButton(
                        onClick = onClose,
                        modifier = Modifier.focusRequester(doneFocusRequester),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                    ) {
                        Text("Done")
                    }
                }
            }
            item {
                StreamPanelSection("Display") {
                    StreamControlSwitch("Audio", if (audioMuted) "Muted" else "On", !audioMuted, onAudioToggle)
                    StreamControlSwitch("Stream stats", if (statsVisible) "On" else "Off", statsVisible, onStatsToggle)
                    StreamControlAction("Stats style", settings.streamStatsStyle.label, onStatsStyleCycle)
                    StreamControlSwitch("Stream sharpening", if (settings.stream.streamSharpeningEnabled) "On" else "Off", settings.stream.streamSharpeningEnabled, onSharpeningToggle)
                    if (settings.stream.streamSharpeningEnabled) {
                        CompactSlider("Sharpness amount", settings.stream.streamSharpeningAmount, 0f, 1f, onSharpeningAmountChange)
                    }
                    StreamControlSwitch("Clarity", if (settings.stream.streamClarityEnabled) "On" else "Off", settings.stream.streamClarityEnabled, onClarityToggle)
                    if (settings.stream.streamClarityEnabled) {
                        CompactSlider("Clarity amount", settings.stream.streamClarityAmount, 0f, 1f, onClarityAmountChange)
                    }
                    StreamControlSwitch("Contrast", if (settings.stream.streamContrastEnabled) "On" else "Off", settings.stream.streamContrastEnabled, onContrastToggle)
                    if (settings.stream.streamContrastEnabled) {
                        CompactSlider("Contrast amount", settings.stream.streamContrastAmount, 0f, 1f, onContrastAmountChange)
                    }
                }
            }
            item {
                StreamPanelSection("Input") {
                    StreamControlSwitch("Keyboard bar", if (keyboardOpen) "Visible" else "Hidden", keyboardOpen, onKeyboardToggle)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                        OutlinedButton(onClick = onEsc, modifier = Modifier.weight(1f)) { Text("Esc") }
                        OutlinedButton(onClick = onEnter, modifier = Modifier.weight(1f)) { Text("Enter") }
                        OutlinedButton(onClick = onBackspace, modifier = Modifier.weight(1f)) { Text("⌫") }
                    }
                    StreamControlSwitch("Finger mouse", if (settings.androidTouch.mousePad) "On" else "Off", settings.androidTouch.mousePad, onMousePadToggle)
                    StreamControlSwitch("Touch controller", if (settings.androidTouch.enabled) "Visible" else "Hidden", settings.androidTouch.enabled, onTouchControlsToggle)
                    StreamControlSwitch("Phone rumble fallback", if (settings.phoneRumbleFallback) "On" else "Off", settings.phoneRumbleFallback, onPhoneRumbleFallbackToggle)
                }
            }
            item {
                StreamPanelSection("Touch Layout") {
                    StreamControlSwitch("Drag edit mode", if (touchLayoutEditing) "On" else "Off", touchLayoutEditing, onTouchLayoutEditingToggle)
                    CompactSlider("Layout scale", settings.androidTouch.scale, 0.6f, 1.4f, onTouchScaleChange)
                    CompactSlider("Button size", settings.androidTouch.buttonScale, 0.65f, 1.5f, onButtonScaleChange)
                    CompactSlider("Stick size", settings.androidTouch.stickScale, 0.65f, 1.5f, onStickScaleChange)
                    CompactSlider("Opacity", settings.androidTouch.opacity, 0.15f, 1f, onOpacityChange)
                    CompactDpSlider("Edge padding", settings.androidTouch.edgePaddingDp, 0f, 72f, onTouchEdgePaddingChange)
                    CompactDpSlider("Bottom padding", settings.androidTouch.bottomPaddingDp, 0f, 120f, onTouchBottomPaddingChange)
                    CompactDpSlider("Left position", settings.androidTouch.leftOffsetYDp, -160f, 160f, onTouchLeftOffsetChange)
                    CompactDpSlider("Right position", settings.androidTouch.rightOffsetYDp, -160f, 160f, onTouchRightOffsetChange)
                }
            }
        }
    }
    DisposableEffect(Unit) {
        onDispose {
            NativeStreamInputRouter.clearStreamPanelTouchPassthroughBounds()
        }
    }
}

@Composable
private fun StreamPanelSection(title: String, content: @Composable ColumnScope.() -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(title, color = TextMuted, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
        content()
    }
}

@Composable
private fun StreamControlSwitch(label: String, value: String, checked: Boolean, onClick: () -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White.copy(alpha = 0.06f))
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(Modifier.weight(1f)) {
            Text(label, fontWeight = FontWeight.SemiBold)
            Text(value, color = TextMuted, style = MaterialTheme.typography.labelSmall)
        }
        Switch(checked = checked, onCheckedChange = { onClick() })
    }
}

@Composable
private fun StreamControlAction(label: String, value: String, onClick: () -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White.copy(alpha = 0.06f))
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(Modifier.weight(1f)) {
            Text(label, fontWeight = FontWeight.SemiBold)
            Text(value, color = TextMuted, style = MaterialTheme.typography.labelSmall)
        }
        Text("Change", color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.labelMedium)
    }
}

@Composable
private fun CompactSlider(label: String, value: Float, min: Float, max: Float, onChange: (Float) -> Unit) {
    var local by remember(value) { mutableFloatStateOf(value) }
    val focusManager = LocalFocusManager.current
    Column {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text(label, Modifier.weight(1f))
            Text("${(local * 100).roundToInt()}%", color = TextMuted)
        }
        Slider(
            modifier = Modifier.onPreviewKeyEvent { handleVerticalDpadFocusMove(it, focusManager) },
            value = local,
            onValueChange = {
                local = it.coerceIn(min, max)
                onChange(local)
            },
            valueRange = min..max,
        )
    }
}

@Composable
private fun CompactDpSlider(label: String, value: Float, min: Float, max: Float, onChange: (Float) -> Unit) {
    var local by remember(value) { mutableFloatStateOf(value) }
    val focusManager = LocalFocusManager.current
    Column {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text(label, Modifier.weight(1f))
            Text("${local.roundToInt()} dp", color = TextMuted)
        }
        Slider(
            modifier = Modifier.onPreviewKeyEvent { handleVerticalDpadFocusMove(it, focusManager) },
            value = local,
            onValueChange = {
                local = it.coerceIn(min, max)
                onChange(local)
            },
            valueRange = min..max,
        )
    }
}

@Composable
private fun StreamKeyboardBar(
    text: String,
    onTextChange: (String) -> Unit,
    onSend: () -> Unit,
    onBackspace: () -> Unit,
    onEnter: () -> Unit,
    onEsc: () -> Unit,
    onDone: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = Panel.copy(alpha = 0.95f),
        tonalElevation = 8.dp,
    ) {
        Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(
                value = text,
                onValueChange = onTextChange,
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                placeholder = { Text("Type into stream", color = TextMuted) },
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                Button(onClick = onSend, enabled = text.isNotBlank(), modifier = Modifier.weight(1f)) { Text("Send") }
                OutlinedButton(onClick = onBackspace, modifier = Modifier.weight(1f)) { Text("⌫") }
                OutlinedButton(onClick = onEnter, modifier = Modifier.weight(1f)) { Text("Enter") }
                OutlinedButton(onClick = onEsc, modifier = Modifier.weight(1f)) { Text("Esc") }
                TextButton(onClick = onDone) { Text("Done") }
            }
        }
    }
}

@Composable
private fun StreamStatsPill(
    gameTitle: String,
    status: String,
    streamStats: StreamRuntimeStats,
    audioMuted: Boolean,
    style: StreamStatsStyle,
    modifier: Modifier = Modifier,
) {
    if (style == StreamStatsStyle.Compact) {
        Surface(
            modifier = modifier.padding(8.dp),
            shape = RoundedCornerShape(999.dp),
            color = Panel.copy(alpha = 0.48f),
            tonalElevation = 0.dp,
        ) {
            Row(
                Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text("FPS ${streamStats.fps?.toString() ?: "--"}", color = TextPrimary, style = MaterialTheme.typography.labelSmall, maxLines = 1)
                Text("Ping ${streamStats.pingMs?.let { "${it}ms" } ?: "--"}", color = TextPrimary, style = MaterialTheme.typography.labelSmall, maxLines = 1)
                Text("BW ${formatRuntimeBitrate(streamStats.bitrateKbps)}", color = TextPrimary, style = MaterialTheme.typography.labelSmall, maxLines = 1)
            }
        }
        return
    }
    Surface(
        modifier = modifier.padding(10.dp),
        shape = RoundedCornerShape(12.dp),
        color = Panel.copy(alpha = 0.8f),
        tonalElevation = 4.dp,
    ) {
        Column(Modifier.padding(horizontal = 12.dp, vertical = 9.dp), verticalArrangement = Arrangement.spacedBy(2.dp)) {
            val statusParts = listOfNotNull(
                status.takeUnless(::shouldHideStreamStatusText),
                if (audioMuted) "audio muted" else "audio on",
            )
            Text(gameTitle, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text(
                listOf(
                    "FPS ${streamStats.fps?.toString() ?: "--"}",
                    "Bitrate ${formatRuntimeBitrate(streamStats.bitrateKbps)}",
                    "Ping ${streamStats.pingMs?.let { "${it}ms" } ?: "--"}",
                    "Codec ${streamStats.codec ?: "--"}",
                ).joinToString(" · "),
                color = TextMuted,
                style = MaterialTheme.typography.labelSmall,
            )
            streamStats.resolution?.let { resolution ->
                Text(
                    resolution,
                    color = TextMuted,
                    style = MaterialTheme.typography.labelSmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            Text(
                statusParts.joinToString(" · "),
                color = TextMuted,
                style = MaterialTheme.typography.labelSmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

private fun formatRuntimeBitrate(bitrateKbps: Int?): String {
    val kbps = bitrateKbps ?: return "--"
    return if (kbps >= 1000) {
        "${(kbps / 1000.0).let { kotlin.math.round(it * 10.0) / 10.0 }} Mbps"
    } else {
        "$kbps Kbps"
    }
}

private fun shouldHideStreamStatusText(status: String): Boolean =
    status.trim().replace('_', ' ').let {
        it.equals("Streaming", ignoreCase = true) ||
            it.equals("ICE CONNECTED", ignoreCase = true) ||
            it.equals("ICE COMPLETED", ignoreCase = true)
    }

@Composable
private fun StreamExitConfirmation(
    gameTitle: String,
    onKeepPlaying: () -> Unit,
    onExit: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val keepPlayingFocusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit) {
        delay(80)
        runCatching { keepPlayingFocusRequester.requestFocus() }
    }
    Box(
        Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.58f))
            .clickable(onClick = onKeepPlaying),
        contentAlignment = Alignment.Center,
    ) {
        Surface(
            modifier = modifier
                .padding(24.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            color = Panel.copy(alpha = 0.95f),
            tonalElevation = 8.dp,
        ) {
            Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("Session Control", color = TextMuted, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                Text("Exit Stream?", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Text("Do you really want to exit $gameTitle?", color = TextMuted)
                Text("Your current cloud gaming session will be closed.", color = TextMuted, style = MaterialTheme.typography.bodySmall)
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                    OutlinedButton(
                        onClick = onKeepPlaying,
                        modifier = Modifier
                            .weight(1f)
                            .focusRequester(keepPlayingFocusRequester),
                    ) { Text("Keep Playing") }
                    Button(onClick = onExit, modifier = Modifier.weight(1f)) { Text("Exit Stream") }
                }
            }
        }
    }
}

@Composable
private fun QueueLoadingScreen(state: OpenNowUiState, viewModel: OpenNowViewModel) {
    val session = state.streamSession
    val game = state.streamGame
    val ads = sessionAdItems(session?.adState)
    val ad = ads.firstOrNull { it.adId == state.queueAdActiveId } ?: ads.firstOrNull()
    val mediaUrl = ad?.adMediaFiles?.firstOrNull { !it.mediaFileUrl.isNullOrBlank() }?.mediaFileUrl
        ?: ad?.adUrl
        ?: ad?.mediaUrl
    val queueCopy = queueLaunchStatusText(state)
    val hasPlayableAd = ad != null && mediaUrl != null

    BoxWithConstraints(
        Modifier
            .fillMaxSize()
            .padding(18.dp),
        contentAlignment = Alignment.Center,
    ) {
        val useLandscapeAdLayout = hasPlayableAd && maxWidth > maxHeight

        if (useLandscapeAdLayout) {
            Row(
                Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.spacedBy(18.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                QueueStatusPanel(
                    game = game,
                    queueCopy = queueCopy,
                    error = state.error,
                    compact = true,
                    onMinimize = viewModel::minimizeStreamLaunch,
                    onCancel = viewModel::stopStream,
                    modifier = Modifier.weight(1f),
                )
                QueueAdPanel(
                    ad = ad,
                    mediaUrl = mediaUrl,
                    viewModel = viewModel,
                    modifier = Modifier.weight(1f),
                    playerModifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(16f / 9f),
                )
            }
        } else {
            Column(
                Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                QueueStatusPanel(
                    game = game,
                    queueCopy = queueCopy,
                    error = state.error,
                    compact = false,
                    onMinimize = viewModel::minimizeStreamLaunch,
                    onCancel = viewModel::stopStream,
                    modifier = Modifier.fillMaxWidth(),
                )
                if (hasPlayableAd) {
                    Spacer(Modifier.height(18.dp))
                    QueueAdPanel(
                        ad = ad,
                        mediaUrl = mediaUrl,
                        viewModel = viewModel,
                        modifier = Modifier.fillMaxWidth(),
                        playerModifier = Modifier
                            .fillMaxWidth(0.8f)
                            .height(220.dp),
                    )
                } else if (isSessionAdsRequired(session?.adState)) {
                    Spacer(Modifier.height(12.dp))
                    Text(session?.adState?.message ?: "Ad is required before the queue can continue.", color = TextMuted)
                }
            }
        }
    }
}

@Composable
private fun QueueStatusPanel(
    game: GameInfo?,
    queueCopy: String,
    error: String?,
    compact: Boolean,
    onMinimize: () -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        val imageWidth = if (compact) 76.dp else 96.dp
        val imageHeight = if (compact) 102.dp else 128.dp
        UrlImage(
            game?.imageUrl,
            Modifier
                .width(imageWidth)
                .height(imageHeight)
                .clip(RoundedCornerShape(14.dp)),
        )
        Spacer(Modifier.height(if (compact) 12.dp else 16.dp))
        Text(
            game?.title ?: "Starting stream",
            style = if (compact) MaterialTheme.typography.titleLarge else MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center,
        )
        Text(
            queueCopy,
            color = TextMuted,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(if (compact) 14.dp else 18.dp))
        LinearProgressIndicator(Modifier.fillMaxWidth(if (compact) 0.9f else 0.7f))
        Spacer(Modifier.height(12.dp))
        Row(
            Modifier.fillMaxWidth(if (compact) 0.92f else 0.7f),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            OutlinedButton(onClick = onMinimize, modifier = Modifier.weight(1f)) {
                Text("Minimize", maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
            OutlinedButton(onClick = onCancel, modifier = Modifier.weight(1f)) {
                Text("Cancel", maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
        }
        error?.let {
            Spacer(Modifier.height(12.dp))
            Text(it, color = Color(0xffff9f9f), textAlign = TextAlign.Center)
        }
    }
}

@Composable
private fun QueueAdPanel(
    ad: SessionAdInfo,
    mediaUrl: String,
    viewModel: OpenNowViewModel,
    modifier: Modifier = Modifier,
    playerModifier: Modifier = Modifier,
) {
    Column(
        modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        QueueAdPlayer(
            adId = ad.adId,
            url = mediaUrl,
            modifier = playerModifier,
            onStarted = { viewModel.reportQueueAd(ad.adId, "start") },
            onPaused = { viewModel.reportQueueAd(ad.adId, "pause") },
            onResumed = { viewModel.reportQueueAd(ad.adId, "resume") },
            onFinished = { watchedTimeInMs ->
                viewModel.reportQueueAd(ad.adId, "finish", watchedTimeInMs = watchedTimeInMs)
            },
            onError = { watchedTimeInMs ->
                viewModel.reportQueueAd(
                    ad.adId,
                    "cancel",
                    watchedTimeInMs = watchedTimeInMs,
                    cancelReason = "error",
                    errorInfo = "Error loading url",
                )
            },
        )
    }
}

@Composable
private fun MinimizedQueuePill(
    state: OpenNowUiState,
    onRestore: () -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier
            .padding(horizontal = 12.dp, vertical = 86.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = Panel.copy(alpha = 0.95f),
        tonalElevation = 8.dp,
    ) {
        Row(
            Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            CircularProgressIndicator(Modifier.size(24.dp), strokeWidth = 2.dp, color = MaterialTheme.colorScheme.primary)
            Column(Modifier.weight(1f)) {
                Text(
                    state.streamGame?.title ?: "Starting stream",
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(queueLaunchStatusText(state), color = TextMuted, style = MaterialTheme.typography.bodySmall)
            }
            TextButton(onClick = onRestore) { Text("View") }
            OutlinedButton(onClick = onCancel, contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)) {
                Text("Cancel")
            }
        }
    }
}

@Composable
private fun QueueAdPlayer(
    adId: String,
    url: String,
    modifier: Modifier = Modifier,
    onStarted: () -> Unit,
    onPaused: () -> Unit,
    onResumed: () -> Unit,
    onFinished: (watchedTimeInMs: Long) -> Unit,
    onError: (watchedTimeInMs: Long) -> Unit,
) {
    val context = LocalContext.current
    var muted by remember { mutableStateOf(false) }
    val player = remember(adId, url) {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(url))
            volume = if (muted) 0f else 1f
            prepare()
            playWhenReady = true
        }
    }
    var reportedStart by remember(adId, url) { mutableStateOf(false) }
    var reportedFinish by remember(adId, url) { mutableStateOf(false) }
    var reportedPause by remember(adId, url) { mutableStateOf(false) }
    var playing by remember(adId, url) { mutableStateOf(player.playWhenReady) }
    var controlsVisible by remember(adId, url) { mutableStateOf(false) }
    LaunchedEffect(controlsVisible, playing) {
        if (controlsVisible && playing) {
            delay(2400L)
            controlsVisible = false
        }
    }
    DisposableEffect(player) {
        val listener = object : Player.Listener {
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                playing = isPlaying
                if (!isPlaying) controlsVisible = true
            }

            override fun onPlayWhenReadyChanged(playWhenReady: Boolean, reason: Int) {
                if (!reportedStart || reportedFinish) return
                if (playWhenReady && reportedPause) {
                    reportedPause = false
                    onResumed()
                } else if (!playWhenReady && player.playbackState != Player.STATE_ENDED && !reportedPause) {
                    reportedPause = true
                    onPaused()
                }
            }

            override fun onPlaybackStateChanged(playbackState: Int) {
                if (playbackState == Player.STATE_READY && player.playWhenReady && !reportedStart) {
                    reportedStart = true
                    onStarted()
                }
                if (playbackState == Player.STATE_ENDED && !reportedFinish) {
                    reportedFinish = true
                    onFinished(player.currentPosition.coerceAtLeast(0L))
                }
            }

            override fun onPlayerError(error: androidx.media3.common.PlaybackException) {
                if (!reportedFinish) {
                    reportedFinish = true
                    onError(player.currentPosition.coerceAtLeast(0L))
                }
            }
        }
        player.addListener(listener)
        listener.onIsPlayingChanged(player.isPlaying)
        listener.onPlaybackStateChanged(player.playbackState)
        onDispose {
            player.removeListener(listener)
            player.release()
        }
    }
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable { controlsVisible = true },
    ) {
        AndroidView(
            modifier = Modifier
                .fillMaxSize(),
            factory = { ctx -> PlayerView(ctx).apply { this.player = player; useController = false } },
            update = { it.player = player; it.useController = false },
        )
        AnimatedVisibility(
            visible = controlsVisible || !playing,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier.align(Alignment.BottomCenter),
        ) {
            Row(
                modifier = Modifier
                    .padding(bottom = 12.dp)
                    .clip(RoundedCornerShape(999.dp))
                    .background(Color.Black.copy(alpha = 0.58f))
                    .padding(horizontal = 8.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                QueueAdIconButton(
                    label = if (playing) "Pause ad" else "Play ad",
                    icon = if (playing) QueueAdControlIcon.Pause else QueueAdControlIcon.Play,
                    onClick = {
                        controlsVisible = true
                        if (playing) {
                            player.pause()
                            playing = false
                        } else {
                            player.play()
                            playing = true
                        }
                    },
                )
                QueueAdIconButton(
                    label = if (muted) "Unmute ad" else "Mute ad",
                    icon = if (muted) QueueAdControlIcon.Muted else QueueAdControlIcon.Volume,
                    onClick = {
                        controlsVisible = true
                        muted = !muted
                        player.volume = if (muted) 0f else 1f
                    },
                )
            }
        }
    }
}

private enum class QueueAdControlIcon { Play, Pause, Volume, Muted }

@Composable
private fun QueueAdIconButton(label: String, icon: QueueAdControlIcon, onClick: () -> Unit) {
    IconButton(
        onClick = onClick,
        modifier = Modifier
            .size(42.dp)
            .semantics { contentDescription = label },
    ) {
        QueueAdControlIconView(icon = icon, modifier = Modifier.size(22.dp))
    }
}

@Composable
private fun QueueAdControlIconView(icon: QueueAdControlIcon, modifier: Modifier = Modifier) {
    Canvas(modifier) {
        val w = size.width
        val h = size.height
        when (icon) {
            QueueAdControlIcon.Play -> {
                val path = Path().apply {
                    moveTo(w * 0.35f, h * 0.24f)
                    lineTo(w * 0.35f, h * 0.76f)
                    lineTo(w * 0.76f, h * 0.5f)
                    close()
                }
                drawPath(path, Color.White)
            }
            QueueAdControlIcon.Pause -> {
                drawRoundRect(Color.White, Offset(w * 0.28f, h * 0.24f), Size(w * 0.14f, h * 0.52f), CornerRadius(w * 0.04f, w * 0.04f))
                drawRoundRect(Color.White, Offset(w * 0.58f, h * 0.24f), Size(w * 0.14f, h * 0.52f), CornerRadius(w * 0.04f, w * 0.04f))
            }
            QueueAdControlIcon.Volume, QueueAdControlIcon.Muted -> {
                val body = Path().apply {
                    moveTo(w * 0.18f, h * 0.42f)
                    lineTo(w * 0.34f, h * 0.42f)
                    lineTo(w * 0.52f, h * 0.26f)
                    lineTo(w * 0.52f, h * 0.74f)
                    lineTo(w * 0.34f, h * 0.58f)
                    lineTo(w * 0.18f, h * 0.58f)
                    close()
                }
                drawPath(body, Color.White)
                if (icon == QueueAdControlIcon.Volume) {
                    drawLine(Color.White, Offset(w * 0.62f, h * 0.38f), Offset(w * 0.72f, h * 0.5f), strokeWidth = w * 0.08f)
                    drawLine(Color.White, Offset(w * 0.72f, h * 0.5f), Offset(w * 0.62f, h * 0.62f), strokeWidth = w * 0.08f)
                } else {
                    drawLine(Color.White, Offset(w * 0.64f, h * 0.36f), Offset(w * 0.84f, h * 0.64f), strokeWidth = w * 0.08f)
                    drawLine(Color.White, Offset(w * 0.84f, h * 0.36f), Offset(w * 0.64f, h * 0.64f), strokeWidth = w * 0.08f)
                }
            }
        }
    }
}

@Composable
private fun TouchOverlay(
    client: NativeStreamClient,
    touch: AndroidTouchSettings,
    layoutEditing: Boolean,
    onLeftOffsetChange: (Float, Float) -> Unit,
    onRightOffsetChange: (Float, Float) -> Unit,
    modifier: Modifier = Modifier,
) {
    val opacity = touch.opacity
    val layoutScale = touch.scale
    val buttonScale = touch.buttonScale
    val stickScale = touch.stickScale
    val leftOffsetX = touch.leftOffsetXDp.dp
    val leftOffsetY = touch.leftOffsetYDp.dp
    val rightOffsetX = touch.rightOffsetXDp.dp
    val rightOffsetY = touch.rightOffsetYDp.dp

    LaunchedEffect(client, touch.enabled) {
        client.setVirtualControllerVisible(touch.enabled)
        NativeStreamInputRouter.setTouchControllerVisible(touch.enabled)
    }
    DisposableEffect(client) {
        onDispose {
            client.setVirtualControllerVisible(false)
            NativeStreamInputRouter.setTouchControllerVisible(false)
            NativeStreamInputRouter.clearTouchControllerPassthroughBounds()
        }
    }

    BoxWithConstraints(
        modifier
            .fillMaxSize()
            .padding(
                start = touch.edgePaddingDp.dp,
                top = 10.dp,
                end = touch.edgePaddingDp.dp,
                bottom = touch.bottomPaddingDp.dp,
            ),
    ) {
        if (touch.enabled) {
            val landscape = maxWidth > maxHeight
            if (landscape) {
                LandscapeTouchControls(
                    client = client,
                    opacity = opacity,
                    layoutScale = layoutScale,
                    buttonScale = buttonScale,
                    stickScale = stickScale,
                    layoutEditing = layoutEditing,
                    leftOffsetX = leftOffsetX,
                    leftOffsetY = leftOffsetY,
                    rightOffsetX = rightOffsetX,
                    rightOffsetY = rightOffsetY,
                    onLeftOffsetChange = onLeftOffsetChange,
                    onRightOffsetChange = onRightOffsetChange,
                )
            } else {
                PortraitTouchControls(
                    client = client,
                    opacity = opacity,
                    layoutScale = layoutScale,
                    buttonScale = buttonScale,
                    stickScale = stickScale,
                    layoutEditing = layoutEditing,
                    leftOffsetX = leftOffsetX,
                    leftOffsetY = leftOffsetY,
                    rightOffsetX = rightOffsetX,
                    rightOffsetY = rightOffsetY,
                    onLeftOffsetChange = onLeftOffsetChange,
                    onRightOffsetChange = onRightOffsetChange,
                )
            }
        }
    }
}

@Composable
private fun PortraitTouchControls(
    client: NativeStreamClient,
    opacity: Float,
    layoutScale: Float,
    buttonScale: Float,
    stickScale: Float,
    layoutEditing: Boolean,
    leftOffsetX: Dp,
    leftOffsetY: Dp,
    rightOffsetX: Dp,
    rightOffsetY: Dp,
    onLeftOffsetChange: (Float, Float) -> Unit,
    onRightOffsetChange: (Float, Float) -> Unit,
) {
    Row(
        Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom,
    ) {
        TouchControlGroup(
            id = "portrait-left",
            layoutEditing = layoutEditing,
            offsetX = leftOffsetX,
            offsetY = leftOffsetY,
            onOffsetChange = onLeftOffsetChange,
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.Start,
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    GamepadButton("LB", 0x0100, client, opacity, 48.dp * buttonScale * layoutScale)
                    GamepadTriggerButton("LT", left = true, client = client, opacity = opacity, size = 48.dp * buttonScale * layoutScale)
                }
                Spacer(Modifier.height(44.dp * buttonScale * layoutScale))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.Bottom,
                ) {
                    VirtualStick(
                        label = "L",
                        client = client,
                        opacity = opacity,
                        diameter = 116.dp * stickScale * layoutScale,
                        onChange = client::setVirtualLeftStick,
                    )
                    DpadCluster(client, opacity, buttonScale * layoutScale)
                }
            }
        }
        TouchControlGroup(
            id = "portrait-right",
            layoutEditing = layoutEditing,
            offsetX = rightOffsetX,
            offsetY = rightOffsetY,
            onOffsetChange = onRightOffsetChange,
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.End,
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    GamepadTriggerButton("RT", left = false, client = client, opacity = opacity, size = 48.dp * buttonScale * layoutScale)
                    GamepadButton("RB", 0x0200, client, opacity, 48.dp * buttonScale * layoutScale)
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    GamepadButton("View", 0x0020, client, opacity, 44.dp * buttonScale * layoutScale)
                    GamepadButton("Menu", 0x0010, client, opacity, 44.dp * buttonScale * layoutScale)
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.Bottom,
                ) {
                    VirtualStick(
                        label = "R",
                        client = client,
                        opacity = opacity,
                        diameter = 104.dp * stickScale * layoutScale,
                        onChange = client::setVirtualRightStick,
                    )
                    FaceButtonCluster(client, opacity, buttonScale * layoutScale)
                }
            }
        }
    }
}

@Composable
private fun BoxScope.LandscapeTouchControls(
    client: NativeStreamClient,
    opacity: Float,
    layoutScale: Float,
    buttonScale: Float,
    stickScale: Float,
    layoutEditing: Boolean,
    leftOffsetX: Dp,
    leftOffsetY: Dp,
    rightOffsetX: Dp,
    rightOffsetY: Dp,
    onLeftOffsetChange: (Float, Float) -> Unit,
    onRightOffsetChange: (Float, Float) -> Unit,
) {
    val controlScale = buttonScale * layoutScale
    TouchControlGroup(
        id = "landscape-top-left",
        layoutEditing = layoutEditing,
        offsetX = leftOffsetX,
        offsetY = leftOffsetY,
        onOffsetChange = onLeftOffsetChange,
        modifier = Modifier.align(Alignment.TopStart),
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            GamepadButton("LB", 0x0100, client, opacity, 46.dp * controlScale)
            GamepadTriggerButton("LT", left = true, client = client, opacity = opacity, size = 50.dp * controlScale)
        }
    }
    TouchControlGroup(
        id = "landscape-top-center",
        layoutEditing = false,
        offsetX = 0.dp,
        offsetY = 0.dp,
        onOffsetChange = { _, _ -> },
        modifier = Modifier.align(Alignment.TopCenter),
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            GamepadPillButton("View", 0x0020, client, opacity, width = 76.dp * controlScale, height = 42.dp * controlScale)
            GamepadPillButton("Start", 0x0010, client, opacity, width = 84.dp * controlScale, height = 42.dp * controlScale)
        }
    }
    TouchControlGroup(
        id = "landscape-top-right",
        layoutEditing = layoutEditing,
        offsetX = rightOffsetX,
        offsetY = rightOffsetY,
        onOffsetChange = onRightOffsetChange,
        modifier = Modifier.align(Alignment.TopEnd),
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            GamepadTriggerButton("RT", left = false, client = client, opacity = opacity, size = 50.dp * controlScale)
            GamepadButton("RB", 0x0200, client, opacity, 46.dp * controlScale)
        }
    }
    TouchControlGroup(
        id = "landscape-bottom-left",
        layoutEditing = layoutEditing,
        offsetX = leftOffsetX,
        offsetY = leftOffsetY,
        onOffsetChange = onLeftOffsetChange,
        modifier = Modifier.align(Alignment.BottomStart),
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.Bottom,
        ) {
            VirtualStick(
                label = "L",
                client = client,
                opacity = opacity,
                diameter = 112.dp * stickScale * layoutScale,
                onChange = client::setVirtualLeftStick,
            )
            DpadCluster(client, opacity, controlScale * 0.88f)
        }
    }
    TouchControlGroup(
        id = "landscape-bottom-right",
        layoutEditing = layoutEditing,
        offsetX = rightOffsetX,
        offsetY = rightOffsetY,
        onOffsetChange = onRightOffsetChange,
        modifier = Modifier.align(Alignment.BottomEnd),
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.Bottom,
        ) {
            FaceButtonCluster(client, opacity, controlScale * 0.9f)
            VirtualStick(
                label = "R",
                client = client,
                opacity = opacity,
                diameter = 98.dp * stickScale * layoutScale,
                onChange = client::setVirtualRightStick,
            )
        }
    }
}

@Composable
private fun TouchControlGroup(
    id: String,
    layoutEditing: Boolean,
    offsetX: Dp,
    offsetY: Dp,
    onOffsetChange: (Float, Float) -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit,
) {
    val density = LocalDensity.current
    Box(
        modifier
            .offset(x = offsetX, y = offsetY)
            .onGloballyPositioned { coordinates ->
                val bounds = coordinates.boundsInRoot()
                NativeStreamInputRouter.setTouchControllerPassthroughBound(
                    id,
                    bounds.left.roundToInt(),
                    bounds.top.roundToInt(),
                    bounds.right.roundToInt(),
                    bounds.bottom.roundToInt(),
                )
            },
        contentAlignment = Alignment.Center,
    ) {
        content()
        if (layoutEditing) {
            Box(
                Modifier
                    .matchParentSize()
                    .clip(RoundedCornerShape(18.dp))
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.16f))
                    .border(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.72f), RoundedCornerShape(18.dp))
                    .pointerInput(offsetX, offsetY) {
                        detectDragGestures { change, dragAmount ->
                            change.consume()
                            val deltaXDp = with(density) { dragAmount.x.toDp().value }
                            val deltaYDp = with(density) { dragAmount.y.toDp().value }
                            onOffsetChange(
                                (offsetX.value + deltaXDp).coerceIn(-220f, 220f),
                                (offsetY.value + deltaYDp).coerceIn(-160f, 160f),
                            )
                        }
                    },
                contentAlignment = Alignment.TopCenter,
            ) {
                Surface(
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.9f),
                    shape = RoundedCornerShape(999.dp),
                    modifier = Modifier.padding(top = 4.dp),
                ) {
                    Text(
                        "Drag",
                        color = MaterialTheme.colorScheme.onPrimary,
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                    )
                }
            }
        }
    }
    DisposableEffect(id) {
        onDispose {
            NativeStreamInputRouter.clearTouchControllerPassthroughBound(id)
        }
    }
}

private fun clampStickOffset(offset: Offset, maxRadius: Float): Offset {
    val distance = sqrt(offset.x * offset.x + offset.y * offset.y)
    if (distance <= maxRadius || distance == 0f) return offset
    val scale = maxRadius / distance
    return Offset(offset.x * scale, offset.y * scale)
}

@Composable
private fun VirtualStick(
    label: String,
    client: NativeStreamClient,
    opacity: Float,
    diameter: androidx.compose.ui.unit.Dp,
    onChange: (Float, Float) -> Unit,
) {
    var knobOffset by remember { mutableStateOf(Offset.Zero) }
    val accent = MaterialTheme.colorScheme.primary
    val idleSurface = MaterialTheme.colorScheme.surfaceVariant

    DisposableEffect(client, onChange) {
        onDispose {
            onChange(0f, 0f)
        }
    }

    Box(
        Modifier
            .size(diameter)
            .clip(CircleShape)
            .background(idleSurface.copy(alpha = opacity * 0.72f))
            .border(1.dp, accent.copy(alpha = opacity), CircleShape)
            .pointerInput(client, onChange) {
                awaitPointerEventScope {
                    while (true) {
                        val event = awaitPointerEvent()
                        val change = event.changes.firstOrNull { it.pressed }
                        val maxRadius = min(size.width, size.height) * 0.34f
                        if (change == null) {
                            if (knobOffset != Offset.Zero) {
                                knobOffset = Offset.Zero
                                onChange(0f, 0f)
                            }
                            continue
                        }
                        val center = Offset(size.width / 2f, size.height / 2f)
                        val clamped = clampStickOffset(change.position - center, maxRadius)
                        knobOffset = clamped
                        onChange(
                            (clamped.x / maxRadius).coerceIn(-1f, 1f),
                            (clamped.y / maxRadius).coerceIn(-1f, 1f),
                        )
                        change.consume()
                    }
                }
            },
        contentAlignment = Alignment.Center,
    ) {
        Box(
            Modifier
                .size(diameter * 0.44f)
                .graphicsLayer {
                    translationX = knobOffset.x
                    translationY = knobOffset.y
                }
                .clip(CircleShape)
                .background(accent.copy(alpha = opacity))
                .border(1.dp, Color.White.copy(alpha = opacity * 0.65f), CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            Text(label, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimary)
        }
    }
}

@Composable
private fun FaceButtonCluster(client: NativeStreamClient, opacity: Float, scale: Float) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(6.dp)) {
        GamepadButton("Y", 0x8000, client, opacity, 54.dp * scale)
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            GamepadButton("X", 0x4000, client, opacity, 54.dp * scale)
            Spacer(Modifier.size(54.dp * scale))
            GamepadButton("B", 0x2000, client, opacity, 54.dp * scale)
        }
        GamepadButton("A", 0x1000, client, opacity, 54.dp * scale)
    }
}

@Composable
private fun DpadCluster(client: NativeStreamClient, opacity: Float, scale: Float) {
    val buttonSize = 54.dp * scale
    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(6.dp)) {
        GamepadButton("↑", 0x0001, client, opacity, buttonSize)
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            GamepadButton("←", 0x0004, client, opacity, buttonSize)
            Spacer(Modifier.size(buttonSize))
            GamepadButton("→", 0x0008, client, opacity, buttonSize)
        }
        GamepadButton("↓", 0x0002, client, opacity, buttonSize)
    }
}

@Composable
private fun GamepadTriggerButton(label: String, left: Boolean, client: NativeStreamClient, opacity: Float, size: androidx.compose.ui.unit.Dp) {
    var pressed by remember { mutableStateOf(false) }
    val accent = MaterialTheme.colorScheme.primary
    val idleSurface = MaterialTheme.colorScheme.surfaceVariant
    Box(
        Modifier
            .width(size * 1.24f)
            .height(size * 0.78f)
            .clip(RoundedCornerShape(999.dp))
            .background((if (pressed) accent else idleSurface).copy(alpha = opacity))
            .border(1.dp, accent.copy(alpha = opacity), RoundedCornerShape(999.dp))
            .pointerInput(left) {
                awaitPointerEventScope {
                    while (true) {
                        val event = awaitPointerEvent()
                        val down = event.changes.any { it.pressed }
                        if (down != pressed) {
                            pressed = down
                            client.setVirtualTrigger(left, down)
                        }
                        event.changes.forEach { it.consume() }
                    }
                }
            },
        contentAlignment = Alignment.Center,
    ) {
        Text(label, fontWeight = FontWeight.Bold, color = if (pressed) MaterialTheme.colorScheme.onPrimary else TextPrimary)
    }
}

@Composable
private fun GamepadButton(label: String, mask: Int, client: NativeStreamClient, opacity: Float, size: androidx.compose.ui.unit.Dp) {
    var pressed by remember { mutableStateOf(false) }
    val accent = MaterialTheme.colorScheme.primary
    val idleSurface = MaterialTheme.colorScheme.surfaceVariant
    Box(
        Modifier
            .size(size)
            .clip(CircleShape)
            .background((if (pressed) accent else idleSurface).copy(alpha = opacity))
            .border(1.dp, accent.copy(alpha = opacity), CircleShape)
            .pointerInput(mask) {
                awaitPointerEventScope {
                    while (true) {
                        val event = awaitPointerEvent()
                        val down = event.changes.any { it.pressed }
                        if (down != pressed) {
                            pressed = down
                            client.setVirtualButton(mask, down)
                        }
                        event.changes.forEach { it.consume() }
                    }
                }
            },
        contentAlignment = Alignment.Center,
    ) {
        Text(label, fontWeight = FontWeight.Bold, color = if (pressed) MaterialTheme.colorScheme.onPrimary else TextPrimary)
    }
}

@Composable
private fun GamepadPillButton(
    label: String,
    mask: Int,
    client: NativeStreamClient,
    opacity: Float,
    width: androidx.compose.ui.unit.Dp,
    height: androidx.compose.ui.unit.Dp,
) {
    var pressed by remember { mutableStateOf(false) }
    val accent = MaterialTheme.colorScheme.primary
    val idleSurface = MaterialTheme.colorScheme.surfaceVariant
    Box(
        Modifier
            .width(width)
            .height(height)
            .clip(RoundedCornerShape(999.dp))
            .background((if (pressed) accent else idleSurface).copy(alpha = opacity))
            .border(1.dp, accent.copy(alpha = opacity), RoundedCornerShape(999.dp))
            .pointerInput(mask) {
                awaitPointerEventScope {
                    while (true) {
                        val event = awaitPointerEvent()
                        val down = event.changes.any { it.pressed }
                        if (down != pressed) {
                            pressed = down
                            client.setVirtualButton(mask, down)
                        }
                        event.changes.forEach { it.consume() }
                    }
                }
            },
        contentAlignment = Alignment.Center,
    ) {
        Text(
            label,
            fontWeight = FontWeight.Bold,
            color = if (pressed) MaterialTheme.colorScheme.onPrimary else TextPrimary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
private fun PrintedWasteSelector(
    state: OpenNowUiState,
    game: GameInfo,
    viewModel: OpenNowViewModel,
    modifier: Modifier = Modifier,
) {
    val zones = remember(state.printedWasteQueue, state.printedWasteMapping, state.printedWastePings) {
        state.printedWasteQueue
            .filter { (zoneId, _) -> isStandardPrintedWasteZone(zoneId) && state.printedWasteMapping[zoneId]?.nuked != true }
            .map { (zoneId, zone) ->
                val routingUrl = printedWasteZoneUrl(zoneId)
                PrintedWasteZoneOption(
                    zoneId = zoneId,
                    zone = zone,
                    routingUrl = routingUrl,
                    pingMs = state.printedWastePings[routingUrl],
                )
            }
    }
    val autoZone = remember(zones) { recommendedPrintedWasteZone(zones) }
    val sortedZones = remember(zones, autoZone) {
        val maxPing = zones.mapNotNull { it.pingMs }.maxOrNull()?.coerceAtLeast(1) ?: 1
        val maxQueue = zones.maxOfOrNull { it.zone.QueuePosition }?.coerceAtLeast(1) ?: 1
        zones.sortedWith(
            compareByDescending<PrintedWasteZoneOption> { it.zoneId == autoZone?.zoneId }
                .thenBy { printedWasteScore(it, maxPing, maxQueue) }
                .thenBy { it.zoneId },
        )
    }
    var selectedZoneId by remember(game.id, sortedZones) { mutableStateOf<String?>(autoZone?.zoneId) }
    val selectedZone = sortedZones.firstOrNull { it.zoneId == selectedZoneId } ?: autoZone

    Box(
        Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.72f))
            .clickable(enabled = false) {},
        contentAlignment = Alignment.Center,
    ) {
        Card(
            modifier = modifier
                .fillMaxWidth(0.94f)
                .fillMaxHeight(0.82f),
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
                        Text("Free tier queue routing", color = TextMuted, style = MaterialTheme.typography.bodySmall)
                    }
                }

                if (state.printedWasteLoading) {
                    Box(Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                            Text("Checking PrintedWaste queues and latency", color = TextMuted)
                        }
                    }
                } else if (state.printedWasteError != null) {
                    Box(Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            Text(state.printedWasteError, color = Color(0xffff9f9f))
                            OutlinedButton(onClick = viewModel::refreshPrintedWasteQueues) { Text("Retry") }
                        }
                    }
                } else {
                    autoZone?.let { zoneOption ->
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
                        ) {
                            Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Text("Recommended: ${zoneOption.zoneId}", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                                Text(
                                    listOfNotNull(
                                        zoneOption.pingMs?.let { "${it}ms" },
                                        "Queue ${zoneOption.zone.QueuePosition}",
                                        zoneOption.zone.eta?.let { formatPrintedWasteWait(it) },
                                    ).joinToString(" · "),
                                    color = TextMuted,
                                )
                            }
                        }
                    }
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        items(sortedZones, key = { it.zoneId }) { zoneOption ->
                            val zoneId = zoneOption.zoneId
                            val zone = zoneOption.zone
                            val selected = zoneId == selectedZoneId
                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .clickable { selectedZoneId = zoneId },
                                shape = RoundedCornerShape(12.dp),
                                color = if (selected) MaterialTheme.colorScheme.primary.copy(alpha = 0.16f) else PanelAlt,
                                tonalElevation = if (selected) 2.dp else 0.dp,
                            ) {
                                Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                                    Column(Modifier.weight(1f)) {
                                        Text(zoneId, fontWeight = FontWeight.Bold, color = if (selected) MaterialTheme.colorScheme.primary else TextPrimary)
                                        Text(regionLabel(zone.Region), color = TextMuted, style = MaterialTheme.typography.bodySmall)
                                    }
                                    Text(zoneOption.pingMs?.let { "${it}ms" } ?: "--", color = zoneOption.pingMs?.let(::pingColor) ?: TextMuted, fontWeight = FontWeight.Bold)
                                    Spacer(Modifier.width(10.dp))
                                    Text("Q ${zone.QueuePosition}", color = queueColor(zone.QueuePosition), fontWeight = FontWeight.Bold)
                                    zone.eta?.let {
                                        Spacer(Modifier.width(10.dp))
                                        Text(formatPrintedWasteWait(it), color = TextMuted)
                                    }
                                }
                            }
                        }
                    }
                }

                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
                    TextButton(onClick = viewModel::dismissPrintedWasteSelector) { Text("Cancel") }
                    OutlinedButton(onClick = { viewModel.launchWithPrintedWaste(null) }, modifier = Modifier.weight(1f)) {
                        Text("Default")
                    }
                    Button(
                        onClick = { viewModel.launchWithPrintedWaste(selectedZone?.routingUrl) },
                        enabled = !state.printedWasteLoading && selectedZone != null,
                        modifier = Modifier.weight(1f),
                    ) {
                        Text("Launch")
                    }
                }
            }
        }
    }
}

private fun isStandardPrintedWasteZone(zoneId: String): Boolean =
    zoneId.startsWith("NP-") && !zoneId.startsWith("NPA-")

private data class PrintedWasteZoneOption(
    val zoneId: String,
    val zone: PrintedWasteZone,
    val routingUrl: String,
    val pingMs: Long?,
)

private fun recommendedPrintedWasteZone(zones: List<PrintedWasteZoneOption>): PrintedWasteZoneOption? {
    if (zones.isEmpty()) return null
    val pool = zones.filter { it.pingMs != null }.ifEmpty { zones }
    val maxPing = pool.mapNotNull { it.pingMs }.maxOrNull()?.coerceAtLeast(1) ?: 1
    val maxQueue = pool.maxOfOrNull { it.zone.QueuePosition }?.coerceAtLeast(1) ?: 1
    return pool.minWithOrNull(
        compareBy<PrintedWasteZoneOption> { printedWasteScore(it, maxPing, maxQueue) }
            .thenBy { it.pingMs ?: Long.MAX_VALUE }
            .thenBy { it.zone.QueuePosition },
    )
}

private fun printedWasteScore(zone: PrintedWasteZoneOption, maxPing: Long, maxQueue: Int): Double {
    val pingScore = ((zone.pingMs ?: maxPing).toDouble() / maxPing.toDouble()) * 0.75
    val queueScore = (zone.zone.QueuePosition.toDouble() / maxQueue.toDouble()) * 0.25
    return pingScore + queueScore
}

private fun printedWasteZoneUrl(zoneId: String): String =
    "https://${zoneId.lowercase()}.cloudmatchbeta.nvidiagrid.net/"

private fun formatPrintedWasteWait(etaMs: Long): String {
    val minutes = ((etaMs + 59_999L) / 60_000L).coerceAtLeast(1L)
    return if (minutes < 60L) "${minutes}m" else "${minutes / 60L}h ${minutes % 60L}m"
}

private fun queueColor(queue: Int): Color = when {
    queue <= 5 -> Green
    queue <= 20 -> Color(0xffc7ef6b)
    queue <= 45 -> Color(0xffffc95a)
    else -> Color(0xffff8d8d)
}

private fun pingColor(pingMs: Long): Color = when {
    pingMs <= 60L -> Green
    pingMs <= 120L -> Color(0xffc7ef6b)
    pingMs <= 180L -> Color(0xffffc95a)
    else -> Color(0xffff8d8d)
}

private fun regionLabel(region: String): String = when (region) {
    "US" -> "North America"
    "CA" -> "Canada"
    "EU" -> "Europe"
    "JP" -> "Japan"
    "KR" -> "South Korea"
    "THAI" -> "Southeast Asia"
    "MY" -> "Malaysia"
    else -> region
}

@Composable
internal fun OpenNowMark(size: androidx.compose.ui.unit.Dp) {
    Image(
        painter = painterResource(R.drawable.opennow_logo_mark),
        contentDescription = "OpenNOW",
        modifier = Modifier
            .width(size * 1.85f)
            .height(size),
        contentScale = ContentScale.Fit,
    )
}

private val ColorQuality.label: String
    get() = when (this) {
        ColorQuality.EightBit420 -> "8-bit 4:2:0"
        ColorQuality.EightBit444 -> "8-bit 4:4:4"
        ColorQuality.TenBit420 -> "10-bit 4:2:0"
        ColorQuality.TenBit444 -> "10-bit 4:4:4"
    }
