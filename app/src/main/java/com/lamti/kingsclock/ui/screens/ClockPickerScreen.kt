package com.lamti.kingsclock.ui.screens

import androidx.compose.animation.animateColor
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.repeatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Slider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lamti.kingsclock.R
import com.lamti.kingsclock.ui.composables.basic.BasicButton
import com.lamti.kingsclock.ui.composables.basic.OutlineIcon
import com.lamti.kingsclock.ui.composables.picker.ChessClockPicker
import com.lamti.kingsclock.ui.playSound
import com.lamti.kingsclock.ui.theme.Blue
import com.lamti.kingsclock.ui.theme.TextColor
import com.lamti.kingsclock.ui.uistate.ChessClock
import com.lamti.kingsclock.ui.uistate.ClockMode
import com.lamti.kingsclock.ui.uistate.UIState

@Composable
fun ClockPickerScreen(
    modifier: Modifier = Modifier,
    miniSpace: Dp = 12.dp,
    space: Dp = 24.dp,
    state: UIState,
    onTimeSelected: (ClockMode, ChessClock) -> Unit,
) {
    val context = LocalContext.current
    var increment: Int by remember { mutableStateOf(state.clock.increment) }
    var clockMode: ClockMode by remember { mutableStateOf(state.clockMode) }
    var pickerValue: ChessClock by remember { mutableStateOf(clockMode.clock) }
    val transition = updateTransition(targetState = clockMode, label = "animation")

    var animateModeColor by remember { mutableStateOf(false) }
    val animatedColor by animateColorAsState(
        targetValue = if (animateModeColor) MaterialTheme.colors.onBackground else Blue,
        animationSpec = repeatable(
            iterations = 1,
            animation = tween(
                durationMillis = 150,
            ),
            repeatMode = RepeatMode.Restart,
        ),
        finishedListener = {
            animateModeColor = false
        }
    )
    val animatedTimeColor by animateColorAsState(
        targetValue = if (animateModeColor) Blue else MaterialTheme.colors.onBackground,
        animationSpec = repeatable(
            iterations = 1,
            animation = tween(
                durationMillis = 150,
            ),
            repeatMode = RepeatMode.Restart,
        ),
        finishedListener = {
            animateModeColor = false
        }
    )

    var animateIncrementColor by remember { mutableStateOf(false) }
    val animatedIncrementColor by animateColorAsState(
        targetValue = if (animateIncrementColor) MaterialTheme.colors.onBackground else Blue,
        animationSpec = repeatable(
            iterations = 1,
            animation = tween(
                durationMillis = 150,
            ),
            repeatMode = RepeatMode.Restart,
        ),
        finishedListener = {
            animateIncrementColor = false
        }
    )

    LaunchedEffect(clockMode) { if (clockMode != ClockMode.Custom) pickerValue = clockMode.clock }

    LaunchedEffect(true) {
        context.playSound()
    }

    val bulletIconColor by transition.animateColor(label = "color") { mode ->
        when (mode) {
            ClockMode.Bullet -> MaterialTheme.colors.primary
            else -> MaterialTheme.colors.onSecondary
        }
    }
    val blitzIconColor by transition.animateColor(label = "color") { mode ->
        when (mode) {
            ClockMode.Blitz -> MaterialTheme.colors.primary
            else -> MaterialTheme.colors.onSecondary
        }
    }
    val rapidIconColor by transition.animateColor(label = "color") { mode ->
        when (mode) {
            ClockMode.Rapid -> MaterialTheme.colors.primary
            else -> MaterialTheme.colors.onSecondary
        }
    }
    val classicalIconColor by transition.animateColor(label = "color") { mode ->
        when (mode) {
            ClockMode.Classical -> MaterialTheme.colors.primary
            else -> MaterialTheme.colors.onSecondary
        }
    }
    val customIconColor by transition.animateColor(label = "color") { mode ->
        when (mode) {
            ClockMode.Custom -> MaterialTheme.colors.primary
            else -> MaterialTheme.colors.onSecondary
        }
    }

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.Start
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(space)
        ) {
            Header()
            Spacer(modifier = Modifier.size(space))
            SubTitle(title = "Mode: ", text = clockMode.name, color = animatedColor)
            Spacer(modifier = Modifier.size(miniSpace))
            IconsRaw(
                customIconColor = customIconColor,
                bulletIconColor = bulletIconColor,
                blitzIconColor = blitzIconColor,
                rapidIconColor = rapidIconColor,
                classicalIconColor = classicalIconColor,
                onCustomIconClick = {
                    context.playSound()
                    animateModeColor = true
                    clockMode = ClockMode.Custom
                },
                onBulletIconClick = {
                    context.playSound()
                    animateModeColor = true
                    clockMode = ClockMode.Bullet
                },
                onBlitzClick = {
                    context.playSound()
                    animateModeColor = true
                    clockMode = ClockMode.Blitz
                },
                onRapidIconClick = {
                    context.playSound()
                    animateModeColor = true
                    clockMode = ClockMode.Rapid
                },
                onClassicalIconClick = {
                    context.playSound()
                    animateModeColor = true
                    clockMode = ClockMode.Classical
                }
            )
            Spacer(modifier = Modifier.size(space))
            SubTitle(title = "Increment: ", text = "$increment\"", color = animatedIncrementColor)
            Spacer(modifier = Modifier.size(miniSpace))
            IncrementSlider(
                value = increment
            ) {
                context.playSound()
                animateIncrementColor = true
                increment = it
            }
        }
        ChessClockPicker(
            value = pickerValue,
            textColor = animatedTimeColor,
            onValueChange = {
                context.playSound()
                pickerValue = it
                clockMode = ClockMode.Custom
            }
        )
        BasicButton(
            modifier = Modifier
                .fillMaxWidth()
                .height(85.dp),
            text = "Select",
            style = MaterialTheme.typography.button.copy(
                color = MaterialTheme.colors.background,
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold
            ),
            shape = MaterialTheme.shapes.small.copy(CornerSize(0.dp)),
        ) {
            onTimeSelected(clockMode, pickerValue.copy(increment = increment))
        }
    }
}

@Composable
private fun IconsRaw(
    iconSize: Dp = 58.dp,
    iconPadding: Dp = 14.dp,
    customIconColor: androidx.compose.ui.graphics.Color,
    bulletIconColor: androidx.compose.ui.graphics.Color,
    blitzIconColor: androidx.compose.ui.graphics.Color,
    rapidIconColor: androidx.compose.ui.graphics.Color,
    classicalIconColor: androidx.compose.ui.graphics.Color,
    onCustomIconClick: () -> Unit,
    onBulletIconClick: () -> Unit,
    onBlitzClick: () -> Unit,
    onRapidIconClick: () -> Unit,
    onClassicalIconClick: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        OutlineIcon(
            imageID = R.drawable.ic_custom,
            size = iconSize,
            padding = iconPadding,
            borderColor = customIconColor,
            onClick = onCustomIconClick,
            stroke = 1.dp
        )
        OutlineIcon(
            imageID = R.drawable.ic_bullet,
            size = iconSize,
            padding = iconPadding,
            borderColor = bulletIconColor,
            onClick = onBulletIconClick,
            stroke = 1.dp
        )
        OutlineIcon(
            imageID = R.drawable.ic_blitz,
            size = iconSize,
            padding = iconPadding,
            borderColor = blitzIconColor,
            onClick = onBlitzClick,
            stroke = 1.dp
        )
        OutlineIcon(
            imageID = R.drawable.ic_rapid,
            size = iconSize,
            padding = iconPadding,
            borderColor = rapidIconColor,
            onClick = onRapidIconClick,
            stroke = 1.dp
        )
        OutlineIcon(
            imageID = R.drawable.ic_classical,
            size = iconSize,
            padding = iconPadding,
            borderColor = classicalIconColor,
            onClick = onClassicalIconClick,
            stroke = 1.dp
        )
    }
}

@Composable
private fun Header() {
    Text(
        text = "King's Clock",
        style = MaterialTheme.typography.h2.copy(
            color = MaterialTheme.colors.onBackground
        )
    )
    Text(
        text = "How many minutes do you want to play with?",
        style = MaterialTheme.typography.body1.copy(color = TextColor)
    )
}

@Composable
private fun SubTitle(
    modifier: Modifier = Modifier,
    title: String,
    text: String,
    color: androidx.compose.ui.graphics.Color
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.h5.copy(
                color = MaterialTheme.colors.onSecondary,
                fontWeight = FontWeight.Light
            )
        )
        Text(
            text = text,
            style = MaterialTheme.typography.h5.copy(
                color = color,
                fontWeight = FontWeight.Light
            )
        )
    }
}

@Composable
fun IncrementSlider(
    modifier: Modifier = Modifier,
    value: Int,
    range: ClosedFloatingPointRange<Float> = 0f..100f,
    onValueChange: (Int) -> Unit,
) {
    Slider(
        modifier = modifier,
        value = value.toFloat(),
        valueRange = range,
        onValueChange = {
            onValueChange(it.toInt())
        }
    )
}
