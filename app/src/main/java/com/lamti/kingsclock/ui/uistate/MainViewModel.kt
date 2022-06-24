package com.lamti.kingsclock.ui.uistate

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lamti.kingsclock.ui.screens.Screen.ClockScreen
import com.lamti.kingsclock.ui.screens.Screen.PickerScreen
import com.lamti.kingsclock.ui.uistate.UIState.Companion.RESUME
import com.lamti.kingsclock.ui.uistate.UIState.Companion.START
import com.lamti.kingsclock.ui.uistate.UIState.Companion.initialState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class MainViewModel : ViewModel() {

    var uiState: UIState by mutableStateOf(initialState)
        private set

    private val _eventFlow = MutableSharedFlow<UIEvent>(extraBufferCapacity = 16)

    init {
        _eventFlow.process().launchIn(viewModelScope)
    }

    fun sendEvent(event: UIEvent) = _eventFlow.tryEmit(event)

    private fun Flow<UIEvent>.process() = onEach {
        when (it) {
            is UIEvent.ClockSelected -> onClockSelected(it.clock)
            UIEvent.Initialize -> onInitialize()
            UIEvent.BackgroundClicked -> onBackgroundClicked()
            UIEvent.StartButtonClicked -> onStartButtonClicked()
            UIEvent.PauseButtonClicked -> onPauseButtonClicked()
            UIEvent.BlacksTimerFinished -> onBlacksTimerFinished()
            UIEvent.WhitesTimerFinished -> onWhitesTimerFinished()
            UIEvent.RestartButtonClicked -> onRestartButtonClicked()
            UIEvent.CloseButtonClicked -> onCloseButtonClicked()
            UIEvent.SettingsClicked -> onSettingsButtonClicked()
        }
    }

    private fun onStartButtonClicked() {
        onClockStateChanged(ClockState.Started)
    }

    private fun onPauseButtonClicked() {
        onClockStateChanged(ClockState.Paused)
    }

    private fun onBlacksTimerFinished() {
        onClockStateChanged(ClockState.Finished)
    }

    private fun onWhitesTimerFinished() {
        onClockStateChanged(ClockState.Finished)
    }

    private fun onInitialize() {
        uiState = uiState.copy(showPauseWidgets = true)
    }

    private fun onBackgroundClicked() {
        uiState = uiState.copy(
            showTouchIndicator = if (uiState.rotateTouchIndicator) false else uiState.showTouchIndicator,
            rotateTouchIndicator = true,
            turn = uiState.turn.changeClock()
        )
    }

    private fun onRestartButtonClicked() {
        uiState = uiState.copy(
            turn = Turn.Whites,
            clockState = ClockState.Started
        )
        onClockStateChanged(uiState.clockState)
    }

    private fun onCloseButtonClicked() {
        uiState = uiState.copy(
            turn = Turn.Whites,
            clockState = ClockState.Idle
        )
        onClockStateChanged(uiState.clockState)
    }

    private fun onClockSelected(clock: ChessClock) {
        uiState = uiState.copy(
            clock = clock,
            screen = ClockScreen
        )
    }

    private fun onClockStateChanged(clockState: ClockState) {
        uiState = when (clockState) {
            ClockState.Idle -> {
                uiState.copy(
                    startButtonText = START,
                    showPauseWidgets = true,
                    showBlacksClock = false,
                    showWhitesClock = false,
                    showRestartButton = false,
                    showCloseButton = false,
                    showMenuCloseIcon = false,
                    showTouchIndicator = false,
                    rotateTouchIndicator = false,
                )
            }
            ClockState.Started -> {
                uiState.copy(
                    showTouchIndicator =
                    if (!uiState.rotateTouchIndicator) true else uiState.showTouchIndicator,
                    showPauseWidgets = false,
                    showBlacksClock = true,
                    showWhitesClock = true,
                    showRestartButton = false,
                    showCloseButton = false,
                    showMenuCloseIcon = false,
                )
            }
            ClockState.Paused -> {
                uiState.copy(
                    startButtonText = RESUME,
                    showPauseWidgets = true,
                    showBlacksClock = false,
                    showWhitesClock = false,
                    showRestartButton = false,
                    showCloseButton = false,
                    showMenuCloseIcon = true,
                    showTouchIndicator = false,
                )
            }
            ClockState.Finished -> {
                uiState.copy(
                    showPauseWidgets = false,
                    showBlacksClock = true,
                    showWhitesClock = true,
                    showRestartButton = true,
                    showCloseButton = true,
                    showMenuCloseIcon = false,
                    showTouchIndicator = false,
                )
            }
        }.run {
            copy(clockState = clockState)
        }
    }

    private fun onSettingsButtonClicked() {
        uiState = uiState.copy(screen = PickerScreen)
    }
}
