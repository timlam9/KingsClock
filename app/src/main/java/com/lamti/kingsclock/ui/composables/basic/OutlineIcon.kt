package com.lamti.kingsclock.ui.composables.basic

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.lamti.kingsclock.ui.theme.Red

@Composable
fun OutlineIcon(
    modifier: Modifier = Modifier,
    imageID: Int,
    size: Dp = 70.dp,
    borderColor: Color = Red,
    color: Color = borderColor,
    backgroundColor: Color = Color.Transparent,
    stroke: Dp = 4.dp,
    padding: Dp = 0.dp,
    enabled: Boolean = true,
    onClick: () -> Unit = {}
) {
    IconButton(
        modifier = modifier
            .size(size)
            .clip(RoundedCornerShape(60))
            .background(backgroundColor)
            .border(
                width = stroke,
                color = borderColor,
                shape = RoundedCornerShape(60)
            ),
        onClick = onClick,
        enabled = enabled
    ) {
        Icon(
            modifier = Modifier.padding(padding),
            painter = painterResource(id = imageID),
            contentDescription = "Rounded Image Button",
            tint = color
        )
    }
}

