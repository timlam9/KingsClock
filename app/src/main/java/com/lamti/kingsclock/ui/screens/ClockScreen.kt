package com.lamti.kingsclock.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
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
import androidx.compose.ui.unit.sp
import com.lamti.kingsclock.R
import com.lamti.kingsclock.ui.composables.basic.AnimatedCircle
import com.lamti.kingsclock.ui.composables.basic.OutlineIcon
import com.lamti.kingsclock.ui.composables.basic.OutlinedButton
import com.lamti.kingsclock.ui.composables.basic.RoundedTextButton
import com.lamti.kingsclock.ui.composables.combound.Clocks
import com.lamti.kingsclock.ui.composables.combound.FinishButtons
import com.lamti.kingsclock.ui.composables.combound.PauseButtons
import com.lamti.kingsclock.ui.theme.Blue
import com.lamti.kingsclock.ui.theme.Green
import com.lamti.kingsclock.ui.theme.KingsClockTheme
import com.lamti.kingsclock.ui.uistate.ChessClock
import com.lamti.kingsclock.ui.uistate.ClockState
import com.lamti.kingsclock.ui.uistate.Timer
import com.lamti.kingsclock.ui.uistate.Turn

@Composable
fun ClockScreen(chessClock: ChessClock, onSettingsClicked: () -> Unit) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp

    val blacksStartedTranslationValue = -screenHeight
    val whitesStartedTranslationValue = screenHeight

    var clockState by rememberSaveable { mutableStateOf(ClockState.Paused) }
    var turn by rememberSaveable { mutableStateOf(Turn.Whites) }

    var showPauseWidgets by rememberSaveable { mutableStateOf(false) }
    var showBlacksClock by rememberSaveable { mutableStateOf(false) }
    var showWhitesClock by rememberSaveable { mutableStateOf(false) }
    var showRestartButton by rememberSaveable { mutableStateOf(false) }
    var showCloseButton by rememberSaveable { mutableStateOf(false) }

    val scaleStartButton by animateFloatAsState(if (showPauseWidgets) 1f else 0f)
    val blacksTimerTranslationX by animateDpAsState(
        targetValue = if (showPauseWidgets) 0.dp else -screenWidth,
        animationSpec = tween(
            durationMillis = 150,
            delayMillis = if (showPauseWidgets) 100 else 30,
            easing = FastOutSlowInEasing
        ),
    )
    val whitesTimerTranslationX by animateDpAsState(
        targetValue = if (showPauseWidgets) 0.dp else -screenWidth,
        animationSpec = tween(
            durationMillis = 150,
            delayMillis = if (showPauseWidgets) 140 else 70,
            easing = FastOutSlowInEasing
        ),
    )
    val settingsIconTranslationY by animateDpAsState(
        targetValue = if (showPauseWidgets) screenHeight / 2.35f else screenHeight,
        animationSpec = tween(
            durationMillis = 100,
            delayMillis = if (showPauseWidgets) 170 else 25,
            easing = FastOutSlowInEasing
        ),
    )

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
                showPauseWidgets = false
                showBlacksClock = true
                showWhitesClock = true
                showRestartButton = false
                showCloseButton = false
            }
            ClockState.Paused -> {
                showPauseWidgets = true
                showBlacksClock = false
                showWhitesClock = false
                showRestartButton = false
                showCloseButton = false
            }
            ClockState.Finished -> {
                showPauseWidgets = false
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
        maxTimeMillis = (chessClock.minutes * 60 * 1000L) + (chessClock.seconds * 1000L),
        screenWidth = screenWidth,
        blacksClockTranslationY = blacksClockTranslationY,
        whitesClockTranslationY = whitesClockTranslationY,
        scaleStartButton = scaleStartButton,
        blacksTimerTranslationX = blacksTimerTranslationX,
        whitesTimerTranslationX = whitesTimerTranslationX,
        settingsIconTranslationY = settingsIconTranslationY,
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
        onSettingsClicked = onSettingsClicked
    )
}

@Composable
private fun ClockScreen(
    clockState: ClockState,
    turn: Turn,
    maxTimeMillis: Long,
    blacksClockTranslationY: Dp,
    whitesClockTranslationY: Dp,
    screenWidth: Dp,
    scaleStartButton: Float,
    blacksTimerTranslationX: Dp,
    whitesTimerTranslationX: Dp,
    settingsIconTranslationY: Dp,
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
    onSettingsClicked: () -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
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
        Column(modifier = Modifier.offset(y = -screenWidth / 1.4f)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(x = blacksTimerTranslationX)
                    .padding(horizontal = 20.dp)
            ) {
                OutlineIcon(
                    modifier = Modifier,
                    size = 70.dp,
                    imageID = R.drawable.ic_rocket_launch,
                    color = Blue,
                    borderColor = Blue
                )
                Spacer(modifier = Modifier.size(20.dp))
                OutlinedButton(
                    modifier = Modifier.weight(1f),
                    text = blacksTimer.formattedTime,
                    width = screenWidth / 1.75f,
                    fontSize = 32.sp,
                    color = Blue,
                    onclick = {}
                )
            }
            Spacer(modifier = Modifier.size(20.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(x = whitesTimerTranslationX)
                    .padding(horizontal = 20.dp)
            ) {
                OutlineIcon(
                    modifier = Modifier,
                    size = 70.dp,
                    imageID = R.drawable.ic_outline_rocket_launch,
                    color = Green,
                    borderColor = Green
                )
                Spacer(modifier = Modifier.size(20.dp))
                OutlinedButton(
                    modifier = Modifier.weight(1f),
                    text = whitesTimer.formattedTime,
                    width = screenWidth / 1.75f,
                    fontSize = 32.sp,
                    color = Green,
                    onclick = {}
                )
            }
        }
        AnimatedVisibility(clockState != ClockState.Finished) {
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
        OutlineIcon(
            modifier = Modifier.offset(y = settingsIconTranslationY),
            size = 70.dp,
            imageID = R.drawable.ic_settings,
            borderColor = MaterialTheme.colors.onSecondary,
            onClick = onSettingsClicked
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    KingsClockTheme {
        ClockScreen(ChessClock(10, 0)) {

        }
    }
}
