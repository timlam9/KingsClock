package com.lamti.kingsclock.ui.uistate

import com.lamti.kingsclock.ui.screens.Screen

data class UIState(
    val isLoading: Boolean,
    val screen: Screen,
    val clock: ChessClock
)
