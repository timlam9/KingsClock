package com.lamti.kingsclock.ui.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.offset
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.lamti.kingsclock.R
import com.lamti.kingsclock.ui.composables.basic.RoundedIcon
import com.lamti.kingsclock.ui.uistate.Turn

@Composable
fun PauseButtons(
    showBlacksClock: Boolean,
    enabledClockState: Turn,
    screenWidth: Dp,
    onPauseButtonClicked: () -> Unit,
    showWhitesClock: Boolean
) {
    val density = LocalDensity.current

    AnimatedVisibility(
        visible = showBlacksClock && enabledClockState == Turn.Blacks,
        enter = slideInHorizontally {
            with(density) { -300.dp.roundToPx() }
        },
        exit = slideOutHorizontally {
            with(density) { -300.dp.roundToPx() }
        }
    ) {
        RoundedIcon(
            modifier = Modifier
                .rotate(180f)
                .offset(x = (screenWidth / 2 - 40.dp), y = screenWidth / 2 + 40.dp),
            icon = R.drawable.ic_pause,
            color = MaterialTheme.colors.onSurface,
            tint = MaterialTheme.colors.primary,
            onClick = onPauseButtonClicked
        )
    }
    AnimatedVisibility(
        visible = showWhitesClock && enabledClockState == Turn.Whites,
        enter = slideInHorizontally {
            with(density) { 300.dp.roundToPx() }
        },
        exit = slideOutHorizontally {
            with(density) { 300.dp.roundToPx() }
        }
    ) {
        RoundedIcon(
            modifier = Modifier
                .offset(x = (screenWidth / 2 - 40.dp), y = screenWidth / 2 + 40.dp),
            icon = R.drawable.ic_pause,
            color = MaterialTheme.colors.onSurface,
            tint = MaterialTheme.colors.onPrimary,
            onClick = onPauseButtonClicked
        )
    }
}
