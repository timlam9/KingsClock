package com.lamti.kingsclock.ui.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.lamti.kingsclock.ui.composables.basic.OutlinedButton
import com.lamti.kingsclock.ui.composables.basic.RoundedIconButton

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
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        AnimatedVisibility(
            visible = showRestartButton,
            enter = slideInHorizontally {
                with(density) { -300.dp.roundToPx() }
            },
            exit = slideOutHorizontally {
                with(density) { -300.dp.roundToPx() }
            }
        ) {
            OutlinedButton(
                text = "Restart",
                onclick = onRestartButtonClicked
            )
        }
        AnimatedVisibility(
            visible = showCloseButton,
            enter = slideInHorizontally {
                with(density) { 300.dp.roundToPx() }
            },
            exit = slideOutHorizontally {
                with(density) { 300.dp.roundToPx() }
            }
        ) {
            RoundedIconButton(onClick = onCloseButtonClicked)
        }
    }
}
