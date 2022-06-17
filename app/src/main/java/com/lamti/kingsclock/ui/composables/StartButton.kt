package com.lamti.kingsclock.ui.composables

import android.graphics.Paint
import android.graphics.Typeface
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.lamti.kingsclock.R

@Composable
fun StartButton(
    modifier: Modifier = Modifier,
    buttonSize: Dp = 250.dp,
    color: Color = MaterialTheme.colors.primary,
    textColor: Int = R.color.white,
    font: Typeface? = null,
    onClick: () -> Unit
) {
    val context = LocalContext.current

    Canvas(
        modifier = modifier
            .size(buttonSize)
            .clickable { onClick() }
    ) {
        val canvasWidth = size.width
        val canvasHeight = size.height

        drawCircle(
            color = color,
            center = Offset(x = canvasWidth / 2, y = canvasHeight / 2),
            radius = buttonSize.toPx() / 2
        )

        drawContext.canvas.nativeCanvas.apply {
            drawText(
                "Start",
                size.width / 2,
                size.height / 2 + 44,
                Paint().apply {
                    if (font != null) typeface = font
                    textSize = 120f
                    letterSpacing = 0.5f
                    textAlign = Paint.Align.CENTER
                    isFakeBoldText = true
                    setColor(ContextCompat.getColor(context, textColor))
                }
            )
        }
    }
}
