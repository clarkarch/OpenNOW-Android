package com.opennow.app.ui.stream

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.MusicOff
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun StreamControls(
    onStop: () -> Unit,
    onMute: () -> Unit,
    onSettings: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var isMuted by remember { mutableStateOf(false) }

    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            modifier = Modifier.background(Color.Black.copy(alpha = 0.5f)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            FilledIconButton(
                onClick = onStop,
                modifier = Modifier.size(56.dp),
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = Color.Red,
                ),
            ) {
                Icon(Icons.Filled.Close, contentDescription = "Stop", tint = Color.White)
            }
            Text("Stop", color = Color.White, fontSize = 12.sp)

            FilledIconButton(
                onClick = {
                    isMuted = !isMuted
                    onMute()
                },
                modifier = Modifier.size(56.dp),
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = Color.DarkGray,
                ),
            ) {
                Icon(
                    if (isMuted) Icons.Filled.MusicOff else Icons.Filled.MusicNote,
                    contentDescription = "Mute",
                    tint = Color.White,
                )
            }
            Text("Mute", color = Color.White, fontSize = 12.sp)

            FilledIconButton(
                onClick = onSettings,
                modifier = Modifier.size(56.dp),
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = Color.DarkGray,
                ),
            ) {
                Icon(Icons.Filled.Settings, contentDescription = "Settings", tint = Color.White)
            }
            Text("Settings", color = Color.White, fontSize = 12.sp)

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
