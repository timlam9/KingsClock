package com.lamti.kingsclock.ui.uistate

data class ChessClock(
    val minutes: Int,
    val seconds: Int,
    val increment: Int,
) {

    fun toMillis(): Long = (minutes * 60 * 1000L) + (seconds * 1000L)
}
