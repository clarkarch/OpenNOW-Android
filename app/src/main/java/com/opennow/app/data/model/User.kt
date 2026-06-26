package com.opennow.app.data.model

data class User(
    val userId: String,
    val displayName: String,
    val email: String = "",
    val avatarUrl: String = "",
    val membershipTier: String = "FREE",
)
