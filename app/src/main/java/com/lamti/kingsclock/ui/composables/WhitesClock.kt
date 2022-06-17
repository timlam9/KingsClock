package com.lamti.kingsclock.ui.composables

import android.graphics.Paint
import android.graphics.Typeface
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.lamti.kingsclock.ui.theme.LightGray
import com.lamti.kingsclock.ui.theme.TextColor

@Composable
fun WhitesClock(
    modifier: Modifier = Modifier,
    clockSize: Dp,
    enabled: Boolean,
    textColor: Int,
    circleColor: Color = LightGray,
    indicatorColor: Color = MaterialTheme.colors.onPrimary,
    offsetY: Dp = clockSize / 2 + 40.dp,
    strokeWidth: Float = 38f,
    font: Typeface? = null,
    currentTimeMillis: Long,
    maxTimeMillis: Long,
    formattedTime: String
) {
    val enabledColor = if (enabled) indicatorColor else TextColor
    val percentage by animateFloatAsState(currentTimeMillis.toFloat() / maxTimeMillis.toFloat())

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(clockSize)
            .offset(y = offsetY),
    ) {
        val canvasWidth = size.width
        val canvasHeight = size.height

        drawCircle(
            SolidColor(circleColor),
            clockSize.toPx() / 2,
            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
        )

        drawArc(
            color = enabledColor,
            size = Size(width = clockSize.toPx(), height = clockSize.toPx()),
            startAngle = 180f,
            sweepAngle = 180f * percentage,
            useCenter = false,
            style = Stroke(width = strokeWidth + 30, cap = StrokeCap.Round)
        )

        drawContext.canvas.nativeCanvas.apply {
            drawText(
                formattedTime,
                canvasWidth / 2,
                canvasHeight / 3.5f,
                Paint().apply {
                    if (font != null) typeface = font
                    textAlign = Paint.Align.CENTER
                    textSize = 150f
                    letterSpacing = 0.1f
                    isFakeBoldText = true
                    color = textColor
                }
            )
        }
    }
}
