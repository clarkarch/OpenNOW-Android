package com.opennow.app.ui.stream

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun OnScreenGamepad(
    onButtonPress: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.Black.copy(alpha = 0.4f))
            .padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        DPad(onButtonPress = onButtonPress)

        AnalogStick(label = "L3")

        GamepadButtons(onButtonPress = onButtonPress)

        AnalogStick(label = "R3")
    }
}

@Composable
private fun DPad(onButtonPress: (String) -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        DPadButton("\u25B2", "UP", onButtonPress)
        Row {
            DPadButton("\u25C0", "LEFT", onButtonPress)
            Box(modifier = Modifier.size(40.dp))
            DPadButton("\u25B6", "RIGHT", onButtonPress)
        }
        DPadButton("\u25BC", "DOWN", onButtonPress)
    }
}

@Composable
private fun DPadButton(
    text: String,
    label: String,
    onPress: (String) -> Unit,
) {
    FilledIconButton(
        onClick = { onPress(label) },
        modifier = Modifier.size(40.dp),
        colors = IconButtonDefaults.filledIconButtonColors(
            containerColor = Color.White.copy(alpha = 0.2f),
        ),
    ) {
        Text(text, color = Color.White, fontSize = 16.sp)
    }
}

@Composable
private fun GamepadButtons(onButtonPress: (String) -> Unit) {
    val buttonSize = 44.dp
    val labels = listOf("X", "A", "B", "Y")
    val colors = listOf(
        Color(0xFF4CAF50),
        Color(0xFF2196F3),
        Color(0xFFF44336),
        Color(0xFFFFEB3B),
    )

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        GamepadButton("Y", colors[3], buttonSize, onButtonPress)
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            GamepadButton("X", colors[0], buttonSize, onButtonPress)
            Box(modifier = Modifier.size(buttonSize))
            GamepadButton("B", colors[2], buttonSize, onButtonPress)
        }
        GamepadButton("A", colors[1], buttonSize, onButtonPress)
    }
}

@Composable
private fun GamepadButton(
    label: String,
    color: Color,
    size: androidx.compose.ui.unit.Dp,
    onPress: (String) -> Unit,
) {
    FilledIconButton(
        onClick = { onPress(label) },
        modifier = Modifier.size(size),
        colors = IconButtonDefaults.filledIconButtonColors(
            containerColor = color.copy(alpha = 0.6f),
        ),
    ) {
        Text(label, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun AnalogStick(label: String) {
    Box(
        modifier = Modifier
            .size(48.dp)
            .background(Color.White.copy(alpha = 0.15f), CircleShape),
        contentAlignment = Alignment.Center,
    ) {
        Text(label, color = Color.White.copy(alpha = 0.5f), fontSize = 10.sp)
    }
}
