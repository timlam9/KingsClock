package com.lamti.kingsclock.ui.screens

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.lamti.kingsclock.ui.composables.AnimatedCircle
import com.lamti.kingsclock.ui.composables.Clocks
import com.lamti.kingsclock.ui.composables.PauseButtons
import com.lamti.kingsclock.ui.composables.RoundedTextButton
import com.lamti.kingsclock.ui.theme.KingsClockTheme
import com.lamti.kingsclock.ui.uistate.ClockState
import com.lamti.kingsclock.ui.uistate.Timer
import com.lamti.kingsclock.ui.uistate.Turn

@Composable
fun ClockScreen(whitesTime: Int, blacksTime: Int) {
    val density = LocalDensity.current
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp

    val blacksStartedTranslationValue = -screenHeight
    val whitesStartedTranslationValue = screenHeight

    var clockState by rememberSaveable { mutableStateOf(ClockState.Finished) }
    var turn by rememberSaveable { mutableStateOf(Turn.Whites) }

    var showStartButton by rememberSaveable { mutableStateOf(false) }
    var showBlacksClock by rememberSaveable { mutableStateOf(false) }
    var showWhitesClock by rememberSaveable { mutableStateOf(false) }

    val scaleStartButton by animateFloatAsState(if (showStartButton) 1f else 0f)
    val whitesClockTranslationY by animateDpAsState(
        when (clockState) {
            ClockState.Started -> 0.dp
            ClockState.Paused -> whitesStartedTranslationValue
            ClockState.Finished -> whitesStartedTranslationValue
        }
    )
    val blacksClockTranslationY by animateDpAsState(
        when (clockState) {
            ClockState.Started -> 0.dp
            ClockState.Paused -> blacksStartedTranslationValue
            ClockState.Finished -> blacksStartedTranslationValue
        }
    )

    LaunchedEffect(clockState) {
        when (clockState) {
            ClockState.Started -> {
                showStartButton = false
                showBlacksClock = true
                showWhitesClock = true
            }
            ClockState.Paused -> {
                showStartButton = true
                showBlacksClock = false
                showWhitesClock = false
            }
            ClockState.Finished -> {
                showStartButton = true
                showBlacksClock = false
                showWhitesClock = false
            }
        }
    }

    ClockScreen(
        clockState = clockState,
        turn = turn,
        screenWidth = screenWidth,
        blacksClockTranslationY = blacksClockTranslationY,
        whitesClockTranslationY = whitesClockTranslationY,
        scaleStartButton = scaleStartButton,
        showBlacksClock = showBlacksClock,
        density = density,
        showWhitesClock = showWhitesClock,
        onBackgroundClicked = { turn = turn.changeClock() },
        onStartButtonClicked = { clockState = ClockState.Started },
        onPauseButtonClicked = { clockState = ClockState.Paused }
    )
}

@Composable
private fun ClockScreen(
    clockState: ClockState,
    turn: Turn,
    blacksClockTranslationY: Dp,
    whitesClockTranslationY: Dp,
    screenWidth: Dp,
    scaleStartButton: Float,
    showBlacksClock: Boolean,
    density: Density,
    showWhitesClock: Boolean,
    onBackgroundClicked: () -> Unit,
    onStartButtonClicked: () -> Unit,
    onPauseButtonClicked: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        val maxTimeMillis = 1 * 30 * 1000L
        val whitesTimer = remember { Timer(maxTimeMillis) }
        val blacksTimer = remember { Timer(maxTimeMillis) }

        LaunchedEffect(clockState, turn) {
            when (clockState) {
                ClockState.Started -> {
                    when (turn) {
                        Turn.Blacks -> {
                            whitesTimer.pause()
                            blacksTimer.start()
                        }
                        Turn.Whites -> {
                            whitesTimer.start()
                            blacksTimer.pause()
                        }
                    }
                }
                ClockState.Paused -> {
                    whitesTimer.pause()
                    blacksTimer.pause()
                }
                ClockState.Finished -> {
                    whitesTimer.reset()
                    blacksTimer.reset()
                }
            }
        }

        if (clockState == ClockState.Started) {
            AnimatedCircle(
                turn = turn,
                modifier = Modifier.offset(y = whitesClockTranslationY)
            )
        }
        Clocks(
            clockState = clockState,
            onBackgroundClicked = onBackgroundClicked,
            blacksClockTranslationY = blacksClockTranslationY,
            screenWidth = screenWidth,
            turn = turn,
            whitesClockTranslationY = whitesClockTranslationY,
            whitesTimer = whitesTimer,
            blacksTimer = blacksTimer,
            maxTimeMillis = maxTimeMillis
        )
        RoundedTextButton(
            modifier = Modifier.scale(scaleStartButton),
            onClick = onStartButtonClicked,
            buttonSize = screenWidth / 1.75f,
            text = "Play"
        )
        PauseButtons(
            showBlacksClock = showBlacksClock,
            enabledClockState = turn,
            density = density,
            screenWidth = screenWidth,
            onPauseButtonClicked = onPauseButtonClicked,
            showWhitesClock = showWhitesClock
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    KingsClockTheme {
        ClockScreen(whitesTime = 10, blacksTime = 10)
    }
}
