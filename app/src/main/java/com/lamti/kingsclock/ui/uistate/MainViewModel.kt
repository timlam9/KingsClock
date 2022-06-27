package com.lamti.kingsclock.ui.uistate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lamti.kingsclock.PreferencesManager
import com.lamti.kingsclock.StoredClock
import com.lamti.kingsclock.ui.screens.Screen.ClockScreen
import com.lamti.kingsclock.ui.screens.Screen.PickerScreen
import com.lamti.kingsclock.ui.uistate.UIState.Companion.RESUME
import com.lamti.kingsclock.ui.uistate.UIState.Companion.START
import com.lamti.kingsclock.ui.uistate.UIState.Companion.initialState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val preferencesManager: PreferencesManager
) : ViewModel() {

    private val _uiState: MutableStateFlow<UIState> = MutableStateFlow(initialState)
    val uiState: StateFlow<UIState> = preferencesManager.storedClock.flatMapLatest { storedClock ->
        val clock = ChessClock(
            minutes = storedClock.minutes,
            seconds = storedClock.seconds,
            increment = storedClock.increment
        )
        _uiState.update {
            it.copy(
                clock = clock,
                clockMode = storedClock.mode ?: ClockMode.Custom,
                whitesTimer = Timer(clock.toMillis()),
                blacksTimer = Timer(clock.toMillis())
            )
        }
        _uiState
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), initialState)

    private val _eventFlow = MutableSharedFlow<UIEvent>(extraBufferCapacity = 16)

    init {
        _eventFlow.process().launchIn(viewModelScope)

        _uiState.flatMapLatest {
            it.whitesTimer.isTimerFinished.combine(it.blacksTimer.isTimerFinished) { white, black ->
                if (white) onWhitesTimerFinished()
                if (black) onBlacksTimerFinished()
            }
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
        _uiState.update {
            _uiState.value.copy(
                clock = clock,
                clockMode = mode,
                screen = ClockScreen,
                whitesTimer = Timer(clock.toMillis()),
                blacksTimer = Timer(clock.toMillis())
            )
        }

        viewModelScope.launch {
            with(_uiState.value) {
                preferencesManager.saveClock(
                    StoredClock(
                        minutes = clock.minutes,
                        seconds = clock.seconds,
                        increment = clock.increment,
                        mode = clockMode
                    )
                )
            }
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
