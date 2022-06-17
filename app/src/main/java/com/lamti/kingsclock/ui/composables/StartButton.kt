package com.lamti.kingsclock.ui.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp
import com.lamti.kingsclock.ui.noRippleClickable

@Composable
fun RoundedTextButton(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colors.primary,
    text: String,
    buttonSize: Dp,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .size(buttonSize)
            .drawBehind {
                drawCircle(
                    color = color,
                    center = Offset(x = size.width / 2, y = size.height / 2),
                    radius = buttonSize.toPx() / 2
                )
            }
            .noRippleClickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.h2.copy(
                color = MaterialTheme.colors.background,
                fontWeight = FontWeight.ExtraBold,
                fontFamily = FontFamily.Monospace,
                letterSpacing = 6.sp,
                textAlign = TextAlign.Center,
            )
        )
    }
}
