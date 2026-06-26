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
import androidx.compose.material3.FilledIconButton
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
                Text("\u2715", color = Color.White, fontSize = 20.sp)
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
                Text(
                    if (isMuted) "\uD83D\uDD07" else "\uD83D\uDD0A",
                    color = Color.White,
                    fontSize = 20.sp,
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
                Text("\u2699", color = Color.White, fontSize = 20.sp)
            }
            Text("Settings", color = Color.White, fontSize = 12.sp)

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
