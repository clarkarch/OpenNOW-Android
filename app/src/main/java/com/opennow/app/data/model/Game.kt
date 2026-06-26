package com.opennow.app.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Game(
    val id: String,
    val title: String,
    val imageUrl: String = "",
    val heroImageUrl: String = "",
    val description: String = "",
    val developer: String = "",
    val publisher: String = "",
    val genres: List<String> = emptyList(),
    val store: String = "",
    val isInLibrary: Boolean = false,
    val launchAppId: String? = null,
)
