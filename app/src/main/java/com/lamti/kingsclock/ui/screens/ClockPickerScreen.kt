package com.lamti.kingsclock.ui.screens

import android.content.Context
import android.media.MediaPlayer
import android.util.Log
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
import com.lamti.kingsclock.ui.theme.Blue
import com.lamti.kingsclock.ui.theme.TextColor
import com.lamti.kingsclock.ui.uistate.ChessClock
import com.lamti.kingsclock.ui.uistate.ChessMode

@Composable
fun ClockPickerScreen(
    modifier: Modifier = Modifier,
    iconSize: Dp = 58.dp,
    iconPadding: Dp = 14.dp,
    miniSpace: Dp = 10.dp,
    space: Dp = 20.dp,
    onTimeSelected: (ChessClock) -> Unit,
) {
    val context = LocalContext.current
    var increment: Int by remember { mutableStateOf(0) }
    var chessMode: ChessMode by remember { mutableStateOf(ChessMode.Custom) }
    var pickerValue: ChessClock by remember { mutableStateOf(chessMode.clock) }
    val transition = updateTransition(targetState = chessMode, label = "animation")

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

    LaunchedEffect(chessMode) { if (chessMode != ChessMode.Custom) pickerValue = chessMode.clock }

    LaunchedEffect(true) {
        context.playSound()
    }

    val bulletIconColor by transition.animateColor(label = "color") { mode ->
        when (mode) {
            ChessMode.Bullet -> MaterialTheme.colors.primary
            else -> MaterialTheme.colors.onSecondary
        }
    }
    val blitzIconColor by transition.animateColor(label = "color") { mode ->
        when (mode) {
            ChessMode.Blitz -> MaterialTheme.colors.primary
            else -> MaterialTheme.colors.onSecondary
        }
    }
    val rapidIconColor by transition.animateColor(label = "color") { mode ->
        when (mode) {
            ChessMode.Rapid -> MaterialTheme.colors.primary
            else -> MaterialTheme.colors.onSecondary
        }
    }
    val classicalIconColor by transition.animateColor(label = "color") { mode ->
        when (mode) {
            ChessMode.Classical -> MaterialTheme.colors.primary
            else -> MaterialTheme.colors.onSecondary
        }
    }
    val customIconColor by transition.animateColor(label = "color") { mode ->
        when (mode) {
            ChessMode.Custom -> MaterialTheme.colors.primary
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
            Text(
                text = "King's Clock",
                style = MaterialTheme.typography.h2.copy(
                    color = MaterialTheme.colors.onBackground
                )
            )
            Text(
                text = "How many minutes (and seconds) do you want to play with?",
                style = MaterialTheme.typography.body1.copy(color = TextColor)
            )
            Spacer(modifier = Modifier.size(space))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Mode: ",
                    style = MaterialTheme.typography.h4.copy(
                        color = MaterialTheme.colors.onSecondary,
                        fontWeight = FontWeight.SemiBold
                    )
                )
                Text(
                    text = chessMode.name,
                    style = MaterialTheme.typography.h4.copy(
                        color = animatedColor,
                        fontWeight = FontWeight.Normal
                    )
                )
            }
            Spacer(modifier = Modifier.size(miniSpace))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                OutlineIcon(
                    imageID = R.drawable.ic_custom,
                    size = iconSize,
                    padding = iconPadding,
                    borderColor = customIconColor,
                    onClick = {
                        animateModeColor = true
                        context.playSound()
                        chessMode = ChessMode.Custom
                    }
                )
                OutlineIcon(
                    imageID = R.drawable.ic_bullet,
                    size = iconSize,
                    padding = iconPadding,
                    borderColor = bulletIconColor,
                    onClick = {
                        animateModeColor = true
                        context.playSound()
                        chessMode = ChessMode.Bullet
                    }
                )
                OutlineIcon(
                    imageID = R.drawable.ic_blitz,
                    size = iconSize,
                    padding = iconPadding,
                    borderColor = blitzIconColor,
                    onClick = {
                        animateModeColor = true
                        context.playSound()
                        chessMode = ChessMode.Blitz
                    }
                )
                OutlineIcon(
                    imageID = R.drawable.ic_rapid,
                    size = iconSize,
                    padding = iconPadding,
                    borderColor = rapidIconColor,
                    onClick = {
                        animateModeColor = true
                        context.playSound()
                        chessMode = ChessMode.Rapid
                    }
                )
                OutlineIcon(
                    imageID = R.drawable.ic_classical,
                    size = iconSize,
                    padding = iconPadding,
                    borderColor = classicalIconColor,
                    onClick = {
                        animateModeColor = true
                        context.playSound()
                        chessMode = ChessMode.Classical
                    }
                )
            }
            Spacer(modifier = Modifier.size(space))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Increment: ",
                    style = MaterialTheme.typography.h4.copy(
                        color = MaterialTheme.colors.onSecondary,
                        fontWeight = FontWeight.SemiBold
                    )
                )
                Text(
                    text = "$increment\"",
                    style = MaterialTheme.typography.h4.copy(
                        color = animatedIncrementColor,
                        fontWeight = FontWeight.Normal
                    )
                )
            }
            Spacer(modifier = Modifier.size(miniSpace))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                OutlineIcon(
                    imageID = R.drawable.ic_arrow_up,
                    size = iconSize,
                    padding = iconPadding,
                    borderColor = MaterialTheme.colors.onSecondary,
                    onClick = {
                        context.playSound()
                        animateIncrementColor = true
                        increment = when (increment) {
                            0 -> 15
                            15 -> 30
                            30 -> 60
                            else -> 0
                        }
                    }
                )
            }
        }
        ChessClockPicker(
            value = pickerValue,
            textColor = animatedTimeColor,
            onValueChange = {
                context.playSound()
                pickerValue = it
                chessMode = ChessMode.Custom
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
            onTimeSelected(pickerValue)
        }
    }
}

fun Context.playSound(sound: Int = R.raw.click) {
    try {
        MediaPlayer.create(this, sound).start()
    } catch (e: Exception) {
        Log.e("TAGARA", "Ex: ${e.message}")
    }
}
