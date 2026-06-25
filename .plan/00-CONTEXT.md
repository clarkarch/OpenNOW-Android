# Shared Context

## Goal

Split the 5789-line `OpenNowScreens.kt` monolith into separate screen files. Add Navigation Compose for routing. Add Coil 3 for images. Rewrite stream UI shell as compact overlay. Keep all existing functionality working.

## Architecture Decision: Keep (state, viewModel) Pattern

Every screen keeps the same function signature it has now:
```kotlin
fun SomeScreen(state: OpenNowUiState, viewModel: OpenNowViewModel)
```

**Why:** Subagents copy-paste existing code and fix imports. Zero rewiring. Zero new state architecture. Lowest risk approach.

**What we DON'T build:**
- No new `AppState` or sub-state classes
- No bridge layer between old and new state
- No generic reusable components used once
- No Motion.kt or animation tokens
- No adaptive scaffold (phone + nav rail only)

## Current Architecture

```
MainActivity.kt
  └─ setContent { OpenNowApp(viewModel) }

OpenNowScreens.kt (5789 lines)
  ├─ OpenNowApp()         — entry point, theme wrapper, auth check
  │   ├─ LoadingScreen()   — shown while initializing
  │   ├─ LoginScreen()     — shown when no auth session
  │   └─ MainShell()       — shown when authenticated
  │       ├─ Scaffold with bottom bar / nav rail
  │       ├─ when(state.page) → HomeScreen / LibraryScreen / SettingsScreen / StreamScreen
  │       ├─ GameDetailsSheet overlay
  │       ├─ PrintedWasteSelector overlay
  │       ├─ StoreLaunchSelector overlay
  │       └─ MinimizedQueuePill overlay
  └─ ~100 private composable functions

OpenNowViewModel.kt (1437 lines)
  ├─ OpenNowUiState — single flat state class (48 fields)
  ├─ _state: MutableStateFlow<OpenNowUiState>
  ├─ state: StateFlow<OpenNowUiState> (exposed as StateFlow)
  └─ ~40 public methods

Models.kt (902 lines) — data classes, NO CHANGES
Streaming.kt (~1700 lines) — WebRTC + input, NO CHANGES
GfnApi.kt (~1800 lines) — API calls, NO CHANGES
Persistence.kt (241 lines) — storage, NO CHANGES
QrCode.kt (228 lines) — QR generation, NO CHANGES
```

## How Navigation Works Now

```kotlin
// OpenNowViewModel.kt line 22-27
enum class AppPage { Home, Library, Settings, Stream }

// ViewModel method
fun setPage(page: AppPage) {
    _state.update { it.copy(page = page, selectedGame = null) }
}

// MainShell line 716-735
when (state.page) {
    AppPage.Home -> HomeScreen(state, viewModel, ...)
    AppPage.Library -> LibraryScreen(state, viewModel, ...)
    AppPage.Settings -> SettingsScreen(state, viewModel, tvProfile)
    AppPage.Stream -> StreamScreen(state, viewModel)
}
```

Bottom bar calls `viewModel.setPage(AppPage.Home)` etc. NavigationRail does the same.

## API Gotchas

| Wrong | Correct |
|-------|---------|
| `coil.compose.AsyncImage` | `coil3.compose.AsyncImage` (Coil 3 uses `coil3` package) |
| `Icons.Default.Store` | Requires `material-icons-extended` — use text labels or existing drawables |
| `LocalActivity.current` | Returns nullable `Activity?` |
| `navigation-compose` | NOT in Compose BOM — must add explicitly |
| `PullToRefreshBox` | Use `pulltorefresh` from `material3` — check if available in BOM 2026.04.01 |

## Existing Drawable Resources

Navigation icons already exist:
- `R.drawable.ic_tab_store`
- `R.drawable.ic_tab_library`
- `R.drawable.ic_tab_settings`
- `R.drawable.ic_tab_stream`

## Key ViewModel Methods Called From UI

| Method | Called From | Signature |
|--------|------------|-----------|
| `setPage(page)` | MainShell, BackHandler | `fun setPage(page: AppPage)` |
| `login()` | LoginScreen | `fun login(provider: LoginProvider = state.value.selectedProvider)` |
| `loginWithCode()` | LoginScreen | `fun loginWithCode(provider: LoginProvider = state.value.selectedProvider)` |
| `cancelLogin()` | LoginScreen | `fun cancelLogin()` |
| `selectProvider(provider)` | LoginScreen | `fun selectProvider(provider: LoginProvider)` |
| `refreshGames()` | HomeScreen | `fun refreshGames()` |
| `setCatalogSearch(query)` | HomeScreen | `fun setCatalogSearch(query: String)` |
| `setLibrarySearch(query)` | LibraryScreen | `fun setLibrarySearch(query: String)` |
| `setCatalogSort(sortId)` | HomeScreen | `fun setCatalogSort(sortId: String)` |
| `toggleCatalogFilter(filterId)` | HomeScreen | `fun toggleCatalogFilter(filterId: String)` |
| `clearCatalogFilters()` | HomeScreen | `fun clearCatalogFilters()` |
| `selectGame(game)` | HomeScreen, LibraryScreen | `fun selectGame(game: GameInfo)` |
| `clearSelectedGame()` | GameDetailsSheet, BackHandler | `fun clearSelectedGame()` |
| `play(game)` | GameDetailsSheet | `fun play(game: GameInfo, ...)` |
| `chooseStore(game)` | GameDetailsSheet | `fun chooseStore(game: GameInfo)` |
| `playVariant(game, variant)` | StoreLaunchSelector | `fun playVariant(game: GameInfo, variant: GameVariant)` |
| `updateFavorites(gameId)` | GameDetailsSheet | `fun updateFavorites(gameId: String)` |
| `setDefaultGameVariant(gameId, variantId)` | StoreLaunchSelector | `fun setDefaultGameVariant(gameId: String, variantId: String?)` |
| `dismissStoreChoice()` | StoreLaunchSelector | `fun dismissStoreChoice()` |
| `updateSettings(next)` | SettingsScreen | `fun updateSettings(next: AppSettings)` |
| `resetSettings()` | SettingsScreen | `fun resetSettings()` |
| `clearCatalogCache()` | SettingsScreen | `fun clearCatalogCache()` |
| `updateStreamSettings(transform)` | StreamScreen, SettingsScreen | `fun updateStreamSettings(transform: (StreamSettings) -> StreamSettings)` |
| `stopStream()` | StreamScreen, MinimizedQueuePill | `fun stopStream()` |
| `resumeActiveSession()` | TopStatusBar | `fun resumeActiveSession()` |
| `minimizeStreamLaunch()` | StreamScreen | `fun minimizeStreamLaunch()` |
| `restoreStreamLaunch()` | MinimizedQueuePill | `fun restoreStreamLaunch()` |
| `logout()` | SettingsScreen | `fun logout()` |
| `logoutAll()` | SettingsScreen | `fun logoutAll()` |
| `switchAccount(userId)` | SettingsScreen | `fun switchAccount(userId: String)` |
| `handleControllerBackNavigation()` | MainActivity | `fun handleControllerBackNavigation()` |
| `launchWithPrintedWaste(zoneUrl)` | PrintedWasteSelector | `fun launchWithPrintedWaste(zoneUrl: String?)` |
| `dismissPrintedWasteSelector()` | PrintedWasteSelector | `fun dismissPrintedWasteSelector()` |
| `refreshPrintedWasteQueues()` | PrintedWasteSelector | `fun refreshPrintedWasteQueues()` |

## Screen Function Signatures (Current)

All from `OpenNowScreens.kt`:

```kotlin
// line 341 — LoginScreen
private fun LoginScreen(state: OpenNowUiState, viewModel: OpenNowViewModel)

// line 404 — TvDeviceLoginScreen
private fun TvDeviceLoginScreen(prompt: DeviceLoginPrompt, phase: String, onCancel: () -> Unit)

// line 634 — MainShell
private fun MainShell(state: OpenNowUiState, viewModel: OpenNowViewModel)

// HomeScreen — has extra params for chrome hiding on landscape
private fun HomeScreen(
    state: OpenNowUiState, viewModel: OpenNowViewModel, tvProfile: Boolean,
    hideChromeWhenScrolled: Boolean, searchInTopBar: Boolean,
    onScrollChromeHiddenChange: (Boolean) -> Unit,
)

// LibraryScreen — same pattern as HomeScreen
private fun LibraryScreen(
    state: OpenNowUiState, viewModel: OpenNowViewModel, tvProfile: Boolean,
    hideChromeWhenScrolled: Boolean, searchInTopBar: Boolean,
    onScrollChromeHiddenChange: (Boolean) -> Unit,
)

// SettingsScreen
private fun SettingsScreen(state: OpenNowUiState, viewModel: OpenNowViewModel, tvProfile: Boolean)

// StreamScreen
private fun StreamScreen(state: OpenNowUiState, viewModel: OpenNowViewModel)
```

## CI Workflow

No local builds. All builds via GitHub Actions.
```bash
git add -A && git commit -m "Phase N: [name]" && git push
gh run watch  # MUST show BUILD SUCCESSFUL
# If fails: gh run view --log-failed → fix → retry
```

## Phases

| # | File | Goal | Risk |
|---|------|------|------|
| 1 | `01-THEME.md` | Extract theme to own file | Zero — pure code motion |
| 2 | `02-DEPENDENCIES.md` | Add Coil 3 + Navigation Compose | Low — additive only |
| 3 | `03-NAVIGATION.md` | Routes, NavBar, NavRail, AppNavigation | Medium — rewires screen switching |
| 4 | `04-SCREENS.md` | Extract Login, Home, Library, Settings | Low — copy-paste + fix imports |
| 5 | `05-STREAM.md` | Move stream code + rewrite overlay shell | High — WebRTC coupling |
| 6 | `06-CLEANUP.md` | Delete remnants, verify | Zero — deletion only |
