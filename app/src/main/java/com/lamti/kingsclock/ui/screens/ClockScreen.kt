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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.lamti.kingsclock.ui.composables.Clocks
import com.lamti.kingsclock.ui.composables.FinishButtons
import com.lamti.kingsclock.ui.composables.PauseButtons
import com.lamti.kingsclock.ui.composables.basic.AnimatedCircle
import com.lamti.kingsclock.ui.composables.basic.RoundedTextButton
import com.lamti.kingsclock.ui.theme.KingsClockTheme
import com.lamti.kingsclock.ui.uistate.ClockState
import com.lamti.kingsclock.ui.uistate.Timer
import com.lamti.kingsclock.ui.uistate.Turn

@Composable
fun ClockScreen() {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp

    val blacksStartedTranslationValue = -screenHeight
    val whitesStartedTranslationValue = screenHeight

    var clockState by rememberSaveable { mutableStateOf(ClockState.Paused) }
    var turn by rememberSaveable { mutableStateOf(Turn.Whites) }

    var showStartButton by rememberSaveable { mutableStateOf(false) }
    var showBlacksClock by rememberSaveable { mutableStateOf(false) }
    var showWhitesClock by rememberSaveable { mutableStateOf(false) }
    var showRestartButton by rememberSaveable { mutableStateOf(false) }
    var showCloseButton by rememberSaveable { mutableStateOf(false) }

    val scaleStartButton by animateFloatAsState(if (showStartButton) 1f else 0f)
    val whitesClockTranslationY by animateDpAsState(
        when (clockState) {
            ClockState.Started -> 0.dp
            ClockState.Paused -> whitesStartedTranslationValue
            ClockState.Finished -> 0.dp
        }
    )
    val blacksClockTranslationY by animateDpAsState(
        when (clockState) {
            ClockState.Started -> 0.dp
            ClockState.Paused -> blacksStartedTranslationValue
            ClockState.Finished -> 0.dp
        }
    )

    LaunchedEffect(clockState) {
        when (clockState) {
            ClockState.Started -> {
                showStartButton = false
                showBlacksClock = true
                showWhitesClock = true
                showRestartButton = false
                showCloseButton = false
            }
            ClockState.Paused -> {
                showStartButton = true
                showBlacksClock = false
                showWhitesClock = false
                showRestartButton = false
                showCloseButton = false
            }
            ClockState.Finished -> {
                showStartButton = false
                showBlacksClock = true
                showWhitesClock = true
                showRestartButton = true
                showCloseButton = true
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
        showWhitesClock = showWhitesClock,
        showRestartButton = showRestartButton,
        showCloseButton = showCloseButton,
        onBackgroundClicked = { turn = turn.changeClock() },
        onStartButtonClicked = { clockState = ClockState.Started },
        onPauseButtonClicked = { clockState = ClockState.Paused },
        onBlacksTimerFinished = { clockState = ClockState.Finished },
        onWhitesTimerFinished = { clockState = ClockState.Finished },
        onRestartButtonClicked = {
            turn = Turn.Whites
            clockState = ClockState.Started
        },
        onCloseButtonClicked = {
            turn = Turn.Whites
            clockState = ClockState.Paused
        },
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
    showWhitesClock: Boolean,
    showRestartButton: Boolean,
    showCloseButton: Boolean,
    onBackgroundClicked: () -> Unit,
    onStartButtonClicked: () -> Unit,
    onPauseButtonClicked: () -> Unit,
    onBlacksTimerFinished: () -> Unit,
    onWhitesTimerFinished: () -> Unit,
    onRestartButtonClicked: () -> Unit,
    onCloseButtonClicked: () -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        val maxTimeMillis = 1 * 12 * 1000L
        val whitesTimer = remember { Timer(maxTimeMillis) }
        val blacksTimer = remember { Timer(maxTimeMillis) }

        LaunchedEffect(blacksTimer.isTimerFinished) {
            if (blacksTimer.isTimerFinished) onBlacksTimerFinished()
        }
        LaunchedEffect(whitesTimer.isTimerFinished) {
            if (whitesTimer.isTimerFinished) onWhitesTimerFinished()
        }

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
        if (clockState != ClockState.Finished) {
            PauseButtons(
                showBlacksClock = showBlacksClock,
                enabledClockState = turn,
                screenWidth = screenWidth,
                onPauseButtonClicked = onPauseButtonClicked,
                showWhitesClock = showWhitesClock
            )
        }
        FinishButtons(
            showRestartButton = showRestartButton,
            showCloseButton = showCloseButton,
            onRestartButtonClicked = {
                blacksTimer.reset()
                whitesTimer.reset()
                onRestartButtonClicked()
            },
            onCloseButtonClicked = {
                blacksTimer.reset()
                whitesTimer.reset()
                onCloseButtonClicked()
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    KingsClockTheme {
        ClockScreen()
    }
}
