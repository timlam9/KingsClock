package com.lamti.kingsclock.ui.uistate

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lamti.kingsclock.ui.screens.Screen.ClockScreen
import com.lamti.kingsclock.ui.screens.Screen.PickerScreen
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
            is UIEvent.ClockStateChanged -> onClockStateChanged(it.clockState)
            UIEvent.Initialize -> onInitialize()
            UIEvent.SettingsClicked -> onSettingsButtonClicked()
        }
    }

    private fun onInitialize() {
        uiState = uiState.copy(showPauseWidgets = true)
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
                uiState.copy(showPauseWidgets = true)
            }
            ClockState.Started -> {
                uiState.copy(showPauseWidgets = false)
            }
            ClockState.Paused -> {
                uiState.copy(showPauseWidgets = true)
            }
            ClockState.Finished -> {
                uiState.copy(showPauseWidgets = false)
            }
        }.run {
            copy(clockState = clockState)
        }
    }

    private fun onSettingsButtonClicked() {
        uiState = uiState.copy(screen = PickerScreen)
    }
}
