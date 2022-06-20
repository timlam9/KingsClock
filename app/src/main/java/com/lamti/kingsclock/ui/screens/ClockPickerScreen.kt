package com.lamti.kingsclock.ui.screens

import androidx.compose.animation.animateColor
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lamti.kingsclock.R
import com.lamti.kingsclock.ui.composables.basic.BasicButton
import com.lamti.kingsclock.ui.composables.basic.OutlineIcon
import com.lamti.kingsclock.ui.composables.basic.OutlinedButton
import com.lamti.kingsclock.ui.composables.picker.ChessClockPicker
import com.lamti.kingsclock.ui.theme.TextColor
import com.lamti.kingsclock.ui.uistate.ChessClock
import com.lamti.kingsclock.ui.uistate.ChessMode

@Composable
fun ClockPickerScreen(
    modifier: Modifier = Modifier,
    onTimeSelected: (ChessClock) -> Unit,
) {
    val screenWidth: Dp = LocalConfiguration.current.screenWidthDp.dp
    var chessMode by rememberSaveable { mutableStateOf(ChessMode.Bullet) }
    var pickerValue: ChessClock by remember { mutableStateOf(chessMode.chessClock) }
    val transition = updateTransition(targetState = chessMode, label = "animation")

    LaunchedEffect(chessMode) {
        pickerValue = chessMode.chessClock
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
                .padding(20.dp)
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
            Spacer(modifier = Modifier.size(32.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                OutlineIcon(
                    imageID = R.drawable.ic_bullet,
                    size = 70.dp,
                    padding = 20.dp,
                    borderColor = bulletIconColor,
                    onClick = { chessMode = ChessMode.Bullet }
                )
                OutlineIcon(
                    imageID = R.drawable.ic_blitz,
                    size = 70.dp,
                    padding = 20.dp,
                    borderColor = blitzIconColor,
                    onClick = { chessMode = ChessMode.Blitz }
                )
                OutlineIcon(
                    imageID = R.drawable.ic_rapid,
                    size = 70.dp,
                    padding = 20.dp,
                    borderColor = rapidIconColor,
                    onClick = { chessMode = ChessMode.Rapid }
                )
                OutlineIcon(
                    imageID = R.drawable.ic_classical,
                    size = 70.dp,
                    padding = 20.dp,
                    borderColor = classicalIconColor,
                    onClick = { chessMode = ChessMode.Classical }
                )
            }
            Spacer(modifier = Modifier.size(20.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                OutlineIcon(
                    imageID = R.drawable.ic_custom,
                    size = 70.dp,
                    padding = 20.dp,
                    borderColor = customIconColor,
                    onClick = { chessMode = ChessMode.Custom }
                )
                OutlineIcon(
                    imageID = R.drawable.ic_arrow_up,
                    size = 70.dp,
                    padding = 20.dp,
                    borderColor = MaterialTheme.colors.onSecondary,
                    onClick = { }
                )
                OutlinedButton(
                    width = screenWidth / 2f - 20.dp,
                    text = chessMode.name,
                    fontSize = 32.sp,
                    borderColor = MaterialTheme.colors.onSecondary,
                    onclick = {}
                )
            }
        }
        ChessClockPicker(
            value = pickerValue,
            onValueChange = {
                chessMode = ChessMode.Custom
                pickerValue = it
            }
        )
        BasicButton(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp),
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
