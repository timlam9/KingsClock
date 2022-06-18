package com.lamti.kingsclock.ui.composables.basic

import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle

@Composable
fun BasicButton(
    modifier: Modifier = Modifier,
    text: String,
    style: TextStyle = MaterialTheme.typography.button,
    shape: Shape = MaterialTheme.shapes.small,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier,
        onClick = onClick,
        shape = shape
    ) {
        Text(
            text = text,
            style = style
        )
    }
}
