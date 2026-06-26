package com.opennow.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.opennow.app.data.mock.MockData
import com.opennow.app.data.model.AppSettings
import com.opennow.app.data.model.Game
import com.opennow.app.data.model.User
import com.opennow.app.ui.library.GameDetailScreen
import com.opennow.app.ui.library.LibraryScreen
import com.opennow.app.ui.login.LoginScreen
import com.opennow.app.ui.settings.SettingsScreen
import com.opennow.app.ui.stream.StreamScreen

enum class Screen { Login, Library, GameDetail, Settings, Stream }

@Composable
fun OpenNowApp() {
    OpenNowTheme {
        var screen by remember { mutableStateOf(Screen.Login) }
        var selectedGame by remember { mutableStateOf<Game?>(null) }
        var settings by remember { mutableStateOf(AppSettings()) }
        var isLoggedIn by remember { mutableStateOf(false) }
        var user by remember { mutableStateOf<User?>(null) }
        var streamGameName by remember { mutableStateOf("") }

        when (screen) {
            Screen.Login -> LoginScreen(
                isLoggedIn = isLoggedIn,
                user = user,
                onLogin = {
                    isLoggedIn = true
                    user = MockData.user
                    screen = Screen.Library
                },
            )
            Screen.Library -> LibraryScreen(
                onGameClick = { game ->
                    selectedGame = game
                    screen = Screen.GameDetail
                },
                onSettingsClick = { screen = Screen.Settings },
            )
            Screen.GameDetail -> selectedGame?.let { game ->
                GameDetailScreen(
                    game = game,
                    onBack = { screen = Screen.Library },
                    onPlay = {
                        streamGameName = it.title
                        screen = Screen.Stream
                    },
                )
            }
            Screen.Stream -> StreamScreen(
                gameName = streamGameName,
                onBack = { screen = Screen.Library },
            )
            Screen.Settings -> SettingsScreen(
                settings = settings,
                onSettingsChange = { settings = it },
                onReset = { settings = AppSettings() },
                onBack = { screen = Screen.Library },
            )
        }
    }
}
