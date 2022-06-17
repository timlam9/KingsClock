package com.lamti.kingsclock.ui.uistate

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlin.time.Duration

fun tickerFlow(period: Duration, initialDelay: Duration = Duration.ZERO, play: Boolean = false) = flow {
    delay(initialDelay)
    while (play) {
        emit(Unit)
        delay(period)
    }
}
