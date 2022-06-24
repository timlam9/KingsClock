package com.lamti.kingsclock.ui.uistate

import com.lamti.kingsclock.ui.screens.Screen

data class UIState(
    val clockState: ClockState,
    val clock: ChessClock,
    val turn: Turn,
    val whitesTimer: Timer,
    val blacksTimer: Timer,
    val screen: Screen,
    val isLoading: Boolean,
    val showPauseWidgets: Boolean,
    val showMenuCloseIcon: Boolean,
    val showBlacksClock: Boolean,
    val showWhitesClock: Boolean,
    val showRestartButton: Boolean,
    val showCloseButton: Boolean,
    val showTouchIndicator: Boolean,
    val rotateTouchIndicator: Boolean,
    val startButtonText: String
) {

    companion object {

        private const val INITIAL_MINUTES = 0
        private const val INITIAL_SECONDS = 30
        private const val ONE_MILLI = 1000L
        private const val MAX_TIME_MILLIS = INITIAL_SECONDS * ONE_MILLI
        const val START = "start"
        const val RESUME = "resume"

        val initialState = UIState(
            isLoading = false,
            screen = Screen.ClockScreen,
            clockState = ClockState.Idle,
            turn = Turn.Whites,
            clock = ChessClock(minutes = INITIAL_MINUTES, seconds = INITIAL_SECONDS),
            blacksTimer = Timer(MAX_TIME_MILLIS),
            whitesTimer = Timer(MAX_TIME_MILLIS),
            showPauseWidgets = false,
            showMenuCloseIcon = false,
            showBlacksClock = false,
            showWhitesClock = false,
            showRestartButton = false,
            showCloseButton = false,
            showTouchIndicator = false,
            rotateTouchIndicator = false,
            startButtonText = START,
        )
    }
}

