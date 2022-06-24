package com.lamti.kingsclock.ui.uistate

import com.lamti.kingsclock.ui.screens.Screen

data class UIState(
    val clockState: ClockState,
    val clock: ChessClock,
    val turn: Turn,
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

        const val START = "start"
        const val RESUME = "resume"

        val initialState = UIState(
            isLoading = false,
            screen = Screen.ClockScreen,
            clockState = ClockState.Idle,
            turn = Turn.Whites,
            clock = ChessClock(minutes = 0, seconds = 10),
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

