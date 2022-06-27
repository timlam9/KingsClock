package com.lamti.kingsclock.ui.composables.basic

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun OutlinedButton(
    modifier: Modifier = Modifier,
    text: String,
    width: Dp = 150.dp,
    height: Dp = 70.dp,
    fontSize: TextUnit = 30.sp,
    borderColor: Color = MaterialTheme.colors.primary,
    backgroundColor: Color = MaterialTheme.colors.background,
    textColor: Color = borderColor,
    shape: Shape = MaterialTheme.shapes.medium.copy(CornerSize(60)),
    stroke: Dp = 4.dp,
    enabled: Boolean = false,
    onclick: () -> Unit
) {
    androidx.compose.material.OutlinedButton(
        onClick = onclick,
        modifier = modifier.size(width, height),
        border = BorderStroke(stroke, borderColor),
        shape = shape,
        enabled = enabled,
        colors = ButtonDefaults.outlinedButtonColors(backgroundColor = backgroundColor)
    ) {
        Text(
            modifier = Modifier,
            text = text,
            style = MaterialTheme.typography.button.copy(
                color = textColor,
                fontWeight = FontWeight.ExtraBold,
                fontSize = fontSize,
            )
        )
    }
}
