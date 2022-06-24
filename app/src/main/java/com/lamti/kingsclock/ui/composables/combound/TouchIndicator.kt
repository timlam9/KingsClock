package com.lamti.kingsclock.ui.composables.combound

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lamti.kingsclock.R

@Composable
fun TouchIndicator(showTouchIndicator: Boolean, touchIndicatorRotateValue: Float) {
    AnimatedVisibility(visible = showTouchIndicator) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                modifier = Modifier
                    .padding(end = 12.dp)
                    .size(32.dp)
                    .rotate(touchIndicatorRotateValue),
                painter = painterResource(id = R.drawable.ic_touch),
                contentDescription = "Rounded Image Button",
                tint = MaterialTheme.colors.onSecondary
            )
            Text(
                modifier = Modifier.rotate(-touchIndicatorRotateValue),
                text = "touch anywhere",
                style = MaterialTheme.typography.h6.copy(
                    color = MaterialTheme.colors.onSecondary,
                    fontWeight = FontWeight.ExtraBold,
                    fontFamily = FontFamily.Monospace,
                    letterSpacing = 4.sp,
                    textAlign = TextAlign.Center,
                )
            )
        }
    }
}
