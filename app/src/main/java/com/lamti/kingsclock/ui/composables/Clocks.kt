package com.lamti.kingsclock.ui.composables

import android.content.Context
import android.graphics.Typeface
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.core.content.ContextCompat
import com.lamti.kingsclock.R
import com.lamti.kingsclock.ui.uistate.ClockState
import com.lamti.kingsclock.ui.uistate.Timer
import com.lamti.kingsclock.ui.uistate.Turn

@Composable
fun Clocks(
    clockState: ClockState,
    onBackgroundClicked: () -> Unit,
    blacksClockTranslationY: Animatable<Float, AnimationVector1D>,
    screenWidth: Dp,
    turn: Turn,
    context: Context,
    font: Typeface?,
    whitesClockTranslationY: Animatable<Float, AnimationVector1D>,
    blacksTimer: Timer,
    whitesTimer: Timer,
    maxTimeMillis: Long
) {
    val blacksTextColor = if (turn == Turn.Blacks) R.color.white else R.color.text_color
    val whitesTextColor = if (turn == Turn.Whites) R.color.white else R.color.text_color

    Column(
        modifier = Modifier
            .fillMaxSize()
            .clickable(
                enabled = clockState == ClockState.Started,
                onClick = onBackgroundClicked
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        BlacksClock(
            modifier = Modifier.offset { IntOffset(0, blacksClockTranslationY.value.toInt()) },
            clockSize = screenWidth,
            enabled = turn == Turn.Blacks,
            textColor = ContextCompat.getColor(context, blacksTextColor),
            font = font,
            currentTimeMillis = blacksTimer.timeMillis,
            maxTimeMillis = maxTimeMillis,
            formattedTime = blacksTimer.formattedTime
        )
        WhitesClock(
            modifier = Modifier.offset { IntOffset(0, whitesClockTranslationY.value.toInt()) },
            clockSize = screenWidth,
            enabled = turn == Turn.Whites,
            textColor = ContextCompat.getColor(context, whitesTextColor),
            font = font,
            currentTimeMillis = whitesTimer.timeMillis,
            maxTimeMillis = maxTimeMillis,
            formattedTime = whitesTimer.formattedTime
        )
    }
}

