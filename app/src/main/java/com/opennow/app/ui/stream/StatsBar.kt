package com.opennow.app.ui.stream

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.opennow.app.data.model.StreamStats

@Composable
fun StatsBar(stats: StreamStats, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .background(Color.Black.copy(alpha = 0.7f))
            .padding(horizontal = 8.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        StatItem("FPS", "${stats.fps}")
        StatItem("Bitrate", "${stats.bitrateKbps} kbps")
        StatItem("Codec", stats.codec)
        StatItem("Latency", "${stats.latencyMs} ms")
        StatItem("Resolution", stats.resolution)
    }
}

@Composable
private fun StatItem(label: String, value: String) {
    Text(
        text = "$label: $value",
        color = Color.White,
        fontSize = 11.sp,
        fontFamily = FontFamily.Monospace,
        modifier = Modifier.padding(horizontal = 4.dp),
    )
}
