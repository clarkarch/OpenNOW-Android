# Phase 4: Screen Rewrite

**Goal:** Rewrite each screen as a focused composable using the new design system.

**Skills to read first:** `compose-modifier-and-layout-style/SKILL.md`, `compose-slot-api-pattern/SKILL.md`, `compose-side-effects/SKILL.md`, `material-3/references/component-catalog.md`

**MCP verification (MANDATORY):** Use `android-docs_search_android_docs` and `android-docs_get_api_reference` to verify Coil 3.x `AsyncImage`, `LazyVerticalGrid`, `Card`, `Button`, and other Compose APIs BEFORE writing code.

**INTEGRATION CHECK (MANDATORY after creating each screen):**
1. Read AppNavigation.kt — find where this screen is called
2. Read the call site — list ALL parameters passed
3. Read the screen composable — verify ALL parameters match
4. Verify ALL callback types match (e.g., `(GameInfo) -> Unit` not `() -> Unit`)
5. Verify the screen is actually rendered (not just defined)
6. Verify GameDetailsSheet is shown when selectedGame is not null
7. Verify SettingsScreen "Add account" button is wired (not empty `onClick = {}`)

---

### Step 4.1: Create component directory

```bash
mkdir -p app/src/main/java/com/opencloudgaming/opennow/ui/components
mkdir -p app/src/main/java/com/opencloudgaming/opennow/ui/screens/login
mkdir -p app/src/main/java/com/opencloudgaming/opennow/ui/screens/home
mkdir -p app/src/main/java/com/opencloudgaming/opennow/ui/screens/library
mkdir -p app/src/main/java/com/opencloudgaming/opennow/ui/screens/settings
mkdir -p app/src/main/java/com/opencloudgaming/opennow/ui/screens/stream
```

### Step 4.2: Create UrlImage.kt

**Create file:** `app/src/main/java/com/opencloudgaming/opennow/ui/components/UrlImage.kt`

> **API NOTE:** Coil 3.x uses `coil3.compose.AsyncImage`, NOT `coil.compose.AsyncImage`.

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
    contentScale: ContentScale = ContentScale.Crop,
) {
    AsyncImage(
        model = url,
        contentDescription = contentDescription,
        modifier = modifier,
        contentScale = contentScale,
    )
}
```

### Step 4.3: Create GameCard.kt

**Create file:** `app/src/main/java/com/opencloudgaming/opennow/ui/components/GameCard.kt`

> **API NOTE:** Use `GameInfo` (not `CatalogGame`) from `Models.kt`.

```kotlin
package com.opencloudgaming.opennow.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.opencloudgaming.opennow.GameInfo
import com.opencloudgaming.opennow.ui.theme.CardShape
import com.opencloudgaming.opennow.ui.theme.Surface
import com.opencloudgaming.opennow.ui.theme.springSnappy

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GameCard(
    game: GameInfo,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = springSnappy(),
        label = "cardScale",
    )

    Card(
        shape = CardShape,
        colors = CardDefaults.cardColors(containerColor = Surface),
        modifier = modifier
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .combinedClickable(
                onClick = onClick,
                onClickLabel = game.title,
            ),
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

### Step 4.4: Create GameGrid.kt

**Create file:** `app/src/main/java/com/opencloudgaming/opennow/ui/components/GameGrid.kt`

> **API NOTE:** Use `verticalArrangement = Arrangement.spacedBy(...)`, NOT `verticalItemSpacing`.

```kotlin
package com.opencloudgaming.opennow.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.opencloudgaming.opennow.GameInfo

@Composable
fun GameGrid(
    games: List<GameInfo>,
    onGameClick: (GameInfo) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(16.dp),
) {
    val configuration = LocalConfiguration.current
    val screenWidthDp = configuration.screenWidthDp

    val columns = when {
        screenWidthDp < 600 -> GridCells.Fixed(2)
        screenWidthDp < 840 -> GridCells.Fixed(3)
        screenWidthDp < 1200 -> GridCells.Fixed(4)
        else -> GridCells.Fixed(5)
    }

    LazyVerticalGrid(
        columns = columns,
        contentPadding = contentPadding,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier,
    ) {
        items(
            count = games.size,
            key = { games[it].id },
        ) { index ->
            GameCard(
                game = games[index],
                onClick = { onGameClick(games[index]) },
            )
        }
    }
}
```

### Step 4.5: Create HeroBanner.kt

**Create file:** `app/src/main/java/com/opencloudgaming/opennow/ui/components/HeroBanner.kt`

```kotlin
package com.opencloudgaming.opennow.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.opencloudgaming.opennow.GameInfo
import com.opencloudgaming.opennow.ui.theme.ButtonShape

@Composable
fun HeroBanner(
    game: GameInfo?,
    onPlay: () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (game == null) return

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp),
    ) {
        UrlImage(
            url = game.imageUrl,
            contentDescription = game.title,
            modifier = Modifier.fillMaxSize(),
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.7f)),
                    )
                ),
        )
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp),
        ) {
            Text(
                text = game.title,
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = onPlay,
                shape = ButtonShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                ),
            ) {
                Text("Play")
            }
        }
    }
}
```

### Step 4.6: Create SearchField.kt

**Create file:** `app/src/main/java/com/opencloudgaming/opennow/ui/components/SearchField.kt`

> **API NOTE:** Use text fallbacks for icons since `material-icons-core` is not added as a dependency.

```kotlin
package com.opencloudgaming.opennow.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.opencloudgaming.opennow.ui.theme.SearchBarShape
import com.opencloudgaming.opennow.ui.theme.Surface

@Composable
fun SearchField(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(false) }

    AnimatedVisibility(visible = expanded) {
        OutlinedTextField(
            value = query,
            onValueChange = onQueryChange,
            placeholder = { Text("Search games...") },
            trailingIcon = {
                TextButton(onClick = {
                    expanded = false
                    onQueryChange("")
                }) {
                    Text("X")
                }
            },
            shape = SearchBarShape,
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Surface,
                focusedBorderColor = MaterialTheme.colorScheme.primary,
            ),
            singleLine = true,
            modifier = modifier.fillMaxWidth(),
        )
    }

    AnimatedVisibility(visible = !expanded) {
        TextButton(onClick = { expanded = true }) {
            Text("Search")
        }
    }
}
```

### Step 4.7: Create EmptyState.kt

**Create file:** `app/src/main/java/com/opencloudgaming/opennow/ui/components/EmptyState.kt`

```kotlin
package com.opencloudgaming.opennow.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.opencloudgaming.opennow.ui.theme.OnSurfaceVariant

@Composable
fun EmptyState(
    message: String,
    onRetry: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            color = OnSurfaceVariant,
        )
        if (onRetry != null) {
            Spacer(modifier = Modifier.height(12.dp))
            Button(onClick = onRetry) {
                Text("Retry")
            }
        }
    }
}
```

### Step 4.8: Create SkeletonCard.kt

**Create file:** `app/src/main/java/com/opencloudgaming/opennow/ui/components/SkeletonCard.kt`

```kotlin
package com.opencloudgaming.opennow.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.opencloudgaming.opennow.ui.theme.CardShape
import com.opencloudgaming.opennow.ui.theme.Surface
import com.opencloudgaming.opennow.ui.theme.SurfaceVariant

@Composable
fun SkeletonCard(modifier: Modifier = Modifier) {
    Card(
        shape = CardShape,
        colors = CardDefaults.cardColors(containerColor = Surface),
        modifier = modifier,
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f)
                    .background(SurfaceVariant),
            )
            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(0.6f)
                    .height(14.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(SurfaceVariant),
            )
        }
    }
}
```

### Step 4.9: Create LoginScreen.kt

**Create file:** `app/src/main/java/com/opencloudgaming/opennow/ui/screens/login/LoginScreen.kt`

> **API NOTE:** Use `LoginProvider.displayName` (not `Provider.name`). Use `ExposedDropdownMenuAnchorType.PrimaryNotEditable` (not `MenuAnchorType.PrimaryNotEditable`).

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
import androidx.compose.material3.ButtonDefaults
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
import com.opencloudgaming.opennow.ui.theme.ButtonShape
import com.opencloudgaming.opennow.ui.theme.OnSurfaceVariant
import com.opencloudgaming.opennow.ui.theme.Primary

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
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "OpenNOW",
            style = MaterialTheme.typography.displayMedium,
        )
        Spacer(modifier = Modifier.height(32.dp))

        var expanded by remember { mutableStateOf(false) }
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = it },
        ) {
            OutlinedTextField(
                value = selectedProvider?.displayName ?: "Select provider",
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable),
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
            ) {
                providers.forEach { provider ->
                    DropdownMenuItem(
                        text = { Text(provider.displayName) },
                        onClick = {
                            onProviderSelected(provider)
                            expanded = false
                        },
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onLogin,
            enabled = selectedProvider != null && !isLoading,
            shape = ButtonShape,
            colors = ButtonDefaults.buttonColors(containerColor = Primary),
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(if (isLoading) "Signing in..." else "Sign in")
        }

        if (error != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = onTvLogin) {
            Text("Use TV code sign-in", color = OnSurfaceVariant)
        }
    }
}
```

### Step 4.10: Create HomeScreen.kt

**Create file:** `app/src/main/java/com/opencloudgaming/opennow/ui/screens/home/HomeScreen.kt`

> **API NOTE:** Use `verticalArrangement = Arrangement.spacedBy(...)`, NOT `verticalItemSpacing`.
> `isRefreshing` is managed as local UI state — not stored in CatalogState.

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
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.opencloudgaming.opennow.GameInfo
import com.opencloudgaming.opennow.ui.components.EmptyState
import com.opencloudgaming.opennow.ui.components.GameCard
import com.opencloudgaming.opennow.ui.components.HeroBanner
import com.opencloudgaming.opennow.ui.components.SearchField
import com.opencloudgaming.opennow.ui.components.SkeletonCard

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

    LaunchedEffect(isLoading) {
        if (!isLoading) isRefreshing = false
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
        ) {
            SearchField(query = search, onQueryChange = onSearchChange)
        }

        HeroBanner(
            game = lastPlayedGame,
            onPlay = { lastPlayedGame?.let { onPlayGame(it) } },
            modifier = Modifier.padding(horizontal = 16.dp),
        )

        if (isLoading && games.isEmpty()) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                items(6) { SkeletonCard() }
            }
        } else if (games.isEmpty() && !isLoading) {
            EmptyState(message = "No games found")
        } else {
            PullToRefreshBox(
                isRefreshing = isRefreshing,
                onRefresh = {
                    isRefreshing = true
                    onRefresh()
                },
            ) {
                com.opencloudgaming.opennow.ui.components.GameGrid(
                    games = games,
                    onGameClick = onGameClick,
                )
            }
        }
    }
}
```

### Step 4.11: Create LibraryScreen.kt

**Create file:** `app/src/main/java/com/opencloudgaming/opennow/ui/screens/library/LibraryScreen.kt`

```kotlin
package com.opencloudgaming.opennow.ui.screens.library

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.opencloudgaming.opennow.GameInfo
import com.opencloudgaming.opennow.ui.components.EmptyState
import com.opencloudgaming.opennow.ui.components.GameGrid
import com.opencloudgaming.opennow.ui.components.SearchField

@Composable
fun LibraryScreen(
    games: List<GameInfo>,
    search: String,
    isLoading: Boolean,
    onSearchChange: (String) -> Unit,
    onGameClick: (GameInfo) -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
        ) {
            SearchField(query = search, onQueryChange = onSearchChange)
        }

        if (games.isEmpty() && !isLoading) {
            EmptyState(message = "No games in your library")
        } else {
            GameGrid(
                games = games,
                onGameClick = onGameClick,
            )
        }
    }
}
```

---

## Phase 4 Expansion: Full SettingsScreen + Reusable Components

> **Replaces the skeleton SettingsScreen from Phase 4, Step 4.12.**

### Step 4.12: Create reusable settings components first

**Create file:** `app/src/main/java/com/opencloudgaming/opennow/ui/components/SettingsComponents.kt`

```kotlin
package com.opencloudgaming.opennow.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.opencloudgaming.opennow.ui.theme.OnSurfaceVariant
import com.opencloudgaming.opennow.ui.theme.Primary
import com.opencloudgaming.opennow.ui.theme.Surface
import com.opencloudgaming.opennow.ui.theme.SurfaceVariant

@Composable
fun SettingsSection(title: String, content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(Surface)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall,
            color = Primary,
        )
        content()
    }
}

@Composable
fun SettingSwitch(
    label: String,
    checked: Boolean,
    enabled: Boolean = true,
    onCheckedChange: (Boolean) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(SurfaceVariant)
            .clickable(enabled = enabled) { onCheckedChange(!checked) }
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(1f),
            color = if (enabled) OnSurfaceVariant else OnSurfaceVariant.copy(alpha = 0.5f),
        )
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            enabled = enabled,
            colors = SwitchDefaults.colors(
                checkedTrackColor = Primary,
            ),
        )
    }
}

@Composable
fun NumberSlider(
    label: String,
    value: Float,
    min: Float,
    max: Float,
    step: Float,
    onChange: (Float) -> Unit,
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(text = label, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.weight(1f))
            Text(
                text = "${value.roundToInt()}",
                style = MaterialTheme.typography.labelMedium,
                color = OnSurfaceVariant,
            )
        }
        Slider(
            value = value,
            onValueChange = onChange,
            valueRange = min..max,
            steps = ((max - min) / step).toInt() - 1,
            colors = SliderDefaults.colors(
                thumbColor = Primary,
                activeTrackColor = Primary,
            ),
        )
    }
}

@Composable
fun ChoiceRow(
    label: String,
    options: List<String>,
    selected: String,
    onSelect: (String) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(SurfaceVariant)
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Text(text = label, style = MaterialTheme.typography.bodyMedium)
        options.forEach { option ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { onSelect(option) }
                    .padding(vertical = 8.dp, horizontal = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier
                        .padding(end = 10.dp)
                        .size(14.dp)
                        .clip(RoundedCornerShape(7.dp))
                        .background(if (option == selected) Primary else OnSurfaceVariant.copy(alpha = 0.3f)),
                )
                Text(text = option, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

@Composable
fun <T> ChoiceMenuRow(
    label: String,
    options: List<T>,
    selectedLabel: String,
    onOptionSelected: (T) -> Unit,
    optionLabel: (T) -> String = { it.toString() },
    optionEnabled: (T) -> Boolean = { true },
    optionBadge: (T) -> String? = { null },
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(SurfaceVariant)
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Text(text = label, style = MaterialTheme.typography.bodyMedium)
        options.forEach { option ->
            val enabled = optionEnabled(option)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .clickable(enabled = enabled) { onOptionSelected(option) }
                    .padding(vertical = 8.dp, horizontal = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier
                        .padding(end = 10.dp)
                        .size(14.dp)
                        .clip(RoundedCornerShape(7.dp))
                        .background(
                            if (optionLabel(option) == selectedLabel) Primary
                            else OnSurfaceVariant.copy(alpha = 0.3f),
                        ),
                )
                Text(
                    text = optionLabel(option),
                    style = MaterialTheme.typography.bodySmall,
                    color = if (enabled) OnSurfaceVariant else OnSurfaceVariant.copy(alpha = 0.5f),
                    modifier = Modifier.weight(1f),
                )
                optionBadge(option)?.let { badge ->
                    Text(
                        text = badge,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.error,
                    )
                }
            }
        }
    }
}
```

### Step 4.13: Create full SettingsScreen

**Create file:** `app/src/main/java/com/opencloudgaming/opennow/ui/screens/settings/SettingsScreen.kt`

> **Replaces the skeleton from Step 4.12. This is the FULL implementation.**

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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.opencloudgaming.opennow.ui.state.SettingsCategory
import com.opencloudgaming.opennow.ui.theme.OnSurfaceVariant
import com.opencloudgaming.opennow.ui.theme.Primary
import com.opencloudgaming.opennow.ui.theme.Surface
import com.opencloudgaming.opennow.ui.theme.SurfaceVariant
import com.opencloudgaming.opennow.ui.components.SettingsSection
import com.opencloudgaming.opennow.ui.components.SettingSwitch
import com.opencloudgaming.opennow.ui.components.NumberSlider
import com.opencloudgaming.opennow.ui.components.ChoiceRow
import com.opencloudgaming.opennow.ui.components.ChoiceMenuRow

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
            text = { Text("Cached store, library, and search results will be removed. Your account and settings stay unchanged.") },
            confirmButton = {
                Button(onClick = { showClearCacheDialog = false; onClearCache() }) {
                    Text("Clear cache")
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearCacheDialog = false }) { Text("Cancel") }
            },
        )
    }
    if (showResetDialog) {
        AlertDialog(
            onDismissRequest = { showResetDialog = false },
            title = { Text("Reset settings?") },
            text = { Text("Stream, input, interface, and controller preferences will return to recommended defaults.") },
            confirmButton = {
                Button(onClick = { showResetDialog = false; onResetSettings() }) {
                    Text("Reset settings")
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetDialog = false }) { Text("Cancel") }
            },
        )
    }

    val configuration = LocalConfiguration.current
    val screenWidthDp = configuration.screenWidthDp
    val useSidebarLayout = screenWidthDp >= 600

    if (useSidebarLayout) {
        // Tablet/landscape: sidebar + detail
        Row(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                modifier = Modifier
                    .weight(0.3f)
                    .background(Surface),
            ) {
                items(SettingsCategory.entries) { category ->
                    Text(
                        text = category.label,
                        style = MaterialTheme.typography.bodyLarge,
                        color = if (category == selectedCategory) Primary else OnSurfaceVariant,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onCategorySelected(category) }
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                    )
                }
            }

            LazyColumn(
                modifier = Modifier
                    .weight(0.7f)
                    .background(SurfaceVariant)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                item {
                    Text(
                        text = selectedCategory.label,
                        style = MaterialTheme.typography.headlineSmall,
                    )
                }
                item {
                    when (selectedCategory) {
                        SettingsCategory.Stream -> StreamSettingsPanel(
                            settings = settings,
                            onUpdateSettings = onUpdateSettings,
                        )
                        SettingsCategory.Input -> InputSettingsPanel(
                            settings = settings,
                            onUpdateSettings = onUpdateSettings,
                        )
                        SettingsCategory.Interface -> InterfaceSettingsPanel(
                            settings = settings,
                            onUpdateSettings = onUpdateSettings,
                        )
                        SettingsCategory.Account -> AccountSettingsPanel(
                            onLogout = onLogout,
                            onLogoutAll = onLogoutAll,
                            onSwitchAccount = onSwitchAccount,
                        )
                        SettingsCategory.Debug -> DebugSettingsPanel(
                            onClearCache = { showClearCacheDialog = true },
                            onResetSettings = { showResetDialog = true },
                        )
                    }
                }
            }
        }
    } else {
        // Phone portrait: tabs at top, content below
        Column(modifier = Modifier.fillMaxSize()) {
            // Category tabs
            androidx.compose.foundation.lazy.LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Surface)
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                items(SettingsCategory.entries) { category ->
                    androidx.compose.material3.FilterChip(
                        selected = category == selectedCategory,
                        onClick = { onCategorySelected(category) },
                        label = { Text(category.label) },
                        colors = androidx.compose.material3.FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Primary.copy(alpha = 0.16f),
                            selectedLabelColor = Primary,
                        ),
                    )
                }
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(SurfaceVariant)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                item {
                    when (selectedCategory) {
                        SettingsCategory.Stream -> StreamSettingsPanel(
                            settings = settings,
                            onUpdateSettings = onUpdateSettings,
                        )
                        SettingsCategory.Input -> InputSettingsPanel(
                            settings = settings,
                            onUpdateSettings = onUpdateSettings,
                        )
                        SettingsCategory.Interface -> InterfaceSettingsPanel(
                            settings = settings,
                            onUpdateSettings = onUpdateSettings,
                        )
                        SettingsCategory.Account -> AccountSettingsPanel(
                            onLogout = onLogout,
                            onLogoutAll = onLogoutAll,
                            onSwitchAccount = onSwitchAccount,
                        )
                        SettingsCategory.Debug -> DebugSettingsPanel(
                            onClearCache = { showClearCacheDialog = true },
                            onResetSettings = { showResetDialog = true },
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun StreamSettingsPanel(
    settings: com.opencloudgaming.opennow.AppSettings,
    onUpdateSettings: (com.opencloudgaming.opennow.AppSettings) -> Unit,
) {
    val stream = settings.stream
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        SettingsSection("Video") {
            ChoiceRow(
                label = "Codec",
                options = com.opencloudgaming.opennow.VideoCodec.entries.map { it.name },
                selected = stream.codec.name,
            ) { onUpdateSettings(settings.copy(stream = stream.copy(codec = com.opencloudgaming.opennow.VideoCodec.valueOf(it)))) }
            ChoiceRow(
                label = "Color quality",
                options = com.opencloudgaming.opennow.ColorQuality.entries.map { it.name },
                selected = stream.colorQuality.name,
            ) { onUpdateSettings(settings.copy(stream = stream.copy(colorQuality = com.opencloudgaming.opennow.ColorQuality.valueOf(it)))) }
            NumberSlider("FPS", stream.fps.toFloat(), 30f, 240f, 30f) {
                onUpdateSettings(settings.copy(stream = stream.copy(fps = it.roundToInt())))
            }
            NumberSlider("Max bitrate (Mbps)", stream.maxBitrateMbps.toFloat(), 1f, 150f, 1f) {
                onUpdateSettings(settings.copy(stream = stream.copy(maxBitrateMbps = it.roundToInt())))
            }
            SettingSwitch("HDR", stream.hdrEnabled) {
                onUpdateSettings(settings.copy(stream = stream.copy(hdrEnabled = it)))
            }
        }
        SettingsSection("Enhancements") {
            SettingSwitch("Stream sharpening", stream.streamSharpeningEnabled) {
                onUpdateSettings(settings.copy(stream = stream.copy(streamSharpeningEnabled = it)))
            }
            if (stream.streamSharpeningEnabled) {
                NumberSlider("Sharpness", stream.streamSharpeningAmount, 0f, 1f, 0.05f) {
                    onUpdateSettings(settings.copy(stream = stream.copy(streamSharpeningAmount = it)))
                }
            }
            SettingSwitch("Clarity", stream.streamClarityEnabled) {
                onUpdateSettings(settings.copy(stream = stream.copy(streamClarityEnabled = it)))
            }
            if (stream.streamClarityEnabled) {
                NumberSlider("Clarity", stream.streamClarityAmount, 0f, 1f, 0.05f) {
                    onUpdateSettings(settings.copy(stream = stream.copy(streamClarityAmount = it)))
                }
            }
            SettingSwitch("Contrast", stream.streamContrastEnabled) {
                onUpdateSettings(settings.copy(stream = stream.copy(streamContrastEnabled = it)))
            }
            if (stream.streamContrastEnabled) {
                NumberSlider("Contrast", stream.streamContrastAmount, 0f, 1f, 0.05f) {
                    onUpdateSettings(settings.copy(stream = stream.copy(streamContrastAmount = it)))
                }
            }
        }
        SettingsSection("Network") {
            SettingSwitch("Session proxy", stream.sessionProxyEnabled) {
                onUpdateSettings(settings.copy(stream = stream.copy(sessionProxyEnabled = it)))
            }
            if (stream.sessionProxyEnabled) {
                OutlinedTextField(
                    value = stream.sessionProxyUrl,
                    onValueChange = { onUpdateSettings(settings.copy(stream = stream.copy(sessionProxyUrl = it))) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    label = { Text("Proxy URL") },
                    placeholder = { Text("http://127.0.0.1:8080") },
                )
            }
            SettingSwitch("L4S", stream.enableL4S) {
                onUpdateSettings(settings.copy(stream = stream.copy(enableL4S = it)))
            }
            SettingSwitch("Cloud G-Sync", stream.enableCloudGsync) {
                onUpdateSettings(settings.copy(stream = stream.copy(enableCloudGsync = it)))
            }
        }
    }
}

@Composable
private fun InputSettingsPanel(
    settings: com.opencloudgaming.opennow.AppSettings,
    onUpdateSettings: (com.opencloudgaming.opennow.AppSettings) -> Unit,
) {
    val stream = settings.stream
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        SettingsSection("Mouse") {
            NumberSlider("Mouse sensitivity", stream.mouseSensitivity, 0.25f, 3f, 0.05f) {
                onUpdateSettings(settings.copy(stream = stream.copy(mouseSensitivity = it)))
            }
            NumberSlider("Mouse acceleration", stream.mouseAcceleration.toFloat(), 1f, 150f, 1f) {
                onUpdateSettings(settings.copy(stream = stream.copy(mouseAcceleration = it.roundToInt())))
            }
        }
        SettingsSection("Touch Controls") {
            SettingSwitch("Touch controls", settings.androidTouch.enabled) {
                onUpdateSettings(settings.copy(androidTouch = settings.androidTouch.copy(enabled = it)))
            }
            SettingSwitch("Finger mouse", settings.androidTouch.mousePad) {
                onUpdateSettings(settings.copy(androidTouch = settings.androidTouch.copy(mousePad = it)))
            }
            NumberSlider("Layout scale", settings.androidTouch.scale, 0.6f, 1.4f, 0.05f) {
                onUpdateSettings(settings.copy(androidTouch = settings.androidTouch.copy(scale = it)))
            }
            NumberSlider("Button size", settings.androidTouch.buttonScale, 0.65f, 1.5f, 0.05f) {
                onUpdateSettings(settings.copy(androidTouch = settings.androidTouch.copy(buttonScale = it)))
            }
            NumberSlider("Stick size", settings.androidTouch.stickScale, 0.65f, 1.5f, 0.05f) {
                onUpdateSettings(settings.copy(androidTouch = settings.androidTouch.copy(stickScale = it)))
            }
            NumberSlider("Opacity", settings.androidTouch.opacity, 0.15f, 1f, 0.05f) {
                onUpdateSettings(settings.copy(androidTouch = settings.androidTouch.copy(opacity = it)))
            }
            NumberSlider("Edge padding", settings.androidTouch.edgePaddingDp, 0f, 72f, 1f) {
                onUpdateSettings(settings.copy(androidTouch = settings.androidTouch.copy(edgePaddingDp = it)))
            }
            NumberSlider("Bottom padding", settings.androidTouch.bottomPaddingDp, 0f, 120f, 1f) {
                onUpdateSettings(settings.copy(androidTouch = settings.androidTouch.copy(bottomPaddingDp = it)))
            }
        }
        SettingsSection("Other") {
            SettingSwitch("Clipboard paste", settings.clipboardPaste) {
                onUpdateSettings(settings.copy(clipboardPaste = it))
            }
            SettingSwitch("Phone rumble fallback", settings.phoneRumbleFallback) {
                onUpdateSettings(settings.copy(phoneRumbleFallback = it))
            }
        }
    }
}

@Composable
private fun InterfaceSettingsPanel(
    settings: com.opencloudgaming.opennow.AppSettings,
    onUpdateSettings: (com.opencloudgaming.opennow.AppSettings) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        SettingsSection("Appearance") {
            SettingSwitch("Dynamic color (Material You)", settings.dynamicColor) {
                onUpdateSettings(settings.copy(dynamicColor = it))
            }
            ChoiceRow(
                label = "Accent color",
                options = com.opencloudgaming.opennow.UiAccent.entries.map { it.name },
                selected = settings.uiAccent.name,
            ) { onUpdateSettings(settings.copy(uiAccent = com.opencloudgaming.opennow.UiAccent.valueOf(it))) }
            SettingSwitch("Compact game cards", settings.compactGameCards) {
                onUpdateSettings(settings.copy(compactGameCards = it))
            }
            SettingSwitch("Show store labels", settings.showGameStoreLabels) {
                onUpdateSettings(settings.copy(showGameStoreLabels = it))
            }
            NumberSlider("Card size", settings.posterSizeScale, 0.82f, 1.08f, 0.02f) {
                onUpdateSettings(settings.copy(posterSizeScale = it))
            }
        }
        SettingsSection("Stream UI") {
            SettingSwitch("Show stats on launch", settings.showStatsOnLaunch) {
                onUpdateSettings(settings.copy(showStatsOnLaunch = it))
            }
            SettingSwitch("Hide server selector", settings.hideServerSelector) {
                onUpdateSettings(settings.copy(hideServerSelector = it))
            }
        }
        SettingsSection("Controller") {
            SettingSwitch("Controller mode", settings.controllerMode) {
                onUpdateSettings(settings.copy(controllerMode = it))
            }
            SettingSwitch("UI sounds", settings.controllerUiSounds) {
                onUpdateSettings(settings.copy(controllerUiSounds = it))
            }
            SettingSwitch("Background animations", settings.controllerBackgroundAnimations) {
                onUpdateSettings(settings.copy(controllerBackgroundAnimations = it))
            }
        }
    }
}

@Composable
private fun AccountSettingsPanel(
    onLogout: () -> Unit,
    onLogoutAll: () -> Unit,
    onSwitchAccount: (String) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        SettingsSection("Account") {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = {}, modifier = Modifier.weight(1f)) { Text("Add account") }
                OutlinedButton(onClick = onLogout, modifier = Modifier.weight(1f)) { Text("Sign out") }
            }
            OutlinedButton(onClick = onLogoutAll, modifier = Modifier.fillMaxWidth()) {
                Text("Sign out all accounts")
            }
        }
    }
}

@Composable
private fun DebugSettingsPanel(
    onClearCache: () -> Unit,
    onResetSettings: () -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        SettingsSection("App Data") {
            Text(
                "Recommended defaults keep touch controls, fullscreen recovery, dynamic color, compact cards, and controller polish on.",
                style = MaterialTheme.typography.bodySmall,
                color = OnSurfaceVariant,
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(onClick = onClearCache, modifier = Modifier.weight(1f)) { Text("Clear cache") }
                OutlinedButton(onClick = onResetSettings, modifier = Modifier.weight(1f)) { Text("Reset settings") }
            }
        }
        SettingsSection("About") {
            Text("OpenNOW Android", style = MaterialTheme.typography.bodyLarge)
            Text("Version 0.5.1", style = MaterialTheme.typography.bodySmall, color = OnSurfaceVariant)
        }
    }
}
```

---

## Phase 4 Expansion: GameDetailsSheet

### Step 4.14: Create GameDetailsSheet

**Create file:** `app/src/main/java/com/opencloudgaming/opennow/ui/components/GameDetailsSheet.kt`

```kotlin
package com.opencloudgaming.opennow.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.opencloudgaming.opennow.GameInfo
import com.opencloudgaming.opennow.ui.theme.ButtonShape
import com.opencloudgaming.opennow.ui.theme.OnSurfaceVariant
import com.opencloudgaming.opennow.ui.theme.Primary
import com.opencloudgaming.opennow.ui.theme.Surface as Panel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun GameDetailsSheet(
    game: GameInfo,
    isFavorite: Boolean,
    onPlay: (GameInfo) -> Unit,
    onFavorite: (String) -> Unit,
    onDismiss: () -> Unit,
) {
    Box(
        Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.72f))
            .clickable(onClick = onDismiss),
        contentAlignment = Alignment.BottomCenter,
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.92f)
                .clickable(onClick = {}),
            shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
            color = Panel,
            tonalElevation = 8.dp,
        ) {
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState()),
            ) {
                // Header image
                UrlImage(
                    url = game.screenshotUrl ?: game.imageUrl,
                    contentDescription = game.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(16f / 9f)
                        .clip(RoundedCornerShape(16.dp)),
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Title + favorite
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = game.title,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Text(
                        text = if (isFavorite) "Unfavorite" else "Favorite",
                        color = Primary,
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier.clickable { onFavorite(game.id) },
                    )
                }

                // Publisher
                game.publisherName?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodySmall,
                        color = OnSurfaceVariant,
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))

                // Description
                Text(
                    text = game.description ?: "No description available.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (game.description != null) OnSurfaceVariant else OnSurfaceVariant.copy(alpha = 0.5f),
                    maxLines = 6,
                    overflow = TextOverflow.Ellipsis,
                )
                Spacer(modifier = Modifier.height(12.dp))

                // Genre chips
                if (game.genres.isNotEmpty()) {
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                    ) {
                        game.genres.take(6).forEach { genre ->
                            AssistChip(onClick = {}, label = { Text(genre) })
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }

                // Play button
                Button(
                    onClick = { onPlay(game) },
                    shape = ButtonShape,
                    colors = ButtonDefaults.buttonColors(containerColor = Primary),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text("Play")
                }
                Spacer(modifier = Modifier.height(8.dp))

                // Dismiss
                OutlinedButton(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text("Close")
                }
            }
        }
    }
}
```

---

## Phase 4 Expansion: Store Components

> Store composables (`SortPicker`, `SelectedFilterChips`, `FilterMenu`, `StoreLaunchSelector`, `LaunchOptionsList`, `LongPressPlayButton`, `CompactDetailRows`, `DetailRows`, `FavoriteIconButton`, `ImageCloseButton`, `ThumbnailStoreButton`, `ThumbnailPlayButton`, `AnimatedLaunchOverlay`) should be moved from `OpenNowScreens.kt` into `app/src/main/java/com/opencloudgaming/opennow/ui/components/StoreComponents.kt`.

---

## Phase 4 Expansion: TvDeviceLogin

> `TvDeviceLoginScreen`, `DeviceLoginPanel`, `DeviceLoginQr`, `DeviceLoginControls`, `QrCodeView` should be moved from `OpenNowScreens.kt` into `app/src/main/java/com/opencloudgaming/opennow/ui/screens/login/TvDeviceLogin.kt`.
