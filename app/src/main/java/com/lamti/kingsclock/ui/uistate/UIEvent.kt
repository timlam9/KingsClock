package com.lamti.kingsclock.ui.uistate

sealed class UIEvent {

    data class ClockModeSelected(val mode: ClockMode, val clock: ChessClock) : UIEvent()

    object Initialize : UIEvent()

    object BackgroundClicked : UIEvent()

    object StartButtonClicked : UIEvent()

    object PauseButtonClicked : UIEvent()

    object BlacksTimerFinished : UIEvent()

    object WhitesTimerFinished : UIEvent()

    object RestartButtonClicked : UIEvent()

    object CloseButtonClicked : UIEvent()

    object SettingsClicked : UIEvent()
}
