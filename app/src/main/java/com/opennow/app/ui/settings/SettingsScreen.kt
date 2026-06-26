package com.opennow.app.ui.settings

import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.unit.dp
import com.opennow.app.data.model.AppSettings
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    settings: AppSettings,
    onSettingsChange: (AppSettings) -> Unit,
    onReset: () -> Unit = {},
    onBack: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    LazyColumn(
        state = listState,
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .onPreviewKeyEvent { event ->
                if (event.type != KeyEventType.KeyDown) return@onPreviewKeyEvent false
                when (event.key) {
                    Key.DirectionUp -> {
                        coroutineScope.launch {
                            val firstVisible = listState.firstVisibleItemIndex
                            if (firstVisible > 0) {
                                listState.animateScrollToItem(firstVisible - 1)
                            }
                        }
                        true
                    }
                    Key.DirectionDown -> {
                        coroutineScope.launch {
                            val lastVisible = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
                            val totalItems = listState.layoutInfo.totalItemsCount
                            if (lastVisible < totalItems - 1) {
                                listState.animateScrollToItem(lastVisible)
                            }
                        }
                        true
                    }
                    else -> false
                }
            },
    ) {
        item {
            Text(
                text = "Stream Settings",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(top = 16.dp),
            )
        }

        item {
            DropdownSetting(
                label = "Resolution",
                options = listOf("1280x720", "1920x1080", "2560x1440", "3840x2160"),
                selected = settings.resolution,
                onSelect = { onSettingsChange(settings.copy(resolution = it)) },
            )
        }

        item {
            DropdownSetting(
                label = "FPS",
                options = listOf("30", "60", "120"),
                selected = settings.fps.toString(),
                onSelect = { onSettingsChange(settings.copy(fps = it.toInt())) },
            )
        }

        item {
            DropdownSetting(
                label = "Codec",
                options = listOf("H264", "H265", "AV1"),
                selected = settings.codec,
                onSelect = { onSettingsChange(settings.copy(codec = it)) },
            )
        }

        item {
            SliderSetting(
                label = "Max Bitrate",
                value = settings.maxBitrateMbps.toFloat(),
                valueRange = 5f..100f,
                onValueChange = { onSettingsChange(settings.copy(maxBitrateMbps = it.toInt())) },
            )
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            Text(
                text = "Audio",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
            )
        }

        item {
            DropdownSetting(
                label = "Microphone",
                options = listOf("disabled", "push-to-talk", "voice-activity"),
                selected = settings.microphoneMode,
                onSelect = { onSettingsChange(settings.copy(microphoneMode = it)) },
            )
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            Text(
                text = "Controls",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
            )
        }

        item {
            DropdownSetting(
                label = "Keyboard Layout",
                options = listOf("en-US", "en-GB", "de-DE", "fr-FR", "ja-JP"),
                selected = settings.keyboardLayout,
                onSelect = { onSettingsChange(settings.copy(keyboardLayout = it)) },
            )
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            Text(
                text = "About",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
            )
        }

        item {
            Text(
                text = "App version: 0.1.0",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(vertical = 8.dp),
            )
        }

        item {
            Text(
                text = "OpenNOW Android",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 16.dp),
            )
        }

        item {
            Button(
                onClick = onReset,
                modifier = Modifier.fillMaxWidth().padding(bottom = 32.dp),
            ) {
                Text("Reset to defaults")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DropdownSetting(
    label: String,
    options: List<String>,
    selected: String,
    onSelect: (String) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .focusable(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f),
        )

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = it },
        ) {
            TextField(
                value = selected,
                onValueChange = {},
                readOnly = true,
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                modifier = Modifier
                    .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                    .weight(1f)
                    .focusable(),
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            onSelect(option)
                            expanded = false
                        },
                    )
                }
            }
        }
    }
}

@Composable
private fun SliderSetting(
    label: String,
    value: Float,
    valueRange: ClosedFloatingPointRange<Float>,
    onValueChange: (Float) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.weight(1f),
            )

            Text(
                text = "${value.toInt()} Mbps",
                style = MaterialTheme.typography.bodyMedium,
            )
        }

        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = valueRange,
            modifier = Modifier
                .fillMaxWidth()
                .focusable(),
        )
    }
}
