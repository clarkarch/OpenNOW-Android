package com.opennow.app.data.model

data class StreamStats(
    val fps: Int = 0,
    val bitrateKbps: Int = 0,
    val codec: String = "H264",
    val latencyMs: Int = 0,
    val frameDrops: Int = 0,
    val resolution: String = "1920x1080",
)
