package com.lamti.kingsclock.ui.composables.combound

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lamti.kingsclock.R
import com.lamti.kingsclock.ui.composables.basic.OutlineIcon
import com.lamti.kingsclock.ui.composables.basic.OutlinedButton

@Composable
fun IconTextRow(
    offset: Dp,
    text: String,
    color: Color,
    textBackgroundColor: Color,
    borderColor: Color,
    textColor: Color = borderColor,
    iconBackgroundColor: Color = Color.Transparent,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .offset(x = offset)
            .padding(horizontal = 20.dp)
    ) {
        OutlineIcon(
            modifier = Modifier,
            imageID = R.drawable.ic_rocket_launch,
            size = 70.dp,
            color = color,
            borderColor = borderColor,
            backgroundColor = iconBackgroundColor
        )
        Spacer(modifier = Modifier.size(20.dp))
        OutlinedButton(
            modifier = Modifier.weight(1f),
            text = text,
            fontSize = 32.sp,
            borderColor = borderColor,
            backgroundColor = textBackgroundColor,
            textColor = textColor,
            onclick = {}
        )
    }
}

