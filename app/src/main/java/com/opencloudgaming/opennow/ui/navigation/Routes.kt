package com.opencloudgaming.opennow.ui.navigation

import kotlinx.serialization.Serializable

sealed interface Route {
    @Serializable data object Login : Route
    @Serializable data object Home : Route
    @Serializable data object Library : Route
    @Serializable data object Settings : Route
    @Serializable data object Stream : Route
}
