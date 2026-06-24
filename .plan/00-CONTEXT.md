# Shared Context

> **Status:** Approved  
> **Date:** 2026-06-23  
> **Approach:** MVP-first, vibecoding-friendly, modular but not over-engineered

---

## Verified Dependencies (from build.gradle.kts)

| Dependency | Version | Notes |
|------------|---------|-------|
| Compose BOM | `2026.04.01` | Maps Material3 to `1.4.0` |
| `material3` | `1.4.0` (via BOM) | Stable — 5-param Shapes only |
| `activity-compose` | `1.11.0` | `LocalActivity.current` returns `Activity?` |
| `kotlinx-serialization-json` | `1.8.1` | For type-safe navigation routes |
| `navigation-compose` | **NOT IN BOM** | Must add `2.9.8` explicitly |
| Coil | **NOT IN DEPS** | Must add `io.coil-kt.coil3:coil-compose:3.5.0` |
| Coil OkHttp network | **NOT IN DEPS** | Must add `io.coil-kt.coil3:coil-network-okhttp:3.5.0` |
| `material-icons-extended` | **NOT IN DEPS** | Only for icons not in core |

---

## Verified API Gotchas

| Library | Wrong | Correct |
|---------|-------|---------|
| Coil 3.x dependency | `io.coil-kt:coil-compose` | `io.coil-kt.coil3:coil-compose` |
| Coil 3.x import | `coil.compose.AsyncImage` | `coil3.compose.AsyncImage` |
| M3 Shapes | `Shapes(extraExtraLarge = ...)` | Only 5 params stable: extraSmall, small, medium, large, extraLarge |
| LazyVerticalGrid | `verticalItemSpacing` | `verticalArrangement = Arrangement.spacedBy(...)` |
| LocalActivity | `LocalActivity.current` (non-null) | `LocalActivity.current` (nullable `Activity?`) |
| Icons | `Icons.Default.Store` | Requires `material-icons-extended` |
| Icons | `Icons.Default.Settings` | Safe — in base `material-icons-core` |
| Icons | `Icons.Default.Search` | Safe — in base `material-icons-core` |
| Icons | `Icons.Default.Close` | Safe — in base `material-icons-core` |
| Icons | `Icons.Default.LibraryBooks` | Requires `material-icons-extended` |
| Icons | `Icons.Default.TouchApp` | Requires `material-icons-extended` |
| Icons | `Icons.Default.BarChart` | Requires `material-icons-extended` |
| Icons | `Icons.Default.Disconnect` | Requires `material-icons-extended` |
| Navigation Compose | Not in Compose BOM | Must add `implementation("androidx.navigation:navigation-compose:2.9.8")` explicitly |

---

## Design Decisions (Locked In)

| Decision | Choice |
|----------|--------|
| **Accent color** | Pixel Blue `#8AB4F8` |
| **Background** | Dark `#090B0D` |
| **Surfaces** | `#11161A` (card/panel), `#171D22` (elevated) |
| **Text** | `#EEF3F5` (primary), `#98A4AA` (muted) — consider bumping muted to `#A8B4BA` for better contrast on small text (AA borderline at 4.5:1) |
| **Font** | System default (Roboto) via M3 Typography |
| **Gradients** | Subtle on hero banner only |
| **Card style** | Flat surface, no shadow, scale-down press — test surface elevation carefully to avoid cards blending into background |
| **Hero** | Recently played large banner + play button — visual anchor of the app |
| **Game details** | Centered card overlay with backdrop blur |
| **Grid** | Adaptive: 2 → 3-4 → 5-6 columns |
| **Search** | Icon → expand |
| **Navigation** | Bottom bar (phone) + side rail (tablet/landscape) |
| **Nav tabs** | Store, Library, Settings + profile icon (visual only) |
| **Settings** | Sidebar + detail (5 categories: Stream, Input, Interface, Account, Debug) |
| **Stream controls** | Floating panel, compact+detailed stats toggle |
| **Transitions** | Slide from right |
| **Animations** | Moderate — smooth fades, card press scale |
| **Loading** | Skeleton placeholders |
| **Empty states** | Message + retry |
| **Errors** | Toast/snackbar |
| **Onboarding** | None — straight to login |
| **Library** | Flat grid, search only |
| **Tablet** | Sidebar + content (desktop-like) |
| **Profile icon** | Visual only (no action) |

### Design Review Notes

- **Muted text contrast:** `#98A4AA` on `#090B0D` is borderline AA (~4.5:1). Bump to `#A8B4BA` if accessibility matters.
- **Flat cards:** No shadows works on dark themes, but test that game cards don't blend into the background. The `SurfaceVariant` differentiation helps.
- **Overall:** 8/10 for a dark gaming UI. Clean, fast, functional. The hero banner with gradient overlay is the visual anchor. Rest is utilitarian — appropriate for a game streaming app.

### Forced Defaults (Override Existing)

These defaults override existing `AppSettings` values in `Models.kt`:

| Field | Old Default | New Default | File |
|-------|-------------|-------------|------|
| `uiAccent` | `UiAccent.OpenNow` | `UiAccent.Pixel` | `Models.kt` |

---

## Skill References (Read Before Each Phase)

> **Note:** Skills are at `~/.agents/skills/` (home directory), NOT `.agents/skills/` (project directory).

| Phase | Skills to Consult | Path |
|-------|-------------------|------|
| **Phase 1: Theme** | `material-3` | `~/.agents/skills/material-3/SKILL.md` |
| | `material-3` → `references/color-system.md` | `~/.agents/skills/material-3/references/color-system.md` |
| | `material-3` → `references/typography-and-shape.md` | `~/.agents/skills/material-3/references/typography-and-shape.md` |
| | `material-3` → `references/theming-and-dynamic-color.md` | `~/.agents/skills/material-3/references/theming-and-dynamic-color.md` |
| **Phase 2: State** | `compose-state-authoring` | `~/.agents/skills/compose-state-authoring/SKILL.md` |
| | `compose-state-hoisting` | `~/.agents/skills/compose-state-hoisting/SKILL.md` |
| | `compose-state-holder-ui-split` | `~/.agents/skills/compose-state-holder-ui-split/SKILL.md` |
| | `kotlin-flow-state-event-modeling` | `~/.agents/skills/kotlin-flow-state-event-modeling/SKILL.md` |
| **Phase 3: Navigation** | `material-3` → `references/navigation-patterns.md` | `~/.agents/skills/material-3/references/navigation-patterns.md` |
| | `compose-focus-navigation` | `~/.agents/skills/compose-focus-navigation/SKILL.md` |
| | `material-3` → `references/layout-and-responsive.md` | `~/.agents/skills/material-3/references/layout-and-responsive.md` |
| **Phase 4: Screens** | `compose-modifier-and-layout-style` | `~/.agents/skills/compose-modifier-and-layout-style/SKILL.md` |
| | `compose-slot-api-pattern` | `~/.agents/skills/compose-slot-api-pattern/SKILL.md` |
| | `compose-side-effects` | `~/.agents/skills/compose-side-effects/SKILL.md` |
| | `material-3` → `references/component-catalog.md` | `~/.agents/skills/material-3/references/component-catalog.md` |
| **Phase 5: Components** | `compose-state-authoring` | `~/.agents/skills/compose-state-authoring/SKILL.md` |
| | `compose-modifier-and-layout-style` | `~/.agents/skills/compose-modifier-and-layout-style/SKILL.md` |
| | `compose-slot-api-pattern` | `~/.agents/skills/compose-slot-api-pattern/SKILL.md` |
| **Phase 6: Polish** | `compose-animations` | `~/.agents/skills/compose-animations/SKILL.md` |
| | `compose-recomposition-performance` | `~/.agents/skills/compose-recomposition-performance/SKILL.md` |
| | `compose-stability-diagnostics` | `~/.agents/skills/compose-stability-diagnostics/SKILL.md` |
| **TV/Controller** | `compose-focus-navigation` | `~/.agents/skills/compose-focus-navigation/SKILL.md` |
| **Testing** | `compose-ui-testing-patterns` | `~/.agents/skills/compose-ui-testing-patterns/SKILL.md` |

## MCP Tools (Use for API Verification)

| Tool | Purpose | When to Use |
|------|---------|-------------|
| `android-docs-mcp` | Verify Android API signatures, parameters, return types | Before implementing any Compose, Material3, or AndroidX API |

**MCP Usage:**
- Verify API signatures before writing code
- Check parameter names and types
- Confirm return types (e.g., nullable vs non-null)
- Look up APIs not in the gotchas table
- Verify deprecated APIs and replacements

## LSP (Real-time Error Detection)

| Tool | Purpose | When to Use |
|------|---------|-------------|
| `kotlin-ls` | Kotlin language server diagnostics | During code generation — catches errors before commit |

**LSP Usage:**
- Provides compile errors, type mismatches, missing imports
- Catches errors before CI (faster feedback loop)
- Reduces CI failures by fixing issues pre-commit

---

## Existing Files Reference

These files exist and will be modified or referenced during the rewrite:

```
app/src/main/java/com/opencloudgaming/opennow/
├── OpenNowScreens.kt          # 5,789 lines — ALL UI (will be deleted at end)
├── OpenNowViewModel.kt        # 1,437 lines — will be slimmed to coordinator
├── GfnApi.kt                  # ~1,800 lines — NO CHANGES
├── Streaming.kt               # ~1,700 lines — NO CHANGES (input routing stays)
├── Models.kt                  # 902 lines — may add minor UI models
├── Persistence.kt             # 241 lines — NO CHANGES
├── MainActivity.kt            # 387 lines — update theme + navigation host
├── QrCode.kt                  # 228 lines — NO CHANGES
├── AndroidQueueStatusNotifier.kt  # 76 lines — NO CHANGES
├── AndroidQueueAds.kt         # 59 lines — NO CHANGES
├── DisplayRefreshRate.kt      # 35 lines — NO CHANGES
└── QueueLaunchStatus.kt       # 21 lines — NO CHANGES
```

## Build Verification

**No local builds.** All builds happen on GitHub Actions only. See `ORCHESTRATION.md` for the CI workflow.

---

## What Stays the Same

- `GfnApi.kt` — API calls, no changes
- `Streaming.kt` — WebRTC + input routing, no changes
- `Models.kt` — data classes, no changes (maybe minor additions)
- `Persistence.kt` — SharedPreferences stays, DataStore later if needed
- `QrCode.kt` — QR generation, no changes
- `Queue/notification files` — no changes

---

## Key Model Types (from Models.kt)

These types are used throughout the plan. Do NOT assume different names.

- `AuthSession` — auth state
- `LoginProvider` — provider info (use `displayName`)
- `SavedAccount` — persisted account
- `DeviceLoginPrompt` — TV device login prompt
- `GameInfo` — game data (NOT `CatalogGame`)
- `CatalogBrowseResult` — catalog browse result
- `AppSettings` — full settings object
- `RuntimeCodecReport` — debug codec info
- `SessionInfo` — stream session info
- `VideoCodec` — enum
- `ColorQuality` — enum
- `UiAccent` — enum (OpenNow, Pixel, HotPink, Lime, Coral, Violet)

---

## Key ViewModel Methods (OpenNowViewModel.kt)

These methods exist and can be called from new screens:

- `selectProvider(provider: LoginProvider)`
- `login()`
- `refreshGames()`
- `selectGame(game: GameInfo)`
- `play(game: GameInfo)`
- `setCatalogSearch(query: String)`
- `setLibrarySearch(query: String)`
- `selectSettingsCategory(category: SettingsCategory)`
- `stopStream()`

---

## Incremental Testing Strategy

### How to test new screens before full cutover

1. **Phase 2 verification:** After adding `appState` to ViewModel, verify it compiles. Do NOT wire it to UI yet.
2. **Phase 3 verification:** After adding `AppNavigation.kt`, temporarily replace `OpenNowApp(viewModel)` in `MainActivity.kt` with `AppNavigation(viewModel, navController)`. Old screens won't work yet — that's fine. Verify navigation compiles and routes render.
3. **Phase 4 verification (one screen at a time):** Wire `LoginScreen` from the new package into `AppNavigation.kt`. Keep all other routes as `Box {}` placeholders. Test login flow in isolation.
4. **Phase 5 verification:** Wire `HomeScreen` next. Test catalog browsing, search, hero banner. Keep Library/Settings as placeholders.
5. **Phase 6 verification:** Wire `SettingsScreen`. Test all settings categories.
6. **Phase 7 verification:** Wire `StreamScreen`. This is the riskiest — test with a real stream session.
7. **Full cutover:** Only after all screens pass individual testing, remove the legacy `OpenNowApp` composable and `OpenNowScreens.kt`.

### Build verification after each phase

**No local builds.** After each phase, commit and push to trigger CI. Wait for CI to pass before proceeding.

```bash
git add -A
git commit -m "Phase N: [phase name]"
git push
gh run watch
```

---

## Risks

| Risk | Mitigation |
|------|------------|
| Breaking stream functionality | Stream UI moved as-is, not rewritten |
| TV regression | TV composables moved as-is, test at Phase 4 |
| Performance regression | Profile after each phase |
| Scope creep | Strict phase boundaries, defer polish |
| Merge conflicts with upstream | Coordinate sync schedule |
| Settings/Stream scope | Fully expanded above — all composables covered |
