package com.opennow.app

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
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
        var selectedTab by remember { mutableIntStateOf(0) }

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
            Screen.Library -> {
                Scaffold(
                    bottomBar = {
                        NavigationBar {
                            NavigationBarItem(
                                selected = selectedTab == 0,
                                onClick = { selectedTab = 0 },
                                icon = { Text("🎮") },
                                label = { Text("Games") },
                            )
                            NavigationBarItem(
                                selected = selectedTab == 1,
                                onClick = { selectedTab = 1 },
                                icon = { Text("📚") },
                                label = { Text("Library") },
                            )
                            NavigationBarItem(
                                selected = selectedTab == 2,
                                onClick = { selectedTab = 2 },
                                icon = { Text("⚙") },
                                label = { Text("Settings") },
                            )
                        }
                    },
                ) { padding ->
                    when (selectedTab) {
                        0 -> LibraryScreen(
                            onGameClick = { game ->
                                selectedGame = game
                                screen = Screen.GameDetail
                            },
                            modifier = Modifier.padding(padding),
                        )
                        1 -> LibraryScreen(
                            onGameClick = { game ->
                                selectedGame = game
                                screen = Screen.GameDetail
                            },
                            modifier = Modifier.padding(padding),
                        )
                        2 -> SettingsScreen(
                            settings = settings,
                            onSettingsChange = { settings = it },
                            onReset = { settings = AppSettings() },
                            modifier = Modifier.padding(padding),
                        )
                    }
                }
            }
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
