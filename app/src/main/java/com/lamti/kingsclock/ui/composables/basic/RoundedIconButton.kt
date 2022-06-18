package com.lamti.kingsclock.ui.composables.basic

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.lamti.kingsclock.ui.theme.Red

@Composable
fun RoundedIconButton(
    icon: ImageVector = Icons.Default.Close,
    size: Dp = 60.dp,
    color: Color = Red,
    onClick: () -> Unit
) {
    IconButton(
        modifier = Modifier
            .then(Modifier.size(size))
            .border(
                width = 2.dp,
                color = color,
                shape = RoundedCornerShape(60)
            ),
        onClick = onClick
    ) {
        Icon(
            imageVector = icon,
            contentDescription = "Rounded Image Button",
            tint = color
        )
    }
}

