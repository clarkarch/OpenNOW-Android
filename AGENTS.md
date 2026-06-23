# AGENTS.md

## CRITICAL RULES - MUST FOLLOW

- Always ask clarifying questions
- Never assume design, tech stack or features
- Use deep-dive sub-agents to assist with research
- Use deep-dive sub-agents to review the different aspects of your plan before presenting to the user
- Never implement features yourself when possible - use sub-agents!
- Identify changes from the plan that can be implemented in parallel, and use sub-agents to implement the features efficiently
- When using sub-agents to implement features, act as a coordinator only
- Use websearch to verify facts, libraries, and best practices before implementing
- Read existing files to understand codebase patterns before suggesting changes
- Prefer editing existing files over creating new ones
- Keep responses concise and avoid unnecessary explanation
- Run lint/typecheck after making code changes
- NEVER let sub-agents run build/compile commands (e.g. gradlew, npm run build, etc). Only the main agent should compile, and only when the user explicitly asks for it

## Project Summary

- **Tech stack:** Kotlin, Jetpack Compose, Material 3, Android WebRTC, Media3
- **Architecture:** Single ViewModel (OpenNowViewModel), monolithic UI file (OpenNowScreens.kt)
- **Build:** `./gradlew :app:assembleDebug`
- **Package:** `com.opencloudgaming.opennow`

### Key Files

| File | Purpose |
|---|---|
| `OpenNowScreens.kt` | All screens, composables, theming (~5000+ lines) |
| `Models.kt` | Data classes, enums, settings models, resolution helpers |
| `OpenNowViewModel.kt` | Single ViewModel — state management, all app logic |
| `GfnApi.kt` | GFN API calls (auth, catalog, sessions, subscriptions) |
| `Streaming.kt` | WebRTC stream client, native JNI bridge |
| `Persistence.kt` | Settings/auth storage (DataStore, SharedPreferences) |
| `MainActivity.kt` | Entry point, system UI setup |
| `DisplayRefreshRate.kt` | Display refresh rate detection |
| `QrCode.kt` | QR code generation for TV login |
| `QueueLaunchStatus.kt` | Queue status text helpers |
| `AndroidQueueAds.kt` | Queue ad playback |
| `AndroidQueueStatusNotifier.kt` | Queue status notifications |

### Screens

| Screen | Description |
|---|---|
| LoginScreen | Provider picker, sign-in button, TV device code login |
| HomeScreen (Store) | Game catalog grid, search, sort/filter, swipe-to-refresh |
| LibraryScreen | User's owned games grid, favorites-first ordering |
| SettingsScreen | App/stream settings (currently flat list) |
| StreamScreen | Video surface, touch overlay, stream controls, queue ads |

### Navigation

- **Phone portrait:** Bottom NavigationBar (3 tabs: Store, Library, Settings)
- **Phone landscape:** Side NavigationRail + top status bar
- **Android TV:** Side NavigationRail + D-pad focus handling
- **Stream:** Full-screen immersive, no navigation chrome

### Theming

- Dark-only theme with accent color customization
- Default accent: Green `#6af0a0`
- Accent options: OpenNow, Pixel, HotPink, Lime, Coral, Violet
- Optional Material You dynamic color (Android 12+)
- Custom color constants: Background, Panel, PanelAlt, TextPrimary, TextMuted

### State Management

- Single `MutableStateFlow<OpenNowUiState>` in `OpenNowViewModel`
- All app state flows through one ViewModel (auth, catalog, streaming, settings, queue)
- Settings persisted via `SettingsStore` (DataStore)
- Auth persisted via `AuthStore` (SharedPreferences)

### Streaming

- WebRTC-based video/audio streaming
- Native JNI library (`opennow_native`) for runtime diagnostics
- Touch overlay with virtual gamepad + finger mouse
- Stream stats (compact/detailed)
- Queue ad playback via Media3
- Safe video fallback (auto-downgrade codec on stall)

## Upstream Sync

This project forked from [OpenCloudGaming/OpenNOW](https://github.com/OpenCloudGaming/OpenNOW). To sync upstream changes:

```bash
# One-time setup
git remote add upstream https://github.com/OpenCloudGaming/OpenNOW.git

# Fetch updates
git fetch upstream

# See what's new
git log upstream/android-native --oneline

# Cherry-pick specific commits
git cherry-pick <commit-hash>

# Or grab specific files
git checkout upstream/android-native -- app/src/main/java/com/opencloudgaming/opennow/GfnApi.kt
```

**What syncs easily:** API changes, ViewModel logic, Models, Streaming improvements, bug fixes
**What will conflict:** OpenNowScreens.kt (custom UI — merge manually)