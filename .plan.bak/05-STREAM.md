# Phase 5: Stream Screen

**Goal:** Move existing stream code from `OpenNowScreens.kt` to `ui/screens/stream/` package.

**YAGNI:** Don't rewrite — just move. The WebRTC integration is too tightly coupled to rewrite.

---

### Step 5.1: Create stream directory

```bash
mkdir -p app/src/main/java/com/opencloudgaming/opennow/ui/screens/stream
```

### Step 5.2: Move stream composables

From `OpenNowScreens.kt`, move these composables to `app/src/main/java/com/opencloudgaming/opennow/ui/screens/stream/StreamScreen.kt`:

- `StreamVideoSurface` (or whatever wraps `SurfaceViewRenderer`)
- `TouchOverlay`
- `PortraitTouchControls` / `LandscapeTouchControls`
- `VirtualStick`, `FaceButtonCluster`, `DpadCluster`
- `GamepadButton`, `GamepadTriggerButton`, `GamepadPillButton`
- `StreamKeyboardBar`
- `QueueLoadingScreen`, `QueueStatusPanel`, `QueueAdPanel`, `QueueAdPlayer`

**Do NOT rewrite these.** Just:
1. Copy the composable functions
2. Update package declaration
3. Update imports
4. Wrap in a single `StreamScreen` composable that accepts `StreamState` and `onDisconnect`

### Step 5.3: Update StreamScreen.kt

Replace the placeholder in `04-SCREENS.md` with the real implementation that delegates to the moved composables.

### Step 5.4: Verify

- All stream composables compile
- `StreamScreen` can be called from `AppNavigation.kt`
- No references to old composables in `OpenNowScreens.kt`
