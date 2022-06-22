package com.lamti.kingsclock.ui.uistate

sealed class UIEvent {

    data class ClockSelected(val clock: ChessClock) : UIEvent()
    object SettingsClicked : UIEvent()
}
