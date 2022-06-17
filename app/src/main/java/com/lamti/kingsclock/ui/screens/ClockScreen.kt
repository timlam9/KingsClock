package com.lamti.kingsclock.ui.screens

import android.content.Context
import android.graphics.Typeface
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.lamti.kingsclock.ui.composables.AnimatedCircle
import com.lamti.kingsclock.ui.composables.Clocks
import com.lamti.kingsclock.ui.composables.PauseButtons
import com.lamti.kingsclock.ui.composables.StartButton
import com.lamti.kingsclock.ui.theme.KingsClockTheme
import com.lamti.kingsclock.ui.uistate.ClockState
import com.lamti.kingsclock.ui.uistate.Timer
import com.lamti.kingsclock.ui.uistate.Turn
import kotlinx.coroutines.launch

@Composable
fun ClockScreen(whitesTime: Int, blacksTime: Int, font: Typeface?) {
    val context = LocalContext.current
    val density = LocalDensity.current
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp

    val blacksStartedTranslationValue = -screenHeight.value
    val whitesStartedTranslationValue = screenHeight.value

    var clockState by rememberSaveable { mutableStateOf(ClockState.Finished) }
    var turn by rememberSaveable { mutableStateOf(Turn.Whites) }

    var showStartButton by rememberSaveable { mutableStateOf(false) }
    var showBlacksClock by rememberSaveable { mutableStateOf(false) }
    var showWhitesClock by rememberSaveable { mutableStateOf(false) }

    val scaleStartButton by animateFloatAsState(if (showStartButton) 1f else 0f)
    val blacksClockTranslationY = remember { Animatable(blacksStartedTranslationValue) }
    val whitesClockTranslationY = remember { Animatable(whitesStartedTranslationValue) }

    LaunchedEffect(clockState) {
        when (clockState) {
            ClockState.Started -> {
                showStartButton = false
                showBlacksClock = true
                showWhitesClock = true
                launch {
                    blacksClockTranslationY.animateTo(0f)
                }
                launch {
                    whitesClockTranslationY.animateTo(0f)
                }
            }
            ClockState.Paused -> {
                showStartButton = true
                showBlacksClock = false
                showWhitesClock = false
                launch {
                    blacksClockTranslationY.animateTo(blacksStartedTranslationValue)
                }
                launch {
                    whitesClockTranslationY.animateTo(whitesStartedTranslationValue)
                }
            }
            ClockState.Finished -> {
                showStartButton = true
                showBlacksClock = false
                showWhitesClock = false
                launch {
                    blacksClockTranslationY.animateTo(blacksStartedTranslationValue)
                }
                launch {
                    whitesClockTranslationY.animateTo(whitesStartedTranslationValue)
                }
            }
        }
    }

    ClockScreen(
        clockState = clockState,
        turn = turn,
        blacksClockTranslationY = blacksClockTranslationY,
        screenWidth = screenWidth,
        context = context,
        font = font,
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
    blacksClockTranslationY: Animatable<Float, AnimationVector1D>,
    screenWidth: Dp,
    context: Context,
    font: Typeface?,
    whitesClockTranslationY: Animatable<Float, AnimationVector1D>,
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
        if (clockState == ClockState.Started) {
            AnimatedCircle(turn = turn, modifier = Modifier.offset { IntOffset(0, whitesClockTranslationY.value.toInt()) })
        }

        val maxTimeMillis = 5 * 60 * 1000L
        val whitesTimer = remember { Timer(maxTimeMillis) }
        val blacksTimer = remember { Timer(maxTimeMillis) }

        LaunchedEffect(clockState) {
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
        LaunchedEffect(turn) {
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

        Clocks(
            clockState = clockState,
            onBackgroundClicked = onBackgroundClicked,
            blacksClockTranslationY = blacksClockTranslationY,
            screenWidth = screenWidth,
            turn = turn,
            context = context,
            font = font,
            whitesClockTranslationY = whitesClockTranslationY,
            whitesTimer = whitesTimer,
            blacksTimer = blacksTimer,
            maxTimeMillis = maxTimeMillis
        )
        StartButton(
            modifier = Modifier.scale(scaleStartButton),
            font = font,
            onClick = onStartButtonClicked
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
        ClockScreen(whitesTime = 10, blacksTime = 10, null)
    }
}
