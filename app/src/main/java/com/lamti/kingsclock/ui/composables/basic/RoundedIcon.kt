package com.lamti.kingsclock.ui.composables.basic

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun RoundedIcon(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colors.surface,
    tint: Color = MaterialTheme.colors.primary,
    size: Dp = 52.dp,
    icon: Int,
    onClick: () -> Unit = { }
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(color)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(painter = painterResource(id = icon), contentDescription = null, tint = tint)
    }
}
