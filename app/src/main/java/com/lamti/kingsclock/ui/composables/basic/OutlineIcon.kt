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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.lamti.kingsclock.ui.theme.Red

@Composable
fun OutlineIcon(
    modifier: Modifier = Modifier,
    imageID: Int? = null,
    icon: ImageVector = Icons.Default.Close,
    size: Dp = 70.dp,
    borderColor: Color = Red,
    color: Color = borderColor,
    stroke: Dp = 6.dp,
    onClick: () -> Unit = {}
) {
    IconButton(
        modifier = modifier
            .size(size)
            .border(
                width = stroke,
                color = borderColor,
                shape = RoundedCornerShape(60)
            ),
        onClick = onClick
    ) {
        if (imageID == null) {
            Icon(
                imageVector = icon,
                contentDescription = "Rounded Image Button",
                tint = color
            )
        } else {
            Icon(
                painter = painterResource(id = imageID),
                contentDescription = "Rounded Image Button",
                tint = color
            )
        }
    }
}

