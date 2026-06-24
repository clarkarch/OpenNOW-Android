# Phase 5: Stream Screen

**Goal:** Rewrite the streaming UI with floating controls and stats toggle.

**Skills to read first:** `compose-focus-navigation/SKILL.md` (for controller/TV support)

**MCP verification (MANDATORY for every API used):**
Before writing ANY code, verify these APIs via MCP:
- `SurfaceViewRenderer` methods: `init()`, `setEnableHardwareScaler()`, `setMirror()`, `setScalingType()`
- `VideoTrack.addSink()` / `removeSink()`
- `EglBase.create()`, `EglBase.eglBaseContext`
- `AndroidView` factory/update signature
- `SurfaceHolder.Callback` methods
- `NativeStreamClient.createRenderer()`, `.start()`, `.stop()`, `.release()`
- `NativeStreamInputRouter.attach()`, `.detach()`, `.dispatchTouch()`, `.dispatchKey()`

**DO NOT assume any API exists. Verify via MCP first.**

**INTEGRATION CHECK (MANDATORY before committing):**
1. StreamScreen MUST create `NativeStreamClient` via `remember`
2. `DisposableEffect` MUST attach/detach from `NativeStreamInputRouter`
3. `LaunchedEffect` MUST call `client.start()` when session is ready
4. `AndroidView` MUST wrap the `SurfaceViewRenderer`
5. All callbacks (onStreamConnected, onStreamError, etc.) MUST be wired to ViewModel
6. StreamControlsPanel MUST be rendered inside AnimatedVisibility
7. NO `setPreferredStreamFrameRate` calls (this API does NOT exist on SurfaceViewRenderer)

---

### Step 5.1: Create StreamControls components

**Create file:** `app/src/main/java/com/opencloudgaming/opennow/ui/screens/stream/StreamControls.kt`

```kotlin
package com.opencloudgaming.opennow.ui.screens.stream

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.opencloudgaming.opennow.ui.theme.OnSurfaceVariant
import com.opencloudgaming.opennow.ui.theme.Primary
import com.opencloudgaming.opennow.ui.theme.StreamControlBg
import com.opencloudgaming.opennow.ui.theme.StreamCtrlShape

@Composable
fun StreamPanelSection(title: String, content: @Composable ColumnScope.() -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = title,
            color = OnSurfaceVariant,
            style = MaterialTheme.typography.labelMedium,
        )
        content()
    }
}

@Composable
fun StreamControlSwitch(label: String, value: String, checked: Boolean, onClick: () -> Unit) {
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
            Text(label, style = MaterialTheme.typography.bodyMedium)
            Text(value, color = OnSurfaceVariant, style = MaterialTheme.typography.labelSmall)
        }
        Switch(checked = checked, onCheckedChange = { onClick() })
    }
}

@Composable
fun StreamControlAction(label: String, value: String, onClick: () -> Unit) {
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
            Text(label, style = MaterialTheme.typography.bodyMedium)
            Text(value, color = OnSurfaceVariant, style = MaterialTheme.typography.labelSmall)
        }
        Text("Change", color = Primary, style = MaterialTheme.typography.labelMedium)
    }
}

@Composable
fun CompactSlider(label: String, value: Float, min: Float, max: Float, onChange: (Float) -> Unit) {
    Column {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text(label, Modifier.weight(1f))
            Text("${(value * 100).roundToInt()}%", color = OnSurfaceVariant)
        }
        Slider(
            value = value,
            onValueChange = onChange,
            valueRange = min..max,
            colors = SliderDefaults.colors(thumbColor = Primary, activeTrackColor = Primary),
        )
    }
}

@Composable
fun StreamStatsPill(
    gameTitle: String,
    status: String,
    detailed: Boolean,
    modifier: Modifier = Modifier,
) {
    Surface(
        color = StreamControlBg,
        modifier = modifier.padding(16.dp),
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(
                text = gameTitle,
                style = MaterialTheme.typography.labelMedium,
                color = OnSurfaceVariant,
            )
            Text(
                text = status,
                style = MaterialTheme.typography.bodySmall,
                color = OnSurfaceVariant,
            )
        }
    }
}

@Composable
fun StreamExitConfirmation(
    gameTitle: String,
    onKeepPlaying: () -> Unit,
    onExit: () -> Unit,
) {
    Surface(
        shape = StreamCtrlShape,
        color = StreamControlBg,
        modifier = Modifier.padding(16.dp),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(24.dp),
        ) {
            Text(
                text = "Exit stream?",
                style = MaterialTheme.typography.titleMedium,
            )
            Text(
                text = gameTitle,
                color = OnSurfaceVariant,
                style = MaterialTheme.typography.bodySmall,
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.padding(top = 16.dp),
            ) {
                OutlinedButton(onClick = onKeepPlaying) {
                    Text("Keep playing")
                }
                Button(
                    onClick = onExit,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error,
                    ),
                ) {
                    Text("Exit")
                }
            }
        }
    }
}
```

### Step 5.2: Create full StreamScreen

**Create file:** `app/src/main/java/com/opencloudgaming/opennow/ui/screens/stream/StreamScreen.kt`

> **This is a PLACEHOLDER shell. The actual WebRTC integration (NativeStreamClient, TouchOverlay, etc.) must be ported from the legacy StreamScreen directly — it's too tightly coupled to native code to rewrite from scratch. The new file should import and delegate to the existing streaming infrastructure.**

```kotlin
package com.opencloudgaming.opennow.ui.screens.stream

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.opencloudgaming.opennow.ui.state.StreamState

@Composable
fun StreamScreen(
    streamState: StreamState,
    onDisconnect: () -> Unit,
) {
    // Local UI state — not in ViewModel
    var controlsOpen by remember { mutableStateOf(false) }
    var exitConfirmOpen by remember { mutableStateOf(false) }
    var statsVisible by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize().background(Color.Black)) {
        // Video surface goes here — delegate to existing NativeStreamClient
        // This requires porting StreamVideoSurface from legacy code

        if (statsVisible) {
            StreamStatsPill(
                gameTitle = streamState.streamGame?.title ?: "Stream",
                status = streamState.streamStatus,
                detailed = false,
                modifier = Modifier.align(Alignment.TopStart),
            )
        }

        AnimatedVisibility(
            visible = controlsOpen,
            enter = fadeIn() + slideInVertically(initialOffsetY = { it / 4 }),
            exit = fadeOut() + slideOutVertically(targetOffsetY = { it / 4 }),
            modifier = Modifier.align(Alignment.BottomEnd),
        ) {
            // StreamControlsPanel goes here — port from legacy
        }

        if (exitConfirmOpen) {
            StreamExitConfirmation(
                gameTitle = streamState.streamGame?.title ?: "this game",
                onKeepPlaying = { exitConfirmOpen = false },
                onExit = { exitConfirmOpen = false; onDisconnect() },
            )
        }
    }
}
```

> **CRITICAL NOTE:** The `StreamVideoSurface`, `TouchOverlay`, `PortraitTouchControls`, `LandscapeTouchControls`, `VirtualStick`, `FaceButtonCluster`, `DpadCluster`, `GamepadButton`, `GamepadTriggerButton`, `GamepadPillButton`, `StreamKeyboardBar`, `QueueLoadingScreen`, `QueueStatusPanel`, `QueueAdPanel`, `QueueAdPlayer`, and `QueueAdIconButton` composables are deeply integrated with `NativeStreamClient` and `NativeStreamInputRouter` (JNI bridge). They should NOT be rewritten — they should be **moved as-is** from `OpenNowScreens.kt` into the new `ui/screens/stream/` package, then imported into the new `StreamScreen`.

---

## Phase 5 Expansion: Queue UI

> Queue composables (`QueueLoadingScreen`, `QueueStatusPanel`, `QueueAdPanel`, `QueueAdPlayer`) should be moved verbatim from `OpenNowScreens.kt` into `app/src/main/java/com/opencloudgaming/opennow/ui/screens/stream/QueueUi.kt`.

---

## Phase 5 Expansion: Touch Controls

> Touch composables (`TouchOverlay`, `PortraitTouchControls`, `LandscapeTouchControls`, `TouchControlGroup`, `VirtualStick`, `FaceButtonCluster`, `DpadCluster`, `GamepadButton`, `GamepadTriggerButton`, `GamepadPillButton`) should be moved verbatim from `OpenNowScreens.kt` into `app/src/main/java/com/opencloudgaming/opennow/ui/screens/stream/TouchControls.kt`.
