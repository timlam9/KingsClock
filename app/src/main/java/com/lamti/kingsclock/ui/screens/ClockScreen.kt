package com.lamti.kingsclock.ui.screens

import android.media.MediaPlayer
import android.os.Build
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lamti.kingsclock.R
import com.lamti.kingsclock.ui.composables.basic.AnimatedCircle
import com.lamti.kingsclock.ui.composables.basic.OutlineIcon
import com.lamti.kingsclock.ui.composables.basic.RoundedTextButton
import com.lamti.kingsclock.ui.composables.combound.Clocks
import com.lamti.kingsclock.ui.composables.combound.FinishButtons
import com.lamti.kingsclock.ui.composables.combound.IconTextRow
import com.lamti.kingsclock.ui.composables.combound.PauseButtons
import com.lamti.kingsclock.ui.drawColoredShadow
import com.lamti.kingsclock.ui.theme.Blue
import com.lamti.kingsclock.ui.theme.KingsClockTheme
import com.lamti.kingsclock.ui.theme.Red
import com.lamti.kingsclock.ui.uistate.ClockState
import com.lamti.kingsclock.ui.uistate.Timer
import com.lamti.kingsclock.ui.uistate.Turn
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

    val blacksStartedTranslationValue = -screenHeight
    val whitesStartedTranslationValue = screenHeight


    val touchIndicatorRotateValue by animateFloatAsState(if (state.rotateTouchIndicator) 180f else 0f)
    val playButtonScale by animateFloatAsState(if (state.showPauseWidgets) 1f else 0f)

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
            ClockState.Idle -> whitesStartedTranslationValue
            ClockState.Started -> 0.dp
            ClockState.Paused -> whitesStartedTranslationValue
            ClockState.Finished -> 0.dp
        }
    )
    val blacksClockTranslationY by animateDpAsState(
        when (state.clockState) {
            ClockState.Idle -> blacksStartedTranslationValue
            ClockState.Started -> 0.dp
            ClockState.Paused -> blacksStartedTranslationValue
            ClockState.Finished -> 0.dp
        }
    )

    ClockScreen(
        clockState = state.clockState,
        turn = state.turn,
        maxTimeMillis = (state.clock.minutes * 60 * 1000L) + (state.clock.seconds * 1000L),
        playButtonText = state.startButtonText,
        screenWidth = screenWidth,
        blacksClockTranslationY = blacksClockTranslationY,
        whitesClockTranslationY = whitesClockTranslationY,
        scaleStartButton = playButtonScale,
        blacksTimerTranslationX = blacksTimerTranslationX,
        whitesTimerTranslationX = whitesTimerTranslationX,
        settingsIconTranslationY = settingsIconTranslationY,
        menuCloseIconTranslationY = menuCloseIconTranslationY,
        showBlacksClock = state.showBlacksClock,
        showWhitesClock = state.showWhitesClock,
        showRestartButton = state.showRestartButton,
        showCloseButton = state.showCloseButton,
        showTutorial = state.showTouchIndicator,
        touchIndicatorRotateValue = touchIndicatorRotateValue,
        onBackgroundClicked = { eventChannel.trySend(UIEvent.BackgroundClicked) },
        onStartButtonClicked = { eventChannel.trySend(UIEvent.StartButtonClicked) },
        onPauseButtonClicked = { eventChannel.trySend(UIEvent.PauseButtonClicked) },
        onBlacksTimerFinished = { eventChannel.trySend(UIEvent.BlacksTimerFinished) },
        onWhitesTimerFinished = { eventChannel.trySend(UIEvent.WhitesTimerFinished) },
        onRestartButtonClicked = { eventChannel.trySend(UIEvent.RestartButtonClicked) },
        onCloseButtonClicked = { eventChannel.trySend(UIEvent.CloseButtonClicked) },
        onSettingsClicked = { eventChannel.trySend(UIEvent.SettingsClicked) }
    )
}

@Composable
private fun ClockScreen(
    clockState: ClockState,
    turn: Turn,
    maxTimeMillis: Long,
    playButtonText: String,
    blacksClockTranslationY: Dp,
    whitesClockTranslationY: Dp,
    screenWidth: Dp,
    scaleStartButton: Float,
    blacksTimerTranslationX: Dp,
    whitesTimerTranslationX: Dp,
    settingsIconTranslationY: Dp,
    menuCloseIconTranslationY: Dp,
    showBlacksClock: Boolean,
    showWhitesClock: Boolean,
    showRestartButton: Boolean,
    showCloseButton: Boolean,
    showTutorial: Boolean,
    touchIndicatorRotateValue: Float,
    onBackgroundClicked: () -> Unit,
    onStartButtonClicked: () -> Unit,
    onPauseButtonClicked: () -> Unit,
    onBlacksTimerFinished: () -> Unit,
    onWhitesTimerFinished: () -> Unit,
    onRestartButtonClicked: () -> Unit,
    onCloseButtonClicked: () -> Unit,
    onSettingsClicked: () -> Unit,
) {
    val context = LocalContext.current
    var sound by remember { mutableStateOf(R.raw.intro) }

    LaunchedEffect(sound) {
        try {
            MediaPlayer.create(context, sound).start()
        } catch (e: Exception) {
            Log.e("TAGARA", "Ex: ${e.message}")
        }
    }

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
                sound = R.raw.game_over
            }
            ClockState.Idle -> Unit
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        AnimatedVisibility(visible = showTutorial) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    modifier = Modifier
                        .padding(end = 12.dp)
                        .size(32.dp)
                        .rotate(touchIndicatorRotateValue),
                    painter = painterResource(id = R.drawable.ic_touch),
                    contentDescription = "Rounded Image Button",
                    tint = MaterialTheme.colors.onSecondary
                )
                Text(
                    modifier = Modifier.rotate(-touchIndicatorRotateValue),
                    text = "touch anywhere",
                    style = MaterialTheme.typography.h6.copy(
                        color = MaterialTheme.colors.onSecondary,
                        fontWeight = FontWeight.ExtraBold,
                        fontFamily = FontFamily.Monospace,
                        letterSpacing = 4.sp,
                        textAlign = TextAlign.Center,
                    )
                )
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
            onBackgroundClicked = {
                onBackgroundClicked()
                sound = if (sound == R.raw.blacks) R.raw.whites else R.raw.blacks
            },
            blacksClockTranslationY = blacksClockTranslationY,
            screenWidth = screenWidth,
            turn = turn,
            whitesClockTranslationY = whitesClockTranslationY,
            whitesTimer = whitesTimer,
            blacksTimer = blacksTimer,
            maxTimeMillis = maxTimeMillis
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            RoundedTextButton(
                modifier = Modifier
                    .scale(scaleStartButton)
                    .drawColoredShadow(color = Blue),
                onClick = {
                    sound = R.raw.start
                    onStartButtonClicked()
                },
                buttonSize = screenWidth / 1.75f,
                text = playButtonText
            )
        } else {
            RoundedTextButton(
                modifier = Modifier.scale(scaleStartButton),
                onClick = {
                    sound = R.raw.start
                    onStartButtonClicked()
                },
                buttonSize = screenWidth / 1.75f,
                text = playButtonText
            )
        }
        Column(modifier = Modifier.offset(y = -screenWidth / 1.4f)) {
            IconTextRow(
                offset = blacksTimerTranslationX,
                text = blacksTimer.formattedTime,
                color = MaterialTheme.colors.onSecondary,
                textBackgroundColor = MaterialTheme.colors.onSecondary,
                borderColor = MaterialTheme.colors.onSecondary,
                iconBackgroundColor = MaterialTheme.colors.background,
                textColor = MaterialTheme.colors.background,
            )
            Spacer(modifier = Modifier.size(20.dp))
            IconTextRow(
                offset = whitesTimerTranslationX,
                text = whitesTimer.formattedTime,
                color = MaterialTheme.colors.background,
                textBackgroundColor = MaterialTheme.colors.background,
                borderColor = MaterialTheme.colors.onSecondary,
                iconBackgroundColor = MaterialTheme.colors.onSecondary,
                textColor = MaterialTheme.colors.onSecondary,
            )
        }
        AnimatedVisibility(clockState != ClockState.Finished) {
            PauseButtons(
                showBlacksClock = showBlacksClock,
                enabledClockState = turn,
                screenWidth = screenWidth,
                onPauseButtonClicked = {
                    sound = R.raw.pause
                    onPauseButtonClicked()
                },
                showWhitesClock = showWhitesClock
            )
        }
        FinishButtons(
            showRestartButton = showRestartButton,
            showCloseButton = showCloseButton,
            onRestartButtonClicked = {
                sound = R.raw.start
                blacksTimer.reset()
                whitesTimer.reset()
                onRestartButtonClicked()
            },
            onCloseButtonClicked = {
                sound = R.raw.exit
                blacksTimer.reset()
                whitesTimer.reset()
                onCloseButtonClicked()
            }
        )
        OutlineIcon(
            modifier = Modifier.offset(y = settingsIconTranslationY),
            imageID = R.drawable.ic_settings,
            size = 70.dp,
            borderColor = MaterialTheme.colors.onSecondary,
            onClick = {
                sound = R.raw.click
                onSettingsClicked()
            }
        )
        OutlineIcon(
            modifier = Modifier.offset(y = menuCloseIconTranslationY),
            imageID = R.drawable.ic_close,
            size = 70.dp,
            borderColor = Red
        ) {
            sound = R.raw.exit
            blacksTimer.reset()
            whitesTimer.reset()
            onCloseButtonClicked()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    KingsClockTheme {
        ClockScreen(UIState.initialState, Channel { })
    }
}
