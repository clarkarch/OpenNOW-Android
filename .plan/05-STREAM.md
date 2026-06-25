# Phase 5: Stream Screen

**Goal:** Move existing stream code to `ui/screens/stream/`. Rewrite the UI shell as a compact overlay triggered by back button. Keep all WebRTC, touch controls, queue logic intact.

**MCP:** Before writing, verify:
```
android-docs_search_android_docs("compose Slider")
android-docs_get_api_reference("androidx.compose.material3.Slider")
android-docs_search_android_docs("compose BackHandler")
android-docs_search_android_docs("compose Surface rounded corner")
```
**YAGNI Checklist:**
- No new touch control composables → reuse existing
- No new animation system → reuse existing `AnimatedVisibility`
- No "compact mode" abstraction → just rewrite the panel content
- No settings persistence wrapper → direct `viewModel.updateStreamSettings`
**KISS Checklist:**
- Move ALL stream composables to new file (copy-paste)
- Rewrite ONLY `StreamControlsPanel` (the controls overlay)
- Keep ALL other composables exactly as-is
- New panel has: FPS slider, mouse sensitivity, touch toggle, disconnect, stats. That's it.
**DO NOT RUN:** `./gradlew` or any build command.

---

### Stream Overlay Design

**User requirement:** "Stream UI should be compact, not block what player sees. Only show when Android back button is triggered."

**Current behavior (keep):**
- Full-screen video surface (always visible)
- Touch overlay for mobile controls
- Stats pill (toggleable)
- Queue loading screen

**Current behavior (rewrite):**
- `StreamControlsPanel` — currently a full side panel. Replace with compact overlay.

**New StreamControlsPanel design:**
- Compact bottom sheet or floating panel
- Shows ONLY real-time configurable settings:
  1. FPS slider (30-240) — applies via `Surface.setFrameRate()`
  2. Mouse sensitivity slider — applies to input processing
  3. Touch controls toggle — shows/hides on-screen controls
  4. Disconnect button
  5. Live stats (FPS, bitrate, ping — read-only)
- Semi-transparent background over video
- Dismissed by tapping outside or back button

### Step 5.1: Create directory

```bash
mkdir -p app/src/main/java/com/opencloudgaming/opennow/ui/screens/stream
```

### Step 5.2: Move stream composables

**Create:** `app/src/main/java/com/opencloudgaming/opennow/ui/screens/stream/StreamScreen.kt`

Copy these from `OpenNowScreens.kt` (keep exact signatures, just change package):

| Composable | Lines | Notes |
|------------|-------|-------|
| `StreamScreen` | 3138-3452 | Main entry — rewrite UI shell here |
| `StreamVideoSurface` | 3454-3600 | Keep as-is |
| `clampStreamZoomOffset` | 3602-? | Keep as-is |
| `SurfaceView.setPreferredStreamFrameRate` | 3600-? | Keep as-is |
| `Surface.setPreferredStreamFrameRate` | 3605-? | Keep as-is |
| `FingerMouseInputLayer` | ?-? | Keep as-is |
| `NoActiveStreamScreen` | ?-? | Keep as-is |
| `StreamControlLauncher` | ?-? | Keep as-is |
| `StreamKeyboardBar` | ?-? | Keep as-is |
| `StreamStatsPill` | 4091-4159 | Keep as-is |
| `formatRuntimeBitrate` | 4161-4168 | Keep as-is |
| `shouldHideStreamStatusText` | 4170-4175 | Keep as-is |
| `StreamExitConfirmation` | 4177-? | Keep as-is |
| `QueueLoadingScreen` | ?-? | Keep as-is |
| `QueueStatusPanel` | ?-? | Keep as-is |
| `QueueAdPanel` | ?-? | Keep as-is |
| `MinimizedQueuePill` | ?-? | Keep as-is |
| `QueueAdPlayer` | ?-? | Keep as-is |
| `QueueAdIconButton` | ?-? | Keep as-is |
| `QueueAdControlIconView` | ?-? | Keep as-is |
| `TouchOverlay` | ?-? | Keep as-is |
| `PortraitTouchControls` | ?-? | Keep as-is |
| `LandscapeTouchControls` | ?-? | Keep as-is |
| `TouchControlGroup` | ?-? | Keep as-is |
| `clampStickOffset` | ?-? | Keep as-is |
| `VirtualStick` | ?-? | Keep as-is |
| `FaceButtonCluster` | ?-? | Keep as-is |
| `DpadCluster` | ?-? | Keep as-is |
| `GamepadTriggerButton` | ?-? | Keep as-is |
| `GamepadButton` | ?-? | Keep as-is |
| `GamepadPillButton` | ?-? | Keep as-is |

**Package:** `com.opencloudgaming.opennow.ui.screens.stream`

### Step 5.3: Rewrite StreamControlsPanel

Replace the current `StreamControlsPanel` (which has sliders for everything) with a compact overlay.

**New `StreamControlsPanel` — compact overlay:**

```kotlin
@Composable
fun StreamControlsPanel(
    settings: AppSettings,
    streamStats: StreamRuntimeStats,
    status: String?,
    audioMuted: Boolean,
    onAudioToggle: () -> Unit,
    onFpsChange: (Int) -> Unit,
    onMouseSensitivityChange: (Float) -> Unit,
    onTouchControlsToggle: () -> Unit,
    onKeyboardToggle: () -> Unit,
    onDisconnect: () -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        color = Panel.copy(alpha = 0.92f),
        tonalElevation = 8.dp,
    ) {
        Column(
            Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            // Status + audio
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(status ?: "Streaming", color = TextPrimary, style = MaterialTheme.typography.titleMedium)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    IconButton(onClick = onAudioToggle) {
                        Icon(
                            painter = painterResource(if (audioMuted) R.drawable.ic_mute else R.drawable.ic_volume),
                            contentDescription = "Toggle audio",
                        )
                    }
                }
            }

            // Live stats
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                StatItem("FPS", "${streamStats.fps ?: "--"}")
                StatItem("Ping", "${streamStats.pingMs?.let { "${it}ms" } ?: "--"}")
                StatItem("BW", formatRuntimeBitrate(streamStats.bitrateKbps))
            }

            Divider(color = TextMuted.copy(alpha = 0.2f))

            // FPS slider
            Text("FPS: ${settings.stream.fps}", color = TextMuted, style = MaterialTheme.typography.labelMedium)
            Slider(
                value = settings.stream.fps.toFloat(),
                onValueChange = { onFpsChange(it.roundToInt()) },
                valueRange = 30f..240f,
                steps = 6,
            )

            // Mouse sensitivity
            Text("Mouse: %.1fx".format(settings.stream.mouseSensitivity), color = TextMuted, style = MaterialTheme.typography.labelMedium)
            Slider(
                value = settings.stream.mouseSensitivity,
                onValueChange = onMouseSensitivityChange,
                valueRange = 0.25f..3f,
            )

            // Toggles
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                OutlinedButton(onClick = onKeyboardToggle, modifier = Modifier.weight(1f)) {
                    Text("Keyboard")
                }
                OutlinedButton(onClick = onTouchControlsToggle, modifier = Modifier.weight(1f)) {
                    Text(if (settings.androidTouch.enabled) "Touch: ON" else "Touch: OFF")
                }
            }

            // Disconnect
            Button(
                onClick = onDisconnect,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xffff6b6b)),
            ) {
                Text("Disconnect")
            }

            // Close
            TextButton(onClick = onClose, modifier = Modifier.fillMaxWidth()) {
                Text("Close", color = TextMuted)
            }
        }
    }
}
```

> **NOTE:** This replaces the full `StreamControlsPanel` which had 30+ parameters. The new version has ~12 parameters and only shows real-time configurable settings. The old touch settings (opacity, scale, edge padding) stay in the SettingsScreen.

### Step 5.4: Update StreamScreen composable

In the moved `StreamScreen`, update the `StreamControlsPanel` call to use the new compact signature. The `controlsOpen` state and `BackHandler` logic stays the same.

### Step 5.5: Update navigation

In `AppNavigation.kt`, the `composable<Route.Stream>` already calls `StreamScreen(state, viewModel)`. No changes needed if the signature stays the same.

### Step 5.6: Verify

```bash
# 1. All stream composables are in new file
grep -rn "StreamVideoSurface\|TouchOverlay\|VirtualStick\|QueueLoadingScreen" app/src/main/java/com/opencloudgaming/opennow/ui/screens/stream/
# Should show results

# 2. No stream composables left in OpenNowScreens.kt (except maybe references)
grep -rn "private fun StreamScreen\|private fun StreamVideoSurface" app/src/main/java/com/opencloudgaming/opennow/OpenNowScreens.kt
# Should return ZERO

# 3. StreamScreen compiles (CI check)
```
