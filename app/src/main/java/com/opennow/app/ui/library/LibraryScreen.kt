package com.opennow.app.ui.library

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.opennow.app.data.mock.MockData
import com.opennow.app.data.model.Game
import com.opennow.app.ui.components.GameGrid

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun LibraryScreen(
    onGameClick: (Game) -> Unit,
    onSettingsClick: () -> Unit = {},
    isLoading: Boolean = false,
    modifier: Modifier = Modifier,
    games: List<Game> = MockData.games,
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedGenre by remember { mutableStateOf<String?>(null) }
    val focusManager = LocalFocusManager.current

    val allGenres = remember(games) {
        games.flatMap { it.genres }.distinct().sorted()
    }

    val filteredGames = remember(games, searchQuery, selectedGenre) {
        games.filter { game ->
            val matchesSearch = searchQuery.isBlank() ||
                game.title.contains(searchQuery, ignoreCase = true)
            val matchesGenre = selectedGenre == null ||
                game.genres.contains(selectedGenre)
            matchesSearch && matchesGenre
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
            ) { focusManager.clearFocus() },
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "OpenNext",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
            )
            IconButton(onClick = onSettingsClick) {
                Text("\u2699")
            }
        }

        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            placeholder = { Text("Search games") },
            leadingIcon = {
                Text("\uD83D\uDD0D")
            },
            singleLine = true,
            shape = MaterialTheme.shapes.medium,
        )

        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            FilterChip(
                selected = selectedGenre == null,
                onClick = { selectedGenre = null },
                label = { Text("All") },
            )
            allGenres.forEach { genre ->
                FilterChip(
                    selected = selectedGenre == genre,
                    onClick = {
                        selectedGenre = if (selectedGenre == genre) null else genre
                    },
                    label = { Text(genre) },
                )
            }
        }

        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            when {
                isLoading -> CircularProgressIndicator()
                filteredGames.isEmpty() -> Text(
                    text = "No games found",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                else -> GameGrid(
                    games = filteredGames,
                    onSelect = onGameClick,
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
    }
}
