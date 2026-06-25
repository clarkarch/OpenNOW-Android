# UI Rewrite — Quick Reference

## Decisions

| Decision | Choice | Why |
|----------|--------|-----|
| State pattern | Keep `(state: OpenNowUiState, viewModel: OpenNowViewModel)` | Copy-paste friendly, zero rewiring |
| Theme | Keep existing colors (#090B0D, #11161A, #8AB4F8) | Already works, dark mode |
| Navigation | Bottom bar (phone) + NavRail (tablet) + Type-safe routes | User choice |
| Tabs | Store, Library, Settings | Same as current |
| Image loading | Coil 3 (`coil3.compose.AsyncImage`) | Better than BitmapFactory |
| Settings | Keep category tabs | Same UX, just extracted |
| Stream | Compact overlay on back press only | User requirement: "doesn't block what player sees" |
| Stream controls | FPS slider, mouse sensitivity, touch toggle, disconnect, stats | Only real-time configurable items |
| Function signatures | Keep `(state, viewModel)` pattern | Lowest risk, vibecoding optimized |
| Build | CI/CD only, no local builds | User's workflow |

## Phases

| # | File | Goal | Risk |
|---|------|------|------|
| 1 | `01-THEME.md` | Extract theme to `ui/theme/Theme.kt` | Zero |
| 2 | `02-DEPENDENCIES.md` | Add Coil 3 + Navigation Compose | Low |
| 3 | `03-NAVIGATION.md` | Routes, NavBar, NavRail, AppNavigation | Medium |
| 4 | `04-SCREENS.md` | Extract Login, Home, Library, Settings | Low |
| 5 | `05-STREAM.md` | Move stream + rewrite overlay shell | High |
| 6 | `06-CLEANUP.md` | Delete remnants, verify | Zero |

## YAGNI Rules

1. **No generic components** — if used once, inline it
2. **No adaptive scaffold** — phone + nav rail only
3. **No Motion.kt** — use `200f` literals
4. **No empty state components** — inline `Text("No games")`
5. **No skeleton card** — inline placeholder Box
6. **No bridge state layer** — keep `(state, viewModel)` pattern
7. **No reusable components used once** — inline if single use
8. **No comments unless asked** — code should be self-documenting

## New File Structure

```
app/src/main/java/com/opencloudgaming/opennow/
├── OpenNowScreens.kt          # Shrunk: only OpenNowApp + MainShell + TopStatusBar
├── OpenNowViewModel.kt        # Unchanged
├── GfnApi.kt                  # Unchanged
├── Streaming.kt                # Unchanged
├── Models.kt                   # Unchanged
├── Persistence.kt              # Unchanged
├── MainActivity.kt             # Updated: uses AppNavigation
├── QrCode.kt                   # Unchanged
└── ui/
    ├── theme/
    │   └── Theme.kt           # NEW: colors, UiAccent.color, OpenNowTheme
    ├── navigation/
    │   ├── Routes.kt          # NEW: @Serializable route objects
    │   ├── NavBar.kt          # NEW: BottomBar + NavRail
    │   └── AppNavigation.kt   # NEW: NavHost + Scaffold + overlay orchestration
    ├── screens/
    │   ├── login/
    │   │   └── LoginScreen.kt      # NEW: Login, TvDeviceLogin, DeviceLoginPanel, etc.
    │   ├── home/
    │   │   └── HomeScreen.kt       # NEW: HomeScreen, GameCard, GameDetailsSheet, etc.
    │   ├── library/
    │   │   └── LibraryScreen.kt    # NEW: LibraryScreen, ActiveSessionResumeCard
    │   ├── settings/
    │   │   └── SettingsScreen.kt   # NEW: SettingsScreen, all settings panels
    │   └── stream/
    │       └── StreamScreen.kt     # NEW: StreamScreen + all stream composables
    └── components/
        └── UrlImage.kt        # NEW: Coil 3 wrapper
```

## CI Workflow

```bash
git add -A && git commit -m "Phase N: [name]" && git push
gh run watch  # MUST show BUILD SUCCESSFUL
# If fails: gh run view --log-failed → fix → retry
```

## Key Files (Don't Modify)

- `GfnApi.kt` — API calls
- `Streaming.kt` — WebRTC + input
- `Models.kt` — data classes
- `Persistence.kt` — storage
- `QrCode.kt` — QR generation

## Key Files (Will Modify)

- `OpenNowViewModel.kt` — NO CHANGES (keep existing state pattern)
- `MainActivity.kt` — Update setContent to use AppNavigation
- `OpenNowScreens.kt` — Delete extracted code, keep orchestration
- `app/build.gradle.kts` — Add 3 dependencies
