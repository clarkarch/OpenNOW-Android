# Phase 4: Screens + Components

**Goal:** Rewrite each screen. Create minimal shared components.

**YAGNI:** No generic settings components — inline each setting. No EmptyState — inline text. No SkeletonCard — inline placeholder. No SearchField — inline text field. Only create components used in 2+ screens.

---

### Step 4.1: Create directory

```bash
mkdir -p app/src/main/java/com/opencloudgaming/opennow/ui/components
mkdir -p app/src/main/java/com/opencloudgaming/opennow/ui/screens/login
mkdir -p app/src/main/java/com/opencloudgaming/opennow/ui/screens/home
mkdir -p app/src/main/java/com/opencloudgaming/opennow/ui/screens/library
mkdir -p app/src/main/java/com/opencloudgaming/opennow/ui/screens/settings
mkdir -p app/src/main/java/com/opencloudgaming/opennow/ui/screens/stream
```

### Step 4.2: Create UrlImage.kt

**Create:** `app/src/main/java/com/opencloudgaming/opennow/ui/components/UrlImage.kt`

```kotlin
package com.opencloudgaming.opennow.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage

@Composable
fun UrlImage(
    url: String?,
    contentDescription: String? = null,
    modifier: Modifier = Modifier,
) {
    AsyncImage(
        model = url,
        contentDescription = contentDescription,
        modifier = modifier,
        contentScale = ContentScale.Crop,
    )
}
```

### Step 4.3: Create GameCard.kt

**Create:** `app/src/main/java/com/opencloudgaming/opennow/ui/components/GameCard.kt`

```kotlin
package com.opencloudgaming.opennow.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.opencloudgaming.opennow.GameInfo
import com.opencloudgaming.opennow.ui.theme.Surface

@Composable
fun GameCard(
    game: GameInfo,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Surface),
        modifier = modifier.clickable(onClick = onClick),
    ) {
        Column {
            UrlImage(
                url = game.imageUrl,
                contentDescription = game.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f),
            )
            Text(
                text = game.title,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(8.dp),
                maxLines = 2,
            )
        }
    }
}
```

### Step 4.4: Create LoginScreen.kt

**Create:** `app/src/main/java/com/opencloudgaming/opennow/ui/screens/login/LoginScreen.kt`

```kotlin
package com.opencloudgaming.opennow.ui.screens.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.opencloudgaming.opennow.LoginProvider

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    providers: List<LoginProvider>,
    selectedProvider: LoginProvider?,
    isLoading: Boolean,
    error: String?,
    onProviderSelected: (LoginProvider) -> Unit,
    onLogin: () -> Unit,
    onTvLogin: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text("OpenNOW", style = MaterialTheme.typography.displayMedium)
        Spacer(modifier = Modifier.height(32.dp))

        var expanded by remember { mutableStateOf(false) }
        ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = it }) {
            OutlinedTextField(
                value = selectedProvider?.displayName ?: "Select provider",
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                modifier = Modifier.fillMaxWidth().menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable),
            )
            ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                providers.forEach { provider ->
                    DropdownMenuItem(
                        text = { Text(provider.displayName) },
                        onClick = { onProviderSelected(provider); expanded = false },
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onLogin,
            enabled = selectedProvider != null && !isLoading,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(if (isLoading) "Signing in..." else "Sign in")
        }

        if (error != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(error, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = onTvLogin) {
            Text("Use TV code sign-in")
        }
    }
}
```

### Step 4.5: Create TvDeviceLogin.kt

**Create:** `app/src/main/java/com/opencloudgaming/opennow/ui/screens/login/TvDeviceLogin.kt`

```kotlin
package com.opencloudgaming.opennow.ui.screens.login

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun TvDeviceLoginScreen(onBack: () -> Unit) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("TV Device Login")
    }
}
```

### Step 4.6: Create HomeScreen.kt

**Create:** `app/src/main/java/com/opencloudgaming/opennow/ui/screens/home/HomeScreen.kt`

```kotlin
package com.opencloudgaming.opennow.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.opencloudgaming.opennow.GameInfo
import com.opencloudgaming.opennow.ui.components.GameCard
import com.opencloudgaming.opennow.ui.components.UrlImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    games: List<GameInfo>,
    lastPlayedGame: GameInfo?,
    search: String,
    isLoading: Boolean,
    onSearchChange: (String) -> Unit,
    onGameClick: (GameInfo) -> Unit,
    onPlayGame: (GameInfo) -> Unit,
    onRefresh: () -> Unit,
) {
    var isRefreshing by remember { mutableStateOf(false) }
    var selectedGame by remember { mutableStateOf<GameInfo?>(null) }

    LaunchedEffect(isLoading) { if (!isLoading) isRefreshing = false }

    Column(modifier = Modifier.fillMaxSize()) {
        // Search bar
        OutlinedTextField(
            value = search,
            onValueChange = onSearchChange,
            placeholder = { Text("Search games...") },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
            singleLine = true,
        )

        // Hero banner — last played game
        lastPlayedGame?.let { game ->
            Box(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            ) {
                UrlImage(
                    url = game.imageUrl,
                    contentDescription = game.title,
                    modifier = Modifier.fillMaxWidth(),
                )
                Text(
                    text = game.title,
                    style = MaterialTheme.typography.headlineMedium,
                    color = androidx.compose.ui.graphics.Color.White,
                    modifier = Modifier.align(Alignment.BottomStart).padding(16.dp),
                )
            }
        }

        // Game grid
        if (isLoading && games.isEmpty()) {
            // Loading placeholder
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                items(6) {
                    Box(
                        modifier = Modifier.aspectRatio(16f / 9f),
                    )
                }
            }
        } else if (games.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No games found")
            }
        } else {
            PullToRefreshBox(
                isRefreshing = isRefreshing,
                onRefresh = { isRefreshing = true; onRefresh() },
            ) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    items(games.size, key = { games[it].id }) { index ->
                        GameCard(
                            game = games[index],
                            onClick = { selectedGame = games[index] },
                        )
                    }
                }
            }
        }
    }

    // Game details bottom sheet
    selectedGame?.let { game ->
        androidx.compose.material3.AlertDialog(
            onDismissRequest = { selectedGame = null },
            title = { Text(game.title) },
            confirmButton = {
                androidx.compose.material3.TextButton(onClick = { onPlayGame(game); selectedGame = null }) {
                    Text("Play")
                }
            },
            dismissButton = {
                androidx.compose.material3.TextButton(onClick = { selectedGame = null }) {
                    Text("Close")
                }
            },
        )
    }
}
```

### Step 4.7: Create LibraryScreen.kt

**Create:** `app/src/main/java/com/opencloudgaming/opennow/ui/screens/library/LibraryScreen.kt`

```kotlin
package com.opencloudgaming.opennow.ui.screens.library

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.opencloudgaming.opennow.GameInfo
import com.opencloudgaming.opennow.ui.components.GameCard

@Composable
fun LibraryScreen(
    games: List<GameInfo>,
    search: String,
    isLoading: Boolean,
    onSearchChange: (String) -> Unit,
    onGameClick: (GameInfo) -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        OutlinedTextField(
            value = search,
            onValueChange = onSearchChange,
            placeholder = { Text("Search library...") },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
            singleLine = true,
        )

        if (games.isEmpty() && !isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No games in your library")
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                items(games.size, key = { games[it].id }) { index ->
                    GameCard(
                        game = games[index],
                        onClick = { onGameClick(games[index]) },
                    )
                }
            }
        }
    }
}
```

### Step 4.8: Create SettingsScreen.kt

**Create:** `app/src/main/java/com/opencloudgaming/opennow/ui/screens/settings/SettingsScreen.kt`

```kotlin
package com.opencloudgaming.opennow.ui.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.opencloudgaming.opennow.ui.state.SettingsCategory
import com.opencloudgaming.opennow.ui.theme.OnSurfaceVariant
import com.opencloudgaming.opennow.ui.theme.Primary
import com.opencloudgaming.opennow.ui.theme.Surface
import com.opencloudgaming.opennow.ui.theme.SurfaceVariant
import kotlin.math.roundToInt

@Composable
fun SettingsScreen(
    settings: com.opencloudgaming.opennow.AppSettings,
    selectedCategory: SettingsCategory,
    onCategorySelected: (SettingsCategory) -> Unit,
    onUpdateSettings: (com.opencloudgaming.opennow.AppSettings) -> Unit,
    onClearCache: () -> Unit,
    onResetSettings: () -> Unit,
    onLogout: () -> Unit,
    onLogoutAll: () -> Unit,
    onSwitchAccount: (String) -> Unit,
) {
    var showClearCacheDialog by remember { mutableStateOf(false) }
    var showResetDialog by remember { mutableStateOf(false) }

    if (showClearCacheDialog) {
        AlertDialog(
            onDismissRequest = { showClearCacheDialog = false },
            title = { Text("Clear game cache?") },
            text = { Text("Cached results will be removed.") },
            confirmButton = { Button(onClick = { showClearCacheDialog = false; onClearCache() }) { Text("Clear") } },
            dismissButton = { TextButton(onClick = { showClearCacheDialog = false }) { Text("Cancel") } },
        )
    }
    if (showResetDialog) {
        AlertDialog(
            onDismissRequest = { showResetDialog = false },
            title = { Text("Reset settings?") },
            text = { Text("Settings will return to defaults.") },
            confirmButton = { Button(onClick = { showResetDialog = false; onResetSettings() }) { Text("Reset") } },
            dismissButton = { TextButton(onClick = { showResetDialog = false }) { Text("Cancel") } },
        )
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // Category tabs
        LazyRow(
            modifier = Modifier.fillMaxWidth().background(Surface).padding(horizontal = 8.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            items(SettingsCategory.entries) { category ->
                FilterChip(
                    selected = category == selectedCategory,
                    onClick = { onCategorySelected(category) },
                    label = { Text(category.label) },
                )
            }
        }

        // Settings content
        LazyColumn(
            modifier = Modifier.fillMaxSize().background(SurfaceVariant).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            item {
                when (selectedCategory) {
                    SettingsCategory.Stream -> StreamSettings(settings, onUpdateSettings)
                    SettingsCategory.Input -> InputSettings(settings, onUpdateSettings)
                    SettingsCategory.Interface -> InterfaceSettings(settings, onUpdateSettings)
                    SettingsCategory.Account -> AccountSettings(onLogout, onLogoutAll)
                    SettingsCategory.Debug -> DebugSettings(
                        onClearCache = { showClearCacheDialog = true },
                        onResetSettings = { showResetDialog = true },
                    )
                }
            }
        }
    }
}

@Composable
private fun StreamSettings(
    settings: com.opencloudgaming.opennow.AppSettings,
    onUpdate: (com.opencloudgaming.opennow.AppSettings) -> Unit,
) {
    val stream = settings.stream
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        // Video
        Row(Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)).background(Surface).padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Text("FPS", Modifier.weight(1f))
            Text("${stream.fps}")
        }
        Slider(
            value = stream.fps.toFloat(),
            onValueChange = { onUpdate(settings.copy(stream = stream.copy(fps = it.roundToInt()))) },
            valueRange = 30f..240f,
            steps = 6,
            colors = SliderDefaults.colors(thumbColor = Primary, activeTrackColor = Primary),
        )

        Row(Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)).background(Surface).padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Text("Max bitrate (Mbps)", Modifier.weight(1f))
            Text("${stream.maxBitrateMbps}")
        }
        Slider(
            value = stream.maxBitrateMbps.toFloat(),
            onValueChange = { onUpdate(settings.copy(stream = stream.copy(maxBitrateMbps = it.roundToInt()))) },
            valueRange = 1f..150f,
            steps = 148,
            colors = SliderDefaults.colors(thumbColor = Primary, activeTrackColor = Primary),
        )

        // HDR toggle
        Row(Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)).background(Surface).padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Text("HDR", Modifier.weight(1f))
            Switch(
                checked = stream.hdrEnabled,
                onCheckedChange = { onUpdate(settings.copy(stream = stream.copy(hdrEnabled = it))) },
                colors = SwitchDefaults.colors(checkedTrackColor = Primary),
            )
        }
    }
}

@Composable
private fun InputSettings(
    settings: com.opencloudgaming.opennow.AppSettings,
    onUpdate: (com.opencloudgaming.opennow.AppSettings) -> Unit,
) {
    val stream = settings.stream
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)).background(Surface).padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Text("Mouse sensitivity", Modifier.weight(1f))
            Text("%.2f".format(stream.mouseSensitivity))
        }
        Slider(
            value = stream.mouseSensitivity,
            onValueChange = { onUpdate(settings.copy(stream = stream.copy(mouseSensitivity = it))) },
            valueRange = 0.25f..3f,
            colors = SliderDefaults.colors(thumbColor = Primary, activeTrackColor = Primary),
        )

        Row(Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)).background(Surface).padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Text("Touch controls", Modifier.weight(1f))
            Switch(
                checked = settings.androidTouch.enabled,
                onCheckedChange = { onUpdate(settings.copy(androidTouch = settings.androidTouch.copy(enabled = it))) },
                colors = SwitchDefaults.colors(checkedTrackColor = Primary),
            )
        }
    }
}

@Composable
private fun InterfaceSettings(
    settings: com.opencloudgaming.opennow.AppSettings,
    onUpdate: (com.opencloudgaming.opennow.AppSettings) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)).background(Surface).padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Text("Dynamic color", Modifier.weight(1f))
            Switch(
                checked = settings.dynamicColor,
                onCheckedChange = { onUpdate(settings.copy(dynamicColor = it)) },
                colors = SwitchDefaults.colors(checkedTrackColor = Primary),
            )
        }

        // Accent color picker
        Column(Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)).background(Surface).padding(12.dp)) {
            Text("Accent color")
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                com.opencloudgaming.opennow.ui.theme.AccentPreset.entries.forEach { preset ->
                    FilterChip(
                        selected = settings.uiAccent.name == preset.name,
                        onClick = { onUpdate(settings.copy(uiAccent = com.opencloudgaming.opennow.UiAccent.valueOf(preset.name))) },
                        label = { Text(preset.label) },
                    )
                }
            }
        }
    }
}

@Composable
private fun AccountSettings(
    onLogout: () -> Unit,
    onLogoutAll: () -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = {}, modifier = Modifier.weight(1f)) { Text("Add account") }
            OutlinedButton(onClick = onLogout, modifier = Modifier.weight(1f)) { Text("Sign out") }
        }
        OutlinedButton(onClick = onLogoutAll, modifier = Modifier.fillMaxWidth()) {
            Text("Sign out all accounts")
        }
    }
}

@Composable
private fun DebugSettings(
    onClearCache: () -> Unit,
    onResetSettings: () -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        OutlinedButton(onClick = onClearCache, modifier = Modifier.fillMaxWidth()) { Text("Clear cache") }
        OutlinedButton(onClick = onResetSettings, modifier = Modifier.fillMaxWidth()) { Text("Reset settings") }
    }
}
```

### Step 4.9: Create StreamScreen.kt (placeholder)

**Create:** `app/src/main/java/com/opencloudgaming/opennow/ui/screens/stream/StreamScreen.kt`

```kotlin
package com.opencloudgaming.opennow.ui.screens.stream

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.opencloudgaming.opennow.ui.state.StreamState

@Composable
fun StreamScreen(
    streamState: StreamState,
    onDisconnect: () -> Unit,
) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Stream — will be ported from legacy code")
    }
}
```

> **Note:** Actual stream screen will be moved from `OpenNowScreens.kt` in Phase 5.
