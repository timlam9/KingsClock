package com.lamti.kingsclock.ui.uistate

sealed class UIEvent {

    data class ClockSelected(val clock: ChessClock) : UIEvent()

    data class ClockStateChanged(val clockState: ClockState) : UIEvent()

    object Initialize : UIEvent()

    object SettingsClicked : UIEvent()
}
