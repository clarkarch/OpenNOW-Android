package com.opennow.app.ui.stream

import androidx.activity.compose.BackHandler
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
import com.opennow.app.data.model.StreamStats

@Composable
fun StreamScreen(
    gameName: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var controlsVisible by remember { mutableStateOf(true) }

    BackHandler {
        if (controlsVisible) {
            controlsVisible = false
        } else {
            onBack()
        }
    }

    Box(modifier = modifier.fillMaxSize().background(Color.Black)) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF1A1A2E)),
        )

        StatsBar(
            stats = StreamStats(fps = 60, bitrateKbps = 45000, latencyMs = 12, resolution = "1920x1080"),
            modifier = Modifier.align(Alignment.TopCenter),
        )

        if (controlsVisible) {
            StreamControls(
                onStop = onBack,
                onMute = {},
                onSettings = {},
                modifier = Modifier.align(Alignment.Center),
            )
        }

        if (controlsVisible) {
            OnScreenGamepad(
                onButtonPress = {},
                modifier = Modifier.align(Alignment.BottomCenter),
            )
        }
    }
}
