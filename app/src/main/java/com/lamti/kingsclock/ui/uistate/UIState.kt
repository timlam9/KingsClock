package com.lamti.kingsclock.ui.uistate

import com.lamti.kingsclock.ui.screens.Screen

data class UIState(
    val clockState: ClockState,
    val clock: ChessClock,
    val turn: Turn,
    val screen: Screen,
    val isLoading: Boolean,
    val showPauseWidgets: Boolean
) {

    companion object {

        val initialState = UIState(
            isLoading = false,
            screen = Screen.ClockScreen,
            clockState = ClockState.Idle,
            turn = Turn.Whites,
            clock = ChessClock(minutes = 0, seconds = 0),
            showPauseWidgets = false
        )
    }
}
