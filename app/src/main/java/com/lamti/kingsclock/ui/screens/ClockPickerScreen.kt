package com.lamti.kingsclock.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lamti.kingsclock.ui.composables.basic.BasicButton
import com.lamti.kingsclock.ui.composables.picker.ChessClockPicker
import com.lamti.kingsclock.ui.theme.TextColor
import com.lamti.kingsclock.ui.uistate.ChessClock

@Composable
fun ClockPickerScreen(
    modifier: Modifier = Modifier,
    chessClock: ChessClock,
    onTimeSelected: (ChessClock) -> Unit,
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.Start
    ) {
        var pickerValue: ChessClock by remember { mutableStateOf(chessClock) }

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
        }
        ChessClockPicker(
            value = pickerValue,
            onValueChange = {
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
