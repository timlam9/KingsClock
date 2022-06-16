package com.lamti.kingsclock.ui.composables

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
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
    color: Color = Color.Blue,
    textColor: Int = R.color.white,
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
                size.height / 2,
                Paint().apply {
                    textSize = 100f
                    textAlign = Paint.Align.CENTER
                    setColor(ContextCompat.getColor(context, textColor))
                }
            )
        }
    }
}
