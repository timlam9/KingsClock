package com.lamti.kingsclock.ui.screens

import android.os.Build
import android.util.Log
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.lamti.kingsclock.R
import com.lamti.kingsclock.ui.composables.basic.AnimatedCircle
import com.lamti.kingsclock.ui.composables.basic.OutlineIcon
import com.lamti.kingsclock.ui.composables.basic.RoundedTextButton
import com.lamti.kingsclock.ui.composables.combound.Clocks
import com.lamti.kingsclock.ui.composables.combound.FinishButtons
import com.lamti.kingsclock.ui.composables.combound.MainHeader
import com.lamti.kingsclock.ui.composables.combound.PauseButtons
import com.lamti.kingsclock.ui.composables.combound.TouchIndicator
import com.lamti.kingsclock.ui.drawColoredShadow
import com.lamti.kingsclock.ui.playSound
import com.lamti.kingsclock.ui.theme.Blue
import com.lamti.kingsclock.ui.theme.KingsClockTheme
import com.lamti.kingsclock.ui.theme.Red
import com.lamti.kingsclock.ui.uistate.ClockState
import com.lamti.kingsclock.ui.uistate.UIEvent
import com.lamti.kingsclock.ui.uistate.UIState
import kotlinx.coroutines.channels.Channel

@Composable
fun ClockScreen(
    state: UIState,
    eventChannel: Channel<UIEvent>
) {
    LaunchedEffect(false) {
        eventChannel.trySend(UIEvent.Initialize)
    }

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp

    val touchIndicatorRotateValue by animateFloatAsState(if (state.rotateTouchIndicator) 180f else 0f)
    val startButtonScale by animateFloatAsState(if (state.showPauseWidgets) 1f else 0f)

    val blacksTimerTranslationX by animateDpAsState(
        targetValue = if (state.showPauseWidgets) 0.dp else -screenWidth,
        animationSpec = tween(
            durationMillis = 150,
            delayMillis = if (state.showPauseWidgets) 100 else 30,
            easing = FastOutSlowInEasing
        ),
    )
    val whitesTimerTranslationX by animateDpAsState(
        targetValue = if (state.showPauseWidgets) 0.dp else -screenWidth,
        animationSpec = tween(
            durationMillis = 150,
            delayMillis = if (state.showPauseWidgets) 140 else 70,
            easing = FastOutSlowInEasing
        ),
    )
    val settingsIconTranslationY by animateDpAsState(
        targetValue = if (state.showPauseWidgets && !state.showMenuCloseIcon) screenHeight / 2.7f else screenHeight,
        animationSpec = tween(
            durationMillis = 100,
            delayMillis = if (state.showPauseWidgets && !state.showMenuCloseIcon) 170 else 25,
            easing = FastOutSlowInEasing
        ),
    )
    val menuCloseIconTranslationY by animateDpAsState(
        targetValue = if (state.showMenuCloseIcon) screenHeight / 2.7f else screenHeight,
        animationSpec = tween(
            durationMillis = 100,
            delayMillis = if (state.showMenuCloseIcon) 170 else 25,
            easing = FastOutSlowInEasing
        ),
    )
    val whitesClockTranslationY by animateDpAsState(
        when (state.clockState) {
            ClockState.Idle -> screenHeight
            ClockState.Started -> 0.dp
            ClockState.Paused -> screenHeight
            ClockState.Finished -> 0.dp
        }
    )
    val blacksClockTranslationY by animateDpAsState(
        when (state.clockState) {
            ClockState.Idle -> -screenHeight
            ClockState.Started -> 0.dp
            ClockState.Paused -> -screenHeight
            ClockState.Finished -> 0.dp
        }
    )

    ClockScreen(
        state = state,
        eventChannel = eventChannel,
        screenWidth = screenWidth,
        touchIndicatorRotateValue = touchIndicatorRotateValue,
        scaleStartButton = startButtonScale,
        blacksClockTranslationY = blacksClockTranslationY,
        whitesClockTranslationY = whitesClockTranslationY,
        blacksTimerTranslationX = blacksTimerTranslationX,
        whitesTimerTranslationX = whitesTimerTranslationX,
        settingsIconTranslationY = settingsIconTranslationY,
        menuCloseIconTranslationY = menuCloseIconTranslationY,
    )
}

@Composable
private fun ClockScreen(
    state: UIState,
    eventChannel: Channel<UIEvent>,
    screenWidth: Dp,
    touchIndicatorRotateValue: Float,
    scaleStartButton: Float,
    blacksClockTranslationY: Dp,
    whitesClockTranslationY: Dp,
    blacksTimerTranslationX: Dp,
    whitesTimerTranslationX: Dp,
    settingsIconTranslationY: Dp,
    menuCloseIconTranslationY: Dp,
) {
    val context = LocalContext.current
    var sound by remember { mutableStateOf(R.raw.intro) }

    LaunchedEffect(state.clockState) {
        if (state.clockState == ClockState.Finished) sound = R.raw.game_over
    }

    LaunchedEffect(sound) {
        try {
            context.playSound(sound)
        } catch (e: Exception) {
            Log.e("TAGARA", "Ex: ${e.message}")
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        TouchIndicator(
            showTouchIndicator = state.showTouchIndicator,
            touchIndicatorRotateValue = touchIndicatorRotateValue
        )
        AnimatedCircle(
            turn = state.turn,
            showCircle = state.clockState == ClockState.Started,
            modifier = Modifier.offset(y = whitesClockTranslationY)
        )
        Clocks(
            clockState = state.clockState,
            onBackgroundClicked = {
                eventChannel.trySend(UIEvent.BackgroundClicked)
                sound = if (sound == R.raw.blacks) R.raw.whites else R.raw.blacks
            },
            blacksClockTranslationY = blacksClockTranslationY,
            screenWidth = screenWidth,
            turn = state.turn,
            whitesClockTranslationY = whitesClockTranslationY,
            whitesTimer = state.whitesTimer,
            blacksTimer = state.blacksTimer,
            maxTimeMillis = (state.clock.minutes * 60 * 1000L) + (state.clock.seconds * 1000L)
        )
        RoundedTextButton(
            modifier = Modifier.startModifier(scaleStartButton),
            buttonSize = screenWidth / 1.75f,
            text = state.startButtonText,
            onClick = {
                sound = R.raw.start
                eventChannel.trySend(UIEvent.StartButtonClicked)
            },
        )
        MainHeader(
            screenWidth = screenWidth,
            blacksTimerTranslationX = blacksTimerTranslationX,
            whitesTimerTranslationX = whitesTimerTranslationX,
            blacksTimerText = state.blacksTimer.formattedTime,
            whitesTimerText = state.whitesTimer.formattedTime
        )
        PauseButtons(
            showBlacksClock = state.showBlacksClock,
            enabledClockState = state.turn,
            screenWidth = screenWidth,
            showWhitesClock = state.showWhitesClock,
            showButtons = state.clockState != ClockState.Finished,
            onPauseButtonClicked = {
                sound = R.raw.pause
                eventChannel.trySend(UIEvent.PauseButtonClicked)
            },
        )
        FinishButtons(
            showRestartButton = state.showRestartButton,
            showCloseButton = state.showCloseButton,
            onRestartButtonClicked = {
                sound = R.raw.start
                eventChannel.trySend(UIEvent.RestartButtonClicked)
            },
            onCloseButtonClicked = {
                sound = R.raw.exit
                eventChannel.trySend(UIEvent.CloseButtonClicked)
            }
        )
        OutlineIcon(
            modifier = Modifier.offset(y = settingsIconTranslationY),
            imageID = R.drawable.ic_settings,
            size = 70.dp,
            borderColor = MaterialTheme.colors.onSecondary,
            onClick = {
                sound = R.raw.click
                eventChannel.trySend(UIEvent.SettingsClicked)
            }
        )
        OutlineIcon(
            modifier = Modifier.offset(y = menuCloseIconTranslationY),
            imageID = R.drawable.ic_close,
            size = 70.dp,
            borderColor = Red,
            onClick = {
                sound = R.raw.exit
                eventChannel.trySend(UIEvent.CloseButtonClicked)
            }
        )
    }
}

private fun Modifier.startModifier(scaleStartButton: Float) = composed {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && MaterialTheme.colors.isLight) {
        this
            .scale(scaleStartButton)
            .drawColoredShadow(color = Blue)
    } else {
        this.scale(scaleStartButton)
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    KingsClockTheme {
        ClockScreen(UIState.initialState, Channel { })
    }
}
