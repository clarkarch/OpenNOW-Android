package com.opennow.app.data.model

data class AppSettings(
    val resolution: String = "1920x1080",
    val fps: Int = 60,
    val codec: String = "H264",
    val maxBitrateMbps: Int = 50,
    val keyboardLayout: String = "en-US",
    val gameLanguage: String = "en_US",
    val microphoneMode: String = "disabled",
)
