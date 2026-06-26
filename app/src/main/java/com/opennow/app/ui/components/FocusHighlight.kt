package com.opennow.app.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.focusable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp

@Composable
fun Modifier.focusHighlight(): Modifier {
    var focused by remember { mutableStateOf(false) }
    return this
        .clip(RoundedCornerShape(4.dp))
        .graphicsLayer {
            scaleX = if (focused) 1.03f else 1f
            scaleY = if (focused) 1.03f else 1f
        }
        .border(
            width = if (focused) 2.dp else 0.dp,
            color = MaterialTheme.colorScheme.primary,
            shape = RoundedCornerShape(4.dp),
        )
        .onFocusChanged { focused = it.isFocused }
        .focusable()
}
