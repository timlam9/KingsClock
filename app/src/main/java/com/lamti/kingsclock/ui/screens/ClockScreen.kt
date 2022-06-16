package com.lamti.kingsclock.ui.screens

import android.content.Context
import android.graphics.Typeface
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.lamti.kingsclock.R
import com.lamti.kingsclock.ui.composables.BlacksClock
import com.lamti.kingsclock.ui.composables.RoundedIcon
import com.lamti.kingsclock.ui.composables.StartButton
import com.lamti.kingsclock.ui.composables.WhitesClock
import com.lamti.kingsclock.ui.theme.Green
import com.lamti.kingsclock.ui.theme.KingsClockTheme
import com.lamti.kingsclock.ui.theme.Red
import com.lamti.kingsclock.ui.uistate.ClockState
import com.lamti.kingsclock.ui.uistate.EnabledClock
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
    var enabledClockState by rememberSaveable { mutableStateOf(EnabledClock.Whites) }

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
        enabledClockState = enabledClockState,
        blacksClockTranslationY = blacksClockTranslationY,
        screenWidth = screenWidth,
        context = context,
        font = font,
        whitesClockTranslationY = whitesClockTranslationY,
        scaleStartButton = scaleStartButton,
        showBlacksClock = showBlacksClock,
        density = density,
        showWhitesClock = showWhitesClock,
        onBackgroundClicked = { enabledClockState = enabledClockState.changeClock() },
        onStartButtonClicked = { clockState = ClockState.Started },
        onPauseButtonClicked = { clockState = ClockState.Paused }
    )
}

@Composable
private fun ClockScreen(
    clockState: ClockState,
    enabledClockState: EnabledClock,
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
        Clocks(
            clockState = clockState,
            onBackgroundClicked = onBackgroundClicked,
            blacksClockTranslationY = blacksClockTranslationY,
            screenWidth = screenWidth,
            enabledClockState = enabledClockState,
            context = context,
            font = font,
            whitesClockTranslationY = whitesClockTranslationY
        )
        StartButton(
            modifier = Modifier.scale(scaleStartButton),
            onClick = onStartButtonClicked
        )
        PauseButtons(
            showBlacksClock = showBlacksClock,
            enabledClockState = enabledClockState,
            density = density,
            screenWidth = screenWidth,
            onPauseButtonClicked = onPauseButtonClicked,
            showWhitesClock = showWhitesClock
        )
    }
}

@Composable
private fun Clocks(
    clockState: ClockState,
    onBackgroundClicked: () -> Unit,
    blacksClockTranslationY: Animatable<Float, AnimationVector1D>,
    screenWidth: Dp,
    enabledClockState: EnabledClock,
    context: Context,
    font: Typeface?,
    whitesClockTranslationY: Animatable<Float, AnimationVector1D>
) {
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
            enabled = enabledClockState == EnabledClock.Blacks,
            textColor = ContextCompat.getColor(context, R.color.red),
            font = font
        )
        WhitesClock(
            modifier = Modifier.offset { IntOffset(0, whitesClockTranslationY.value.toInt()) },
            clockSize = screenWidth,
            enabled = enabledClockState == EnabledClock.Whites,
            textColor = ContextCompat.getColor(context, R.color.green),
            font = font
        )
    }
}

@Composable
private fun PauseButtons(
    showBlacksClock: Boolean,
    enabledClockState: EnabledClock,
    density: Density,
    screenWidth: Dp,
    onPauseButtonClicked: () -> Unit,
    showWhitesClock: Boolean
) {
    AnimatedVisibility(
        visible = showBlacksClock && enabledClockState == EnabledClock.Blacks,
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
            color = MaterialTheme.colors.onBackground,
            tint = Red,
            onClick = onPauseButtonClicked
        )
    }
    AnimatedVisibility(
        visible = showWhitesClock && enabledClockState == EnabledClock.Whites,
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
            color = MaterialTheme.colors.onBackground,
            tint = Green,
            onClick = onPauseButtonClicked
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
