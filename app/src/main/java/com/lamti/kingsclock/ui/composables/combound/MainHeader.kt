package com.lamti.kingsclock.ui.composables.combound

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun MainHeader(
    screenWidth: Dp,
    blacksTimerTranslationX: Dp,
    whitesTimerTranslationX: Dp,
    blacksTimerText: String,
    whitesTimerText: String,
) {
    Column(modifier = Modifier.offset(y = -screenWidth / 1.4f)) {
        IconTextRow(
            offset = blacksTimerTranslationX,
            text = blacksTimerText,
            color = MaterialTheme.colors.onSecondary,
            textBackgroundColor = MaterialTheme.colors.onSecondary,
            borderColor = MaterialTheme.colors.onSecondary,
            iconBackgroundColor = MaterialTheme.colors.background,
            textColor = MaterialTheme.colors.background,
        )
        Spacer(modifier = Modifier.size(20.dp))
        IconTextRow(
            offset = whitesTimerTranslationX,
            text = whitesTimerText,
            color = MaterialTheme.colors.background,
            textBackgroundColor = MaterialTheme.colors.background,
            borderColor = MaterialTheme.colors.onSecondary,
            iconBackgroundColor = MaterialTheme.colors.onSecondary,
            textColor = MaterialTheme.colors.onSecondary,
        )
    }
}
