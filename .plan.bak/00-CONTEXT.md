# Shared Context

## Dependencies

| Dependency | Version | Notes |
|------------|---------|-------|
| Compose BOM | `2026.04.01` | Maps Material3 to `1.4.0` |
| `material3` | `1.4.0` (via BOM) | Stable |
| `activity-compose` | `1.11.0` | `LocalActivity.current` returns `Activity?` |
| `kotlinx-serialization-json` | `1.8.1` | For type-safe navigation |
| `navigation-compose` | `2.9.8` | **NOT in BOM** — must add explicitly |
| `coil3` | `3.5.0` | **NOT in DEPS** — must add `io.coil-kt.coil3:coil-compose` |

## API Gotchas

| Wrong | Correct |
|-------|---------|
| `coil.compose.AsyncImage` | `coil3.compose.AsyncImage` |
| `Icons.Default.Store` | Requires `material-icons-extended` — use text labels |
| `verticalItemSpacing` | `verticalArrangement = Arrangement.spacedBy(...)` |
| `LocalActivity.current` | Returns nullable `Activity?` |
| `navigation-compose` | Not in Compose BOM — add explicitly |

## Design Decisions

| Decision | Choice |
|----------|--------|
| Accent color | Pixel Blue `#8AB4F8` |
| Background | Dark `#090B0D` |
| Surfaces | `#11161A` (card), `#171D22` (elevated) |
| Text | `#EEF3F5` (primary), `#98A4AA` (muted) |
| Font | System default (Roboto) |
| Card style | Flat, no shadow |
| Grid | 2 → 3-4 → 5-6 columns |
| Navigation | Bottom bar (phone only for MVP) |
| Nav tabs | Store, Library, Settings |

## Existing Files

```
app/src/main/java/com/opencloudgaming/opennow/
├── OpenNowScreens.kt          # 5,789 lines — ALL UI (delete at end)
├── OpenNowViewModel.kt        # 1,437 lines — add appState
├── GfnApi.kt                  # ~1,800 lines — NO CHANGES
├── Streaming.kt               # ~1,700 lines — NO CHANGES
├── Models.kt                  # 902 lines — minor edits
├── Persistence.kt             # 241 lines — NO CHANGES
├── MainActivity.kt            # 387 lines — update theme + nav
└── QrCode.kt                  # 228 lines — NO CHANGES
```

## Key ViewModel Methods

- `selectProvider(provider: LoginProvider)`
- `login()`
- `refreshGames()`
- `selectGame(game: GameInfo)`
- `play(game: GameInfo)`
- `setCatalogSearch(query: String)`
- `setLibrarySearch(query: String)`
- `selectSettingsCategory(category: SettingsCategory)`
- `stopStream()`
- `updateSettings(next: AppSettings)`
- `resetSettings()`
- `logout()`
- `logoutAll()`
- `switchAccount(userId: String)`

**MISSING:** `clearCache()` — add it to ViewModel (see Phase 2).

## CI Only

No local builds. All builds via GitHub Actions.
