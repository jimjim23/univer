package com.universitinder.app.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun CircularAvatar(
    name: String,
    modifier: Modifier = Modifier,
    size: Dp = 40.dp,
    textStyle: TextStyle = MaterialTheme.typography.bodyMedium,
) {
    val color = MaterialTheme.colorScheme.primaryContainer
    val textColor = MaterialTheme.colorScheme.onPrimaryContainer
    Box(modifier.size(size), contentAlignment = Alignment.Center) {
        val initials = name.split(" ").joinToString("") { it.take(1).uppercase() }
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(SolidColor(color))
        }
        Text(text = initials, style = textStyle, color = textColor)
    }
}