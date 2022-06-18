package com.lamti.kingsclock.ui.composables.combound

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.lamti.kingsclock.ui.noRippleClickable
import com.lamti.kingsclock.ui.uistate.ClockState
import com.lamti.kingsclock.ui.uistate.Timer
import com.lamti.kingsclock.ui.uistate.Turn

@Composable
fun Clocks(
    clockState: ClockState,
    onBackgroundClicked: () -> Unit,
    blacksClockTranslationY: Dp,
    screenWidth: Dp,
    turn: Turn,
    whitesClockTranslationY: Dp,
    blacksTimer: Timer,
    whitesTimer: Timer,
    maxTimeMillis: Long
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .noRippleClickable(
                enabled = clockState == ClockState.Started,
                onClick = onBackgroundClicked
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        BlacksClock(
            modifier = Modifier.offset(y = blacksClockTranslationY),
            clockSize = screenWidth,
            enabled = turn == Turn.Blacks,
            currentTimeMillis = blacksTimer.timeMillis,
            maxTimeMillis = maxTimeMillis,
            formattedTime = blacksTimer.formattedTime
        )
        WhitesClock(
            modifier = Modifier.offset(y = whitesClockTranslationY),
            enabled = turn == Turn.Whites,
            currentTimeMillis = whitesTimer.timeMillis,
            maxTimeMillis = maxTimeMillis,
            formattedTime = whitesTimer.formattedTime
        )
    }
}

