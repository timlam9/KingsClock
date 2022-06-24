package com.lamti.kingsclock.ui.composables.picker

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lamti.kingsclock.ui.uistate.ChessClock
import kotlin.math.abs

@Composable
fun ChessClockPicker(
    modifier: Modifier = Modifier,
    value: ChessClock = ChessClock(0, 0, 0),
    leadingZero: Boolean = true,
    minutesRange: Iterable<Int> = 0..59,
    secondsRange: Iterable<Int> = 0..59,
    onValueChange: (ChessClock) -> Unit,
    dividersColor: Color = MaterialTheme.colors.primary,
    textColor: Color = MaterialTheme.colors.onSecondary,
    textStyle: TextStyle = LocalTextStyle.current.copy(color = textColor),
) {
    Row(
        modifier = modifier.padding(20.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        ListItemPicker(
            modifier = Modifier.weight(1f),
            label = { "${if (leadingZero && abs(it) < 10) "0" else ""}$it" },
            value = value.minutes,
            onValueChange = { onValueChange(value.copy(minutes = it)) },
            dividersColor = dividersColor,
            textStyle = textStyle.copy(fontSize = 35.sp, fontWeight = FontWeight.Bold),
            list = minutesRange.toList()
        )
        Text(
            modifier = Modifier.size(24.dp),
            textAlign = TextAlign.Center,
            text = ":"
        )
        ListItemPicker(
            modifier = Modifier.weight(1f),
            label = { "${if (leadingZero && abs(it) < 10) "0" else ""}$it" },
            value = value.seconds,
            onValueChange = { onValueChange(value.copy(seconds = it)) },
            dividersColor = dividersColor,
            textStyle = textStyle.copy(fontSize = 35.sp, fontWeight = FontWeight.Bold),
            list = secondsRange.toList()
        )
    }
}
