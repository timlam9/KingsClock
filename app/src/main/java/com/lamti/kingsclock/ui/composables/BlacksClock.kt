package com.lamti.kingsclock.ui.composables

import android.graphics.Paint
import android.graphics.Typeface
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.lamti.kingsclock.ui.theme.Red

@Composable
fun BlacksClock(
    modifier: Modifier = Modifier,
    clockSize: Dp,
    enabled: Boolean,
    textColor: Int,
    indicatorColor: Color = Red,
    strokeWidth: Float = 30f,
    font: Typeface? = null,
    offsetY: Dp = -(clockSize / 2 + 40.dp)
) {
    val enabledColor = if (enabled) indicatorColor else Color.DarkGray

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(clockSize)
            .offset(y = offsetY),
    ) {
        val canvasWidth = size.width
        val canvasHeight = size.height

        drawCircle(
            SolidColor(Color.Gray),
            clockSize.toPx() / 2,
            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
        )

        drawArc(
            color = enabledColor,
            size = Size(width = clockSize.toPx(), height = clockSize.toPx()),
            startAngle = 180f,
            sweepAngle = -80f,
            useCenter = false,
            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
        )

        rotate(180f) {
            drawContext.canvas.nativeCanvas.apply {
                drawText(
                    "10:00.00",
                    canvasWidth / 2,
                    canvasHeight / 3.5f,
                    Paint().apply {
                        textSize = 150f
                        if (font != null) typeface = font
                        textAlign = Paint.Align.CENTER
                        isFakeBoldText = true
                        color = textColor
                    }
                )
            }
        }
    }
}
