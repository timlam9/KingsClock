package com.lamti.kingsclock.ui.composables.combound

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.lamti.kingsclock.R
import com.lamti.kingsclock.ui.composables.basic.OutlineButton
import com.lamti.kingsclock.ui.composables.basic.OutlineIcon

@Composable
fun FinishButtons(
    showRestartButton: Boolean,
    showCloseButton: Boolean,
    onRestartButtonClicked: () -> Unit,
    onCloseButtonClicked: () -> Unit,
) {
    val density = LocalDensity.current

    Row(
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        AnimatedVisibility(
            visible = showRestartButton,
            enter = slideInHorizontally(
                animationSpec = tween(
                    durationMillis = 120,
                    easing = FastOutSlowInEasing
                ),
                initialOffsetX = {
                    with(density) { -300.dp.roundToPx() }
                }
            ),
            exit = slideOutHorizontally(
                animationSpec = tween(
                    durationMillis = 120,
                    delayMillis = 50,
                    easing = FastOutSlowInEasing
                ),
                targetOffsetX = {
                    with(density) { -300.dp.roundToPx() }
                }
            )
        ) {
            OutlineButton(
                text = "Restart",
                enabled = true,
                onclick = onRestartButtonClicked
            )
        }
        Spacer(modifier = Modifier.size(24.dp))
        AnimatedVisibility(
            visible = showCloseButton,
            enter = slideInHorizontally(
                animationSpec = tween(
                    durationMillis = 120,
                    delayMillis = 50,
                    easing = FastOutSlowInEasing
                ),
                initialOffsetX = { with(density) { 300.dp.roundToPx() } }
            ),
            exit = slideOutHorizontally(
                animationSpec = tween(
                    durationMillis = 120,
                    easing = FastOutSlowInEasing
                ),
                targetOffsetX = {
                    with(density) { 300.dp.roundToPx() }
                }
            )
        ) {
            OutlineIcon(onClick = onCloseButtonClicked, imageID = R.drawable.ic_close)
        }
    }
}
