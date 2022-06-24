package com.lamti.kingsclock.ui.uistate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lamti.kingsclock.ui.screens.Screen.ClockScreen
import com.lamti.kingsclock.ui.screens.Screen.PickerScreen
import com.lamti.kingsclock.ui.uistate.UIState.Companion.RESUME
import com.lamti.kingsclock.ui.uistate.UIState.Companion.START
import com.lamti.kingsclock.ui.uistate.UIState.Companion.initialState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

class MainViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(initialState)
    val uiState: StateFlow<UIState> = _uiState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UIEvent>(extraBufferCapacity = 16)

    init {
        _eventFlow.process().launchIn(viewModelScope)
        _uiState.onEach {
            it.whitesTimer.isTimerFinished.onEach { isFinished ->
                if (isFinished) onWhitesTimerFinished()
            }.launchIn(viewModelScope)

            it.blacksTimer.isTimerFinished.onEach { isFinished ->
                if (isFinished) onBlacksTimerFinished()
            }.launchIn(viewModelScope)
        }.launchIn(viewModelScope)
    }

    fun sendEvent(event: UIEvent) = _eventFlow.tryEmit(event)

    private fun Flow<UIEvent>.process() = onEach {
        when (it) {
            UIEvent.Initialize -> onInitialize()
            UIEvent.BackgroundClicked -> onBackgroundClicked()
            UIEvent.StartButtonClicked -> onStartButtonClicked()
            UIEvent.PauseButtonClicked -> onPauseButtonClicked()
            UIEvent.BlacksTimerFinished -> onBlacksTimerFinished()
            UIEvent.WhitesTimerFinished -> onWhitesTimerFinished()
            UIEvent.RestartButtonClicked -> onRestartButtonClicked()
            UIEvent.CloseButtonClicked -> onCloseButtonClicked()
            UIEvent.SettingsClicked -> onSettingsButtonClicked()
            is UIEvent.ClockModeSelected -> onClockModeSelected(it.mode, it.clock)
        }
    }

    private fun onClockModeSelected(mode: ClockMode, clock: ChessClock) {
        val maxTimeMillis = (clock.minutes * 60 * 1000L) + (clock.seconds * 1000L)

        _uiState.update {
            _uiState.value.copy(
                clock = clock,
                clockMode = mode,
                screen = ClockScreen,
                whitesTimer = Timer(maxTimeMillis),
                blacksTimer = Timer(maxTimeMillis)
            )
        }
    }

    private fun onInitialize() {
        _uiState.update {
            _uiState.value.copy(showPauseWidgets = true)
        }
    }

    private fun onStartButtonClicked() {
        onClockStateChanged(ClockState.Started)
    }

    private fun onPauseButtonClicked() {
        onClockStateChanged(ClockState.Paused)
    }

    private fun onRestartButtonClicked() {
        _uiState.value.blacksTimer.reset()
        _uiState.value.whitesTimer.reset()
        _uiState.update {
            uiState.value.copy(
                turn = Turn.Whites,
                clockState = ClockState.Started
            )
        }
        onClockStateChanged(_uiState.value.clockState)
    }

    private fun onBackgroundClicked() {
        _uiState.update {
            _uiState.value.copy(
                showTouchIndicator = if (_uiState.value.rotateTouchIndicator) false else _uiState.value.showTouchIndicator,
                rotateTouchIndicator = true,
                turn = _uiState.value.turn.changeClock(),
            )
        }
        startProperTimer()
        addIncrement()
    }

    private fun onCloseButtonClicked() {
        _uiState.value.blacksTimer.reset()
        _uiState.value.whitesTimer.reset()
        _uiState.update {
            _uiState.value.copy(
                turn = Turn.Whites,
                clockState = ClockState.Idle
            )
        }
        onClockStateChanged(_uiState.value.clockState)
    }

    private fun onSettingsButtonClicked() {
        _uiState.update { _uiState.value.copy(screen = PickerScreen) }
    }

    private fun onBlacksTimerFinished() {
        onClockStateChanged(ClockState.Finished)
    }

    private fun onWhitesTimerFinished() {
        onClockStateChanged(ClockState.Finished)
    }


    private fun onClockStateChanged(clockState: ClockState) {
        _uiState.value = when (clockState) {
            ClockState.Idle -> {
                _uiState.value.copy(
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
                startProperTimer()
                _uiState.value.copy(
                    showTouchIndicator =
                    if (!_uiState.value.rotateTouchIndicator) true else _uiState.value.showTouchIndicator,
                    showPauseWidgets = false,
                    showBlacksClock = true,
                    showWhitesClock = true,
                    showRestartButton = false,
                    showCloseButton = false,
                    showMenuCloseIcon = false,
                )
            }
            ClockState.Paused -> {
                _uiState.value.whitesTimer.pause()
                _uiState.value.blacksTimer.pause()

                _uiState.value.copy(
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
                _uiState.value.copy(
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

    private fun startProperTimer() {
        when (_uiState.value.turn) {
            Turn.Blacks -> {
                _uiState.value.whitesTimer.pause()
                _uiState.value.blacksTimer.start()
            }
            Turn.Whites -> {
                _uiState.value.whitesTimer.start()
                _uiState.value.blacksTimer.pause()
            }
        }
    }

    private fun addIncrement() {
        when (_uiState.value.turn) {
            Turn.Blacks -> _uiState.value.whitesTimer.addIncrement(_uiState.value.clock.increment)
            Turn.Whites -> _uiState.value.blacksTimer.addIncrement(_uiState.value.clock.increment)
        }
    }
}
