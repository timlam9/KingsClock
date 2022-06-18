package com.lamti.kingsclock.ui.composables.basic

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.lamti.kingsclock.ui.uistate.Turn

@Composable
fun AnimatedCircle(
    modifier: Modifier = Modifier,
    turn: Turn
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp

    val baseOffset = (screenHeight / 2) + 60.dp
    val offset by animateDpAsState(if (turn == Turn.Whites) baseOffset else -baseOffset)
    val color by animateColorAsState(if (turn == Turn.Whites) MaterialTheme.colors.onPrimary else MaterialTheme.colors.primary)

    Canvas(
        modifier = modifier
            .size(screenWidth)
            .offset(y = offset)
    ) {
        val canvasWidth = size.width
        val canvasHeight = size.height

        drawCircle(
            color = color,
            center = Offset(x = canvasWidth / 2, y = canvasHeight / 2),
            radius = screenWidth.toPx() / 2
        )
    }
}
