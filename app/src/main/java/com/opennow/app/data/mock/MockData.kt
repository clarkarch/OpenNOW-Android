package com.opennow.app.data.mock

import com.opennow.app.data.model.Game
import com.opennow.app.data.model.User
import com.opennow.app.data.model.StreamStats

object MockData {
    val user = User(
        userId = "12345",
        displayName = "TestUser",
        email = "test@example.com",
        membershipTier = "Priority",
    )

    val games = listOf(
        Game(id = "1", title = "Cyberpunk 2077", store = "Steam", genres = listOf("RPG", "Action"), description = "An open-world action-adventure RPG."),
        Game(id = "2", title = "Fortnite", store = "Epic", genres = listOf("Battle Royale", "Shooter")),
        Game(id = "3", title = "Valorant", store = "Riot", genres = listOf("FPS", "Tactical")),
        Game(id = "4", title = "GTA V", store = "Rockstar", genres = listOf("Action", "Adventure")),
        Game(id = "5", title = "Minecraft", store = "Microsoft", genres = listOf("Sandbox", "Survival")),
        Game(id = "6", title = "Apex Legends", store = "EA", genres = listOf("Battle Royale", "FPS")),
        Game(id = "7", title = "League of Legends", store = "Riot", genres = listOf("MOBA")),
        Game(id = "8", title = "The Witcher 3", store = "GOG", genres = listOf("RPG")),
        Game(id = "9", title = "Red Dead Redemption 2", store = "Rockstar", genres = listOf("Action", "Adventure")),
        Game(id = "10", title = "Elden Ring", store = "Steam", genres = listOf("RPG", "Action")),
        Game(id = "11", title = "Baldur's Gate 3", store = "Steam", genres = listOf("RPG")),
        Game(id = "12", title = "Hogwarts Legacy", store = "Steam", genres = listOf("RPG", "Action")),
        Game(id = "13", title = "Starfield", store = "Microsoft", genres = listOf("RPG", "Sci-Fi")),
        Game(id = "14", title = "Diablo IV", store = "Battle.net", genres = listOf("RPG", "Action")),
        Game(id = "15", title = "Overwatch 2", store = "Battle.net", genres = listOf("FPS")),
        Game(id = "16", title = "Dead by Daylight", store = "Steam", genres = listOf("Horror", "Survival")),
        Game(id = "17", title = "Rocket League", store = "Epic", genres = listOf("Sports", "Racing")),
        Game(id = "18", title = " Destiny 2", store = "Epic", genres = listOf("FPS", "MMO")),
        Game(id = "19", title = "No Man's Sky", store = "Steam", genres = listOf("Survival", "Exploration")),
        Game(id = "20", title = "Rainbow Six Siege", store = "Ubisoft", genres = listOf("FPS", "Tactical")),
    )

    fun fakeStats(fps: Int = 60) = StreamStats(
        fps = fps,
        bitrateKbps = 45000,
        codec = "H264",
        latencyMs = 12,
        frameDrops = 0,
        resolution = "1920x1080",
    )
}
