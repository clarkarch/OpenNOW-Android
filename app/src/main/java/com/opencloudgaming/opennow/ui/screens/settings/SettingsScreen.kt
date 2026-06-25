package com.opencloudgaming.opennow.ui.screens.settings

import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.opencloudgaming.opennow.AuthSession
import com.opencloudgaming.opennow.BuildConfig
import com.opencloudgaming.opennow.CodecCapability
import com.opencloudgaming.opennow.ColorQuality
import com.opencloudgaming.opennow.OpenNowUiState
import com.opencloudgaming.opennow.OpenNowViewModel
import com.opencloudgaming.opennow.R
import com.opencloudgaming.opennow.RuntimeCodecReport
import com.opencloudgaming.opennow.SavedAccount
import com.opencloudgaming.opennow.StreamStatsStyle
import com.opencloudgaming.opennow.UiAccent
import com.opencloudgaming.opennow.VideoCodec
import com.opencloudgaming.opennow.hasUltimateStreamingPlan
import com.opencloudgaming.opennow.isTvActivateKey
import com.opencloudgaming.opennow.normalizeStreamResolutionForAspect
import com.opencloudgaming.opennow.streamAspectRatioOptions
import com.opencloudgaming.opennow.streamResolutionChoicesForAspect
import com.opencloudgaming.opennow.ui.theme.SettingsBackground
import com.opencloudgaming.opennow.ui.theme.SettingsPanel
import com.opencloudgaming.opennow.ui.theme.SettingsPanelAlt
import com.opencloudgaming.opennow.ui.theme.SettingsText
import com.opencloudgaming.opennow.ui.theme.SettingsTextMuted
import com.opencloudgaming.opennow.ui.theme.uiAccentLabel
import kotlin.math.roundToInt

internal data class SettingsChoiceOption(val value: String, val label: String)
internal data class ChoiceMenuOption(
    val value: String,
    val label: String,
    val enabled: Boolean = true,
    val badge: String? = null,
)


internal val keyboardLayoutOptions = listOf(
    SettingsChoiceOption("en-US", "English (US)"),
    SettingsChoiceOption("en-GB", "English (UK)"),
    SettingsChoiceOption("tr-TR", "Turkish Q"),
    SettingsChoiceOption("de-DE", "German"),
    SettingsChoiceOption("fr-FR", "French"),
    SettingsChoiceOption("es-ES", "Spanish"),
    SettingsChoiceOption("es-MX", "Spanish (Latin America)"),
    SettingsChoiceOption("it-IT", "Italian"),
    SettingsChoiceOption("pt-PT", "Portuguese (Portugal)"),
    SettingsChoiceOption("pt-BR", "Portuguese (Brazil)"),
    SettingsChoiceOption("pl-PL", "Polish"),
    SettingsChoiceOption("ru-RU", "Russian"),
    SettingsChoiceOption("ja-JP", "Japanese"),
    SettingsChoiceOption("ko-KR", "Korean"),
    SettingsChoiceOption("zh-CN", "Chinese (Simplified)"),
    SettingsChoiceOption("zh-TW", "Chinese (Traditional)"),
)

internal val gameLanguageOptions = listOf(
    SettingsChoiceOption("en_US", "English (US)"),
    SettingsChoiceOption("en_GB", "English (UK)"),
    SettingsChoiceOption("de_DE", "Deutsch"),
    SettingsChoiceOption("fr_FR", "Francais"),
    SettingsChoiceOption("es_ES", "Espanol (ES)"),
    SettingsChoiceOption("es_MX", "Espanol (MX)"),
    SettingsChoiceOption("it_IT", "Italiano"),
    SettingsChoiceOption("pt_PT", "Portugues (PT)"),
    SettingsChoiceOption("pt_BR", "Portugues (BR)"),
    SettingsChoiceOption("ru_RU", "Russian"),
    SettingsChoiceOption("pl_PL", "Polish"),
    SettingsChoiceOption("tr_TR", "Turkish"),
    SettingsChoiceOption("ar_SA", "Arabic"),
    SettingsChoiceOption("ja_JP", "Japanese"),
    SettingsChoiceOption("ko_KR", "Korean"),
    SettingsChoiceOption("zh_CN", "Chinese (Simplified)"),
    SettingsChoiceOption("zh_TW", "Chinese (Traditional)"),
    SettingsChoiceOption("th_TH", "Thai"),
    SettingsChoiceOption("vi_VN", "Vietnamese"),
    SettingsChoiceOption("id_ID", "Indonesian"),
    SettingsChoiceOption("cs_CZ", "Czech"),
    SettingsChoiceOption("el_GR", "Greek"),
    SettingsChoiceOption("hu_HU", "Hungarian"),
    SettingsChoiceOption("ro_RO", "Romanian"),
    SettingsChoiceOption("uk_UA", "Ukrainian"),
    SettingsChoiceOption("nl_NL", "Dutch"),
    SettingsChoiceOption("sv_SE", "Swedish"),
    SettingsChoiceOption("da_DK", "Danish"),
    SettingsChoiceOption("fi_FI", "Finnish"),
    SettingsChoiceOption("no_NO", "Norwegian"),
)

@Composable
internal fun SettingsScreen(state: OpenNowUiState, viewModel: OpenNowViewModel, tvProfile: Boolean) {
    var showSessionProxyWarning by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()
    val focusManager = LocalFocusManager.current
    if (showSessionProxyWarning) {
        SessionProxyWarningDialog(
            onCancel = { showSessionProxyWarning = false },
            onEnable = {
                viewModel.updateStreamSettings { s -> s.copy(sessionProxyEnabled = true) }
                showSessionProxyWarning = false
            },
        )
    }
    if (tvProfile) {
        Column(
            Modifier
                .fillMaxSize()
                .background(SettingsBackground)
                .onPreviewKeyEvent { handleVerticalDpadFocusMove(it, focusManager) }
                .verticalScroll(scrollState)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            SettingsContent(state = state, viewModel = viewModel, showSessionProxyWarning = { showSessionProxyWarning = true })
        }
    } else {
        LazyColumn(
            Modifier
                .fillMaxSize()
                .background(SettingsBackground),
            contentPadding = PaddingValues(14.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            item {
                SettingsContent(state = state, viewModel = viewModel, showSessionProxyWarning = { showSessionProxyWarning = true })
            }
        }
    }
}

@Composable
internal fun SettingsContent(
    state: OpenNowUiState,
    viewModel: OpenNowViewModel,
    showSessionProxyWarning: () -> Unit,
) {
    val settings = state.settings
    Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
    SettingsSection(stringResource(R.string.settings_section_stream)) {
                val fallbackMembershipTier = state.authSession?.user?.membershipTier
                val resolutionChoices = streamResolutionChoicesForAspect(settings.stream.aspectRatio).ifEmpty {
                    streamResolutionChoicesForAspect("16:9")
                }
                val selectedResolution = normalizeStreamResolutionForAspect(settings.stream.resolution, settings.stream.aspectRatio)
                ChoiceMenuRow(
                    label = stringResource(R.string.settings_resolution),
                    options = resolutionChoices.map { choice ->
                        val available = choice.isAvailableFor(state.subscriptionInfo, fallbackMembershipTier)
                        ChoiceMenuOption(
                            value = choice.value,
                            label = choice.label,
                            enabled = available,
                            badge = if (available) null else choice.requiredPlanLabel,
                        )
                    },
                    selectedLabel = resolutionChoices.firstOrNull { it.value == selectedResolution }?.label ?: selectedResolution,
                ) {
                    viewModel.updateStreamSettings { s -> s.copy(resolution = it) }
                }
                ChoiceMenuRow(
                    label = stringResource(R.string.settings_aspect_ratio),
                    options = streamAspectRatioOptions().map { aspectRatio ->
                        val choices = streamResolutionChoicesForAspect(aspectRatio)
                        val available = choices.any { it.isAvailableFor(state.subscriptionInfo, fallbackMembershipTier) }
                        ChoiceMenuOption(
                            value = aspectRatio,
                            label = aspectRatio,
                            enabled = available,
                            badge = if (available) null else choices.firstNotNullOfOrNull { it.requiredPlanLabel },
                        )
                    },
                    selectedLabel = settings.stream.aspectRatio,
                ) {
                    viewModel.updateStreamSettings { s ->
                        s.copy(
                            aspectRatio = it,
                            resolution = normalizeStreamResolutionForAspect(s.resolution, it),
                        )
                    }
                }
                NumberSlider(stringResource(R.string.settings_fps), settings.stream.fps.toFloat(), 30f, 240f, 30f) {
                    viewModel.updateStreamSettings { s -> s.copy(fps = it.roundToInt()) }
                }
                NumberSlider(stringResource(R.string.settings_bitrate), settings.stream.maxBitrateMbps.toFloat(), 1f, 150f, 1f) {
                    viewModel.updateStreamSettings { s -> s.copy(maxBitrateMbps = it.roundToInt()) }
                }
                ChoiceRow(stringResource(R.string.settings_codec), VideoCodec.entries.map { it.name }, settings.stream.codec.name) {
                    viewModel.updateStreamSettings { s -> s.copy(codec = VideoCodec.valueOf(it)) }
                }
                ChoiceRow(stringResource(R.string.settings_color), ColorQuality.entries.map { it.label }, settings.stream.colorQuality.label) { label ->
                    viewModel.updateStreamSettings { s -> s.copy(colorQuality = ColorQuality.entries.first { it.label == label }) }
                }
                val hdrAvailable = hasUltimateStreamingPlan(state.subscriptionInfo, fallbackMembershipTier)
                SettingSwitch(
                    stringResource(R.string.settings_hdr),
                    settings.stream.hdrEnabled && hdrAvailable,
                    enabled = hdrAvailable,
                ) { enabled ->
                    viewModel.updateStreamSettings { s ->
                        s.copy(
                            hdrEnabled = enabled,
                            colorQuality = if (enabled && !s.colorQuality.name.startsWith("TenBit")) ColorQuality.TenBit420 else s.colorQuality,
                        )
                    }
                }
                SettingSwitch("Stream sharpening", settings.stream.streamSharpeningEnabled) {
                    viewModel.updateStreamSettings { s -> s.copy(streamSharpeningEnabled = it) }
                }
                if (settings.stream.streamSharpeningEnabled) {
                    NumberSlider("Sharpness amount", settings.stream.streamSharpeningAmount, 0f, 1f, 0.05f) {
                        viewModel.updateStreamSettings { s -> s.copy(streamSharpeningAmount = it) }
                    }
                }
                SettingSwitch("Clarity", settings.stream.streamClarityEnabled) {
                    viewModel.updateStreamSettings { s -> s.copy(streamClarityEnabled = it) }
                }
                if (settings.stream.streamClarityEnabled) {
                    NumberSlider("Clarity amount", settings.stream.streamClarityAmount, 0f, 1f, 0.05f) {
                        viewModel.updateStreamSettings { s -> s.copy(streamClarityAmount = it) }
                    }
                }
                SettingSwitch("Contrast", settings.stream.streamContrastEnabled) {
                    viewModel.updateStreamSettings { s -> s.copy(streamContrastEnabled = it) }
                }
                if (settings.stream.streamContrastEnabled) {
                    NumberSlider("Contrast amount", settings.stream.streamContrastAmount, 0f, 1f, 0.05f) {
                        viewModel.updateStreamSettings { s -> s.copy(streamContrastAmount = it) }
                    }
                }
                ChoiceRow(stringResource(R.string.settings_region), listOf(stringResource(R.string.option_auto)) + state.regions.map { it.name }, state.regions.firstOrNull { it.url == settings.stream.region }?.name ?: stringResource(R.string.option_auto)) { label ->
                    val url = state.regions.firstOrNull { it.name == label }?.url.orEmpty()
                    viewModel.updateStreamSettings { s -> s.copy(region = url) }
                }
                SettingSwitch(stringResource(R.string.settings_session_proxy), settings.stream.sessionProxyEnabled) { enabled ->
                    if (enabled) {
                        showSessionProxyWarning()
                    } else {
                        viewModel.updateStreamSettings { s -> s.copy(sessionProxyEnabled = false) }
                    }
                }
                Text(
                    stringResource(R.string.settings_session_proxy_hint),
                    color = SettingsTextMuted,
                    style = MaterialTheme.typography.bodySmall,
                )
                if (settings.stream.sessionProxyEnabled) {
                    OutlinedTextField(
                        value = settings.stream.sessionProxyUrl,
                        onValueChange = { value -> viewModel.updateStreamSettings { s -> s.copy(sessionProxyUrl = value) } },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        label = { Text(stringResource(R.string.settings_session_proxy_url)) },
                        placeholder = { Text("http://127.0.0.1:8080") },
                    )
                }
                SettingSwitch(stringResource(R.string.settings_l4s), settings.stream.enableL4S) { viewModel.updateStreamSettings { s -> s.copy(enableL4S = it) } }
                SettingSwitch(stringResource(R.string.settings_cloud_gsync), settings.stream.enableCloudGsync) { viewModel.updateStreamSettings { s -> s.copy(enableCloudGsync = it) } }
            }
    SettingsSection("Input") {
                NumberSlider("Mouse sensitivity", settings.stream.mouseSensitivity, 0.25f, 3f, 0.05f) {
                    viewModel.updateStreamSettings { s -> s.copy(mouseSensitivity = it) }
                }
                NumberSlider("Mouse acceleration", settings.stream.mouseAcceleration.toFloat(), 1f, 150f, 1f) {
                    viewModel.updateStreamSettings { s -> s.copy(mouseAcceleration = it.roundToInt()) }
                }
                ChoiceOptionRow("Keyboard layout", keyboardLayoutOptions, settings.stream.keyboardLayout) {
                    viewModel.updateStreamSettings { s -> s.copy(keyboardLayout = it) }
                }
                ChoiceOptionRow("Game language", gameLanguageOptions, settings.stream.gameLanguage) {
                    viewModel.updateStreamSettings { s -> s.copy(gameLanguage = it) }
                }
                SettingSwitch("Clipboard paste", settings.clipboardPaste) { enabled -> viewModel.updateSettings(settings.copy(clipboardPaste = enabled)) }
                SettingSwitch("Phone rumble fallback", settings.phoneRumbleFallback) { enabled -> viewModel.updateSettings(settings.copy(phoneRumbleFallback = enabled)) }
                SettingSwitch("Touch controls", settings.androidTouch.enabled) { enabled -> viewModel.updateSettings(settings.copy(androidTouch = settings.androidTouch.copy(enabled = enabled))) }
                SettingSwitch("Finger mouse", settings.androidTouch.mousePad) { enabled -> viewModel.updateSettings(settings.copy(androidTouch = settings.androidTouch.copy(mousePad = enabled))) }
                NumberSlider("Touch layout scale", settings.androidTouch.scale, 0.6f, 1.4f, 0.05f) { value -> viewModel.updateSettings(settings.copy(androidTouch = settings.androidTouch.copy(scale = value))) }
                NumberSlider("Touch button size", settings.androidTouch.buttonScale, 0.65f, 1.5f, 0.05f) { value -> viewModel.updateSettings(settings.copy(androidTouch = settings.androidTouch.copy(buttonScale = value))) }
                NumberSlider("Touch stick size", settings.androidTouch.stickScale, 0.65f, 1.5f, 0.05f) { value -> viewModel.updateSettings(settings.copy(androidTouch = settings.androidTouch.copy(stickScale = value))) }
                NumberSlider("Touch opacity", settings.androidTouch.opacity, 0.15f, 1f, 0.05f) { value -> viewModel.updateSettings(settings.copy(androidTouch = settings.androidTouch.copy(opacity = value))) }
                NumberSlider("Touch edge padding", settings.androidTouch.edgePaddingDp, 0f, 72f, 1f) { value -> viewModel.updateSettings(settings.copy(androidTouch = settings.androidTouch.copy(edgePaddingDp = value))) }
                NumberSlider("Touch bottom padding", settings.androidTouch.bottomPaddingDp, 0f, 120f, 1f) { value -> viewModel.updateSettings(settings.copy(androidTouch = settings.androidTouch.copy(bottomPaddingDp = value))) }
                NumberSlider("Left controls horizontal offset", settings.androidTouch.leftOffsetXDp, -220f, 220f, 2f) { value -> viewModel.updateSettings(settings.copy(androidTouch = settings.androidTouch.copy(leftOffsetXDp = value))) }
                NumberSlider("Left controls vertical offset", settings.androidTouch.leftOffsetYDp, -160f, 160f, 2f) { value -> viewModel.updateSettings(settings.copy(androidTouch = settings.androidTouch.copy(leftOffsetYDp = value))) }
                NumberSlider("Right controls horizontal offset", settings.androidTouch.rightOffsetXDp, -220f, 220f, 2f) { value -> viewModel.updateSettings(settings.copy(androidTouch = settings.androidTouch.copy(rightOffsetXDp = value))) }
                NumberSlider("Right controls vertical offset", settings.androidTouch.rightOffsetYDp, -160f, 160f, 2f) { value -> viewModel.updateSettings(settings.copy(androidTouch = settings.androidTouch.copy(rightOffsetYDp = value))) }
            }
    SettingsSection(stringResource(R.string.settings_section_interface)) {
                val accentOptions = UiAccent.entries.map { it to uiAccentLabel(it) }
                SettingSwitch(stringResource(R.string.settings_dynamic_color), settings.dynamicColor) { viewModel.updateSettings(settings.copy(dynamicColor = it)) }
                ChoiceRow(stringResource(R.string.settings_accent), accentOptions.map { it.second }, accentOptions.firstOrNull { it.first == settings.uiAccent }?.second ?: accentOptions.first().second) { label ->
                    accentOptions.firstOrNull { it.second == label }?.first?.let { accent ->
                        viewModel.updateSettings(settings.copy(uiAccent = accent))
                    }
                }
                SettingSwitch(stringResource(R.string.settings_expressive_ui), settings.expressiveUi) { viewModel.updateSettings(settings.copy(expressiveUi = it)) }
                SettingSwitch(stringResource(R.string.settings_compact_cards), settings.compactGameCards) { viewModel.updateSettings(settings.copy(compactGameCards = it)) }
                SettingSwitch(stringResource(R.string.settings_show_store_labels), settings.showGameStoreLabels) { viewModel.updateSettings(settings.copy(showGameStoreLabels = it)) }
                NumberSlider(stringResource(R.string.settings_card_size), settings.posterSizeScale, 0.82f, 1.08f, 0.02f) { value -> viewModel.updateSettings(settings.copy(posterSizeScale = value)) }
                SettingSwitch(stringResource(R.string.settings_show_stats), settings.showStatsOnLaunch) { viewModel.updateSettings(settings.copy(showStatsOnLaunch = it)) }
                ChoiceRow("Stats overlay style", StreamStatsStyle.entries.map { it.label }, settings.streamStatsStyle.label) { label ->
                    StreamStatsStyle.entries.firstOrNull { it.label == label }?.let { style ->
                        viewModel.updateSettings(settings.copy(streamStatsStyle = style))
                    }
                }
                SettingSwitch(stringResource(R.string.settings_hide_server_selector), settings.hideServerSelector) { viewModel.updateSettings(settings.copy(hideServerSelector = it)) }
                SettingSwitch(stringResource(R.string.settings_controller_mode), settings.controllerMode) { viewModel.updateSettings(settings.copy(controllerMode = it)) }
                SettingSwitch(stringResource(R.string.settings_controller_sounds), settings.controllerUiSounds) { viewModel.updateSettings(settings.copy(controllerUiSounds = it)) }
                SettingSwitch(stringResource(R.string.settings_controller_animations), settings.controllerBackgroundAnimations) { viewModel.updateSettings(settings.copy(controllerBackgroundAnimations = it)) }
                SettingSwitch(stringResource(R.string.settings_controller_backdrop), settings.controllerLibraryGameBackdrop) { viewModel.updateSettings(settings.copy(controllerLibraryGameBackdrop = it)) }
                SettingSwitch(stringResource(R.string.settings_auto_load_library), settings.autoLoadControllerLibrary) { viewModel.updateSettings(settings.copy(autoLoadControllerLibrary = it)) }
                SettingSwitch(stringResource(R.string.settings_session_counter), settings.sessionCounterEnabled) { viewModel.updateSettings(settings.copy(sessionCounterEnabled = it)) }
            }
    SettingsSection("App Data") {
                AppDataSettingsPanel(viewModel = viewModel)
            }
    SettingsSection("Account") {
                AccountSettingsPanel(state = state, viewModel = viewModel)
            }
    SettingsSection("Codec Diagnostics") {
                CodecDiagnosticsPanel(state.codecReport)
            }
    SettingsSection("Debug Logs") {
                DebugLogsPanel(state = state, viewModel = viewModel)
            }
    SettingsSection("About") {
                AppVersionPanel()
            }
    }
}

@Composable
internal fun AppDataSettingsPanel(viewModel: OpenNowViewModel) {
    var clearCacheConfirmOpen by remember { mutableStateOf(false) }
    var resetSettingsConfirmOpen by remember { mutableStateOf(false) }
    if (clearCacheConfirmOpen) {
        AlertDialog(
            onDismissRequest = { clearCacheConfirmOpen = false },
            title = { Text("Clear game cache?") },
            text = { Text("Cached store, library, and search results will be removed. Your account and settings stay unchanged.") },
            confirmButton = {
                Button(
                    onClick = {
                        clearCacheConfirmOpen = false
                        viewModel.clearCatalogCache()
                    },
                ) {
                    Text("Clear cache")
                }
            },
            dismissButton = {
                TextButton(onClick = { clearCacheConfirmOpen = false }) {
                    Text(stringResource(R.string.action_cancel))
                }
            },
        )
    }
    if (resetSettingsConfirmOpen) {
        AlertDialog(
            onDismissRequest = { resetSettingsConfirmOpen = false },
            title = { Text("Reset settings?") },
            text = { Text("Stream, input, interface, and controller preferences will return to recommended defaults. Accounts stay signed in.") },
            confirmButton = {
                Button(
                    onClick = {
                        resetSettingsConfirmOpen = false
                        viewModel.resetSettings()
                    },
                ) {
                    Text("Reset settings")
                }
            },
            dismissButton = {
                TextButton(onClick = { resetSettingsConfirmOpen = false }) {
                    Text(stringResource(R.string.action_cancel))
                }
            },
        )
    }
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(
            "Recommended defaults keep touch controls, fullscreen recovery, dynamic color, compact cards, and controller polish on. Riskier debugging, proxy, stats, clipboard, and auto-load options stay off.",
            color = SettingsTextMuted,
            style = MaterialTheme.typography.bodySmall,
        )
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
            OutlinedButton(onClick = { clearCacheConfirmOpen = true }, modifier = Modifier.weight(1f)) {
                Text("Clear cache", maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
            OutlinedButton(onClick = { resetSettingsConfirmOpen = true }, modifier = Modifier.weight(1f)) {
                Text("Reset settings", maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
        }
    }
}

@Composable
internal fun AccountSettingsPanel(state: OpenNowUiState, viewModel: OpenNowViewModel) {
    val currentUserId = state.authSession?.user?.userId
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        state.savedAccounts.ifEmpty {
            state.authSession?.toSavedAccount()?.let { listOf(it) } ?: emptyList()
        }.forEach { account ->
            val selected = account.userId == currentUserId
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                color = if (selected) MaterialTheme.colorScheme.primary.copy(alpha = 0.16f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.76f),
            ) {
                Row(
                    Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    Column(Modifier.weight(1f)) {
                        Text(account.displayName.ifBlank { "NVIDIA Account" }, color = SettingsText, fontWeight = FontWeight.SemiBold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                        Text(
                            listOfNotNull(account.email?.takeIf { it.isNotBlank() }, account.providerCode, account.membershipTier).joinToString(" - "),
                            color = SettingsTextMuted,
                            style = MaterialTheme.typography.bodySmall,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                    if (selected) {
                        Text("Active", color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                    } else {
                        OutlinedButton(onClick = { viewModel.switchAccount(account.userId) }, contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)) {
                            Text("Switch")
                        }
                    }
                }
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
            Button(onClick = { viewModel.login() }, modifier = Modifier.weight(1f)) { Text("Add account") }
            OutlinedButton(onClick = viewModel::logout, modifier = Modifier.weight(1f)) { Text("Sign out") }
        }
        OutlinedButton(onClick = viewModel::logoutAll, modifier = Modifier.fillMaxWidth()) { Text("Sign out all accounts") }
    }
}

internal fun AuthSession.toSavedAccount(): SavedAccount =
    SavedAccount(
        userId = user.userId,
        displayName = user.displayName,
        email = user.email,
        avatarUrl = user.avatarUrl,
        membershipTier = user.membershipTier,
        providerCode = provider.code,
    )

@Composable
internal fun CodecDiagnosticsPanel(report: RuntimeCodecReport?) {
    if (report == null) {
        Text(stringResource(R.string.settings_codec_diagnostics_unavailable), color = SettingsTextMuted)
        return
    }
    val clipboard = LocalClipboardManager.current
    var copied by remember(report) { mutableStateOf(false) }
    val safeDecoders = report.capabilities.count { it.streamingRealtimeSafe() }
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Button(
            onClick = {
                clipboard.setText(AnnotatedString(formatCodecDiagnosticReport(report)))
                copied = true
            },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                if (copied) {
                    stringResource(R.string.settings_codec_diagnostics_copied)
                } else {
                    stringResource(R.string.settings_codec_diagnostics_copy)
                },
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
            CodecSummaryChip("${safeDecoders}/${report.capabilities.size}", "real-time decoders")
            CodecSummaryChip(if (report.lowPowerGpuProfile) "Low power" else "Standard", "device profile")
            CodecSummaryChip(if (report.androidTvProfile) "TV" else "Mobile", "shell")
        }
        report.capabilities.forEach { capability ->
            CodecCapabilityRow(capability)
        }
        Text(
            report.nativeRuntimeSummary.replace("{", "").replace("}", "").replace("\"", ""),
            color = SettingsTextMuted,
            style = MaterialTheme.typography.bodySmall,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

internal fun formatCodecDiagnosticReport(report: RuntimeCodecReport): String = buildString {
    appendLine("OpenNOW Android codec diagnostics")
    appendLine("nativeRuntimeSummary=${report.nativeRuntimeSummary}")
    appendLine("androidTvProfile=${report.androidTvProfile}")
    appendLine("lowPowerGpuProfile=${report.lowPowerGpuProfile}")
    report.capabilities.forEach { capability ->
        appendLine()
        appendLine("codec=${capability.codec}")
        appendLine("decoderAvailable=${capability.decoderAvailable}")
        appendLine("decoderName=${capability.decoderName ?: "none"}")
        appendLine("hardwareDecoder=${capability.hardwareDecoder}")
        appendLine("realtimeSafe=${capability.realtimeSafe}")
        appendLine("webRtcDecoderAvailable=${capability.webRtcDecoderAvailable ?: "unknown"}")
        appendLine("webRtcDecoderName=${capability.webRtcDecoderName ?: "none"}")
        appendLine("webRtcHardwareDecoderAvailable=${capability.webRtcHardwareDecoderAvailable ?: "unknown"}")
        appendLine("webRtcProfiles=${capability.webRtcCodecProfiles.joinToString(", ").ifBlank { "none" }}")
        appendLine("encoderAvailable=${capability.encoderAvailable}")
        appendLine("encoderName=${capability.encoderName ?: "none"}")
        appendLine("hardwareEncoder=${capability.hardwareEncoder}")
    }
}

@Composable
internal fun RowScope.CodecSummaryChip(value: String, label: String) {
    Surface(
        modifier = Modifier.weight(1f),
        shape = RoundedCornerShape(14.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.76f),
    ) {
        Column(Modifier.padding(horizontal = 10.dp, vertical = 8.dp)) {
            Text(value, color = SettingsText, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text(label, color = SettingsTextMuted, style = MaterialTheme.typography.labelSmall, maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
    }
}

@Composable
internal fun CodecCapabilityRow(capability: CodecCapability) {
    val streamingReady = capability.streamingDecoderAvailable()
    val healthy = capability.streamingRealtimeSafe()
    val status = when {
        healthy -> "Ready"
        streamingReady -> "WebRTC ready"
        capability.decoderAvailable -> "Platform only"
        else -> "Unavailable"
    }
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.76f),
    ) {
        Column(Modifier.padding(horizontal = 12.dp, vertical = 10.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(capability.codec.name, color = SettingsText, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                Text(
                    status,
                    color = if (healthy) MaterialTheme.colorScheme.primary else Color(0xffffc266),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                )
            }
            Text(
                "WebRTC: ${capability.streamingDecoderName() ?: "none"}",
                color = SettingsTextMuted,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                "Hardware decode ${yesNo(capability.streamingHardwareDecoderAvailable())} - platform ${capability.decoderName ?: "none"}",
                color = SettingsTextMuted,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

internal fun yesNo(value: Boolean): String = if (value) "yes" else "no"

internal val StreamStatsStyle.label: String
    get() = when (this) {
        StreamStatsStyle.Compact -> "Compact line"
        StreamStatsStyle.Detailed -> "Detailed card"
    }

internal fun StreamStatsStyle.next(): StreamStatsStyle =
    when (this) {
        StreamStatsStyle.Compact -> StreamStatsStyle.Detailed
        StreamStatsStyle.Detailed -> StreamStatsStyle.Compact
    }

@Composable
internal fun AppVersionPanel() {
    Row(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(SettingsPanelAlt)
            .padding(horizontal = 14.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text("OpenNOW Android", color = SettingsText, fontWeight = FontWeight.SemiBold)
            Text("Version ${BuildConfig.VERSION_NAME}", color = SettingsTextMuted, style = MaterialTheme.typography.bodySmall)
        }
        Text("Build ${BuildConfig.VERSION_CODE}", color = SettingsTextMuted, style = MaterialTheme.typography.labelMedium)
    }
}

@Composable
internal fun DebugLogsPanel(state: OpenNowUiState, viewModel: OpenNowViewModel) {
    val context = LocalContext.current
    val clipboard = LocalClipboardManager.current
    var copied by remember { mutableStateOf(false) }
    var saved by remember { mutableStateOf(false) }
    var saveError by remember { mutableStateOf<String?>(null) }
    var pendingLogText by remember { mutableStateOf("") }
    val saveLauncher = rememberLauncherForActivityResult(ActivityResultContracts.CreateDocument("text/plain")) { uri ->
        if (uri == null) return@rememberLauncherForActivityResult
        runCatching {
            context.contentResolver.openOutputStream(uri)?.use { output ->
                output.write(pendingLogText.toByteArray(Charsets.UTF_8))
            } ?: error("Could not open log file")
        }.onSuccess {
            saved = true
            saveError = null
        }.onFailure { error ->
            saveError = error.message ?: "Could not save logs"
        }
    }
    Text("Includes launch state, queue state, ads, stream settings, input settings, and codec capabilities.", color = SettingsTextMuted)
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
        Button(
            onClick = {
                clipboard.setText(AnnotatedString(viewModel.debugLogText()))
                copied = true
            },
            modifier = Modifier.weight(1f),
        ) {
            Text(if (copied) "Copied logs" else "Copy logs", maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
        OutlinedButton(
            onClick = {
                pendingLogText = viewModel.debugLogText()
                saved = false
                saveError = null
                saveLauncher.launch("opennow-android-logs.txt")
            },
            modifier = Modifier.weight(1f),
        ) {
            Text(if (saved) "Saved .txt" else "Save .txt", maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
    }
    state.error?.let { error ->
        OutlinedButton(onClick = {
            clipboard.setText(AnnotatedString(error))
            copied = true
        }) {
            Text("Copy error")
        }
    }
    saveError?.let {
        Text(it, color = Color(0xffff9f9f), style = MaterialTheme.typography.bodySmall)
    }
}

@Composable
internal fun SessionProxyWarningDialog(onCancel: () -> Unit, onEnable: () -> Unit) {
    AlertDialog(
        onDismissRequest = onCancel,
        title = { Text(stringResource(R.string.settings_session_proxy_warning_title), fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(stringResource(R.string.settings_session_proxy_warning_traffic), style = MaterialTheme.typography.bodySmall)
                Text(stringResource(R.string.settings_session_proxy_warning_breakage), style = MaterialTheme.typography.bodySmall)
                Text(stringResource(R.string.settings_session_proxy_warning_trust), style = MaterialTheme.typography.bodySmall)
            }
        },
        confirmButton = {
            Button(onClick = onEnable) {
                Text(stringResource(R.string.settings_session_proxy_warning_enable))
            }
        },
        dismissButton = {
            TextButton(onClick = onCancel) {
                Text(stringResource(R.string.action_cancel))
            }
        },
        containerColor = SettingsPanel,
        titleContentColor = SettingsText,
        textContentColor = SettingsTextMuted,
    )
}

@Composable
internal fun SettingsSection(title: String, content: @Composable ColumnScope.() -> Unit) {
    val sectionShape = RoundedCornerShape(14.dp)
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = sectionShape,
    ) {
        Column(Modifier.fillMaxWidth().padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(title, color = SettingsText, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            content()
        }
    }
}

@Composable
internal fun SettingSwitch(label: String, checked: Boolean, enabled: Boolean = true, onCheckedChange: (Boolean) -> Unit) {
    val focusManager = LocalFocusManager.current
    var focused by remember { mutableStateOf(false) }
    val shape = RoundedCornerShape(14.dp)
    val toggle = {
        if (enabled) {
            onCheckedChange(!checked)
        }
    }
    Row(
        Modifier
            .fillMaxWidth()
            .onFocusChanged { focused = it.isFocused || it.hasFocus }
            .border(
                width = if (focused) 2.dp else 1.dp,
                color = if (focused) MaterialTheme.colorScheme.primary else Color.Transparent,
                shape = shape,
            )
            .clip(shape)
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.76f))
            .clickable(enabled = enabled, onClick = toggle)
            .onPreviewKeyEvent { event ->
                when {
                    enabled && isTvActivateKey(event) -> {
                        toggle()
                        true
                    }
                    else -> handleVerticalDpadFocusMove(event, focusManager)
                }
            }
            .focusable()
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        val contentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = if (enabled) 1f else 0.45f)
        Text(label, Modifier.weight(1f), color = contentColor, maxLines = 2, overflow = TextOverflow.Ellipsis)
        Switch(checked = checked, enabled = enabled, onCheckedChange = onCheckedChange)
    }
}

@Composable
internal fun NumberSlider(label: String, value: Float, min: Float, max: Float, step: Float, onChange: (Float) -> Unit) {
    var local by remember(value) { mutableFloatStateOf(value) }
    val focusManager = LocalFocusManager.current
    Column(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.76f))
            .padding(horizontal = 12.dp, vertical = 10.dp),
    ) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text(label, Modifier.weight(1f), color = MaterialTheme.colorScheme.onSurface, maxLines = 2, overflow = TextOverflow.Ellipsis)
            Text(if (step < 1f) "%.2f".format(local) else local.roundToInt().toString(), color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Slider(
            modifier = Modifier.onPreviewKeyEvent { handleVerticalDpadFocusMove(it, focusManager) },
            value = local,
            onValueChange = { local = ((it / step).roundToInt() * step).coerceIn(min, max) },
            onValueChangeFinished = { onChange(local) },
            valueRange = min..max,
        )
    }
}

@Composable
internal fun ChoiceRow(label: String, options: List<String>, selected: String, onSelect: (String) -> Unit) {
    ChoiceMenuRow(
        label = label,
        options = options.map { ChoiceMenuOption(value = it, label = it) },
        selectedLabel = selected,
        onSelect = onSelect,
    )
}

@Composable
internal fun ChoiceMenuRow(
    label: String,
    options: List<ChoiceMenuOption>,
    selectedLabel: String,
    onSelect: (String) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    var focused by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    val autoLabel = stringResource(R.string.option_auto)
    val shape = RoundedCornerShape(14.dp)
    Row(
        Modifier
            .fillMaxWidth()
            .onFocusChanged { focused = it.isFocused || it.hasFocus }
            .border(
                width = if (focused) 2.dp else 1.dp,
                color = if (focused) MaterialTheme.colorScheme.primary else Color.Transparent,
                shape = shape,
            )
            .clip(shape)
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.76f))
            .clickable { expanded = true }
            .onPreviewKeyEvent { event ->
                when {
                    isTvActivateKey(event) -> {
                        expanded = true
                        true
                    }
                    else -> handleVerticalDpadFocusMove(event, focusManager)
                }
            }
            .focusable()
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(label, Modifier.weight(1f), color = MaterialTheme.colorScheme.onSurface, maxLines = 2, overflow = TextOverflow.Ellipsis)
        Box {
            OutlinedButton(onClick = { expanded = true }) { Text(selectedLabel.ifBlank { autoLabel }, maxLines = 1, overflow = TextOverflow.Ellipsis) }
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                            ) {
                                Text(
                                    option.label,
                                    color = if (option.enabled) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.48f),
                                )
                                option.badge?.let { badge ->
                                    Text(
                                        badge,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier
                                            .border(1.dp, MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f), RoundedCornerShape(3.dp))
                                            .padding(horizontal = 4.dp, vertical = 1.dp),
                                    )
                                }
                            }
                        },
                        enabled = option.enabled,
                        onClick = {
                            expanded = false
                            onSelect(option.value)
                        },
                    )
                }
            }
        }
    }
}

@Composable
internal fun ChoiceOptionRow(label: String, options: List<SettingsChoiceOption>, selectedValue: String, onSelect: (String) -> Unit) {
    val selectedLabel = options.firstOrNull { it.value == selectedValue }?.label ?: selectedValue
    ChoiceRow(label, options.map { it.label }, selectedLabel) { selected ->
        options.firstOrNull { it.label == selected }?.value?.let(onSelect)
    }
}

private fun handleVerticalDpadFocusMove(event: androidx.compose.ui.input.key.KeyEvent, focusManager: FocusManager): Boolean {
    if (event.type != KeyEventType.KeyDown) return false
    val direction = when (event.key) {
        androidx.compose.ui.input.key.Key.DirectionUp -> FocusDirection.Up
        androidx.compose.ui.input.key.Key.DirectionDown -> FocusDirection.Down
        else -> return false
    }
    focusManager.moveFocus(direction)
    return true
}
