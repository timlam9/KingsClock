package com.lamti.kingsclock.ui.composables

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.lamti.kingsclock.ui.theme.DarkRed
import com.lamti.kingsclock.ui.theme.LightGray
import com.lamti.kingsclock.ui.theme.TextColor

@Composable
fun WhitesClock(
    modifier: Modifier = Modifier,
    enabled: Boolean,
    circleColor: Color = LightGray,
    indicatorColor: Color = MaterialTheme.colors.onPrimary,
    strokeWidth: Float = 38f,
    currentTimeMillis: Long,
    maxTimeMillis: Long,
    formattedTime: String
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    val enabledColor = if (enabled) indicatorColor else TextColor
    val percentage by animateFloatAsState(currentTimeMillis.toFloat() / maxTimeMillis.toFloat())

    val bgCircleColor by animateColorAsState(
        if (enabled) indicatorColor else MaterialTheme.colors.background,
        animationSpec = tween(
            durationMillis = 350,
            delayMillis = 0,
            easing = FastOutSlowInEasing
        )
    )
    val textColor by animateColorAsState(
        if (enabled) MaterialTheme.colors.background else TextColor,
        animationSpec = tween(
            durationMillis = 350,
            delayMillis = 0,
            easing = FastOutSlowInEasing
        )
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(screenWidth)
            .offset(y = screenWidth / 2)
            .drawBehind {
                drawCircle(
                    SolidColor(bgCircleColor),
                    screenWidth.toPx() / 2,
                    style = Fill,
                )
                drawCircle(
                    SolidColor(circleColor),
                    screenWidth.toPx() / 2,
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                )
                drawArc(
                    color = enabledColor,
                    size = Size(width = screenWidth.toPx(), height = screenWidth.toPx()),
                    startAngle = 180f,
                    sweepAngle = 180f * percentage,
                    useCenter = false,
                    style = Stroke(width = strokeWidth + 30, cap = StrokeCap.Round)
                )
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = formattedTime,
            modifier = Modifier.offset(y = -screenWidth / 5),
            style = MaterialTheme.typography.h3.copy(
                color = if (formattedTime == "time's up".uppercase()) DarkRed else textColor,
                fontWeight = FontWeight.SemiBold,
                fontFamily = FontFamily.Monospace,
                textAlign = TextAlign.Center,
            )
        )
    }
}
