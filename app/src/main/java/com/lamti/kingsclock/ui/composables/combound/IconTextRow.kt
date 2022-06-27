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
    modifier: Modifier = Modifier,
    padding: Dp = 20.dp,
    offset: Dp = 0.dp,
    text: String,
    icon: Int = R.drawable.ic_pawns,
    iconPadding: Dp = 18.dp,
    color: Color,
    textBackgroundColor: Color = Color.Transparent,
    borderColor: Color,
    iconBorderColor: Color = borderColor,
    textColor: Color = borderColor,
    iconBackgroundColor: Color = Color.Transparent,
    onIconClicked: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .offset(x = offset)
            .padding(horizontal = padding)
    ) {
        OutlineIcon(
            modifier = Modifier,
            imageID = icon,
            size = 70.dp,
            color = color,
            padding = iconPadding,
            borderColor = iconBorderColor,
            backgroundColor = iconBackgroundColor,
            enabled = false,
            onClick = onIconClicked
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

