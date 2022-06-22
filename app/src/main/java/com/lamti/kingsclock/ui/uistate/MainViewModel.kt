package com.lamti.kingsclock.ui.uistate

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lamti.kingsclock.ui.screens.Screen.ClockScreen
import com.lamti.kingsclock.ui.screens.Screen.PickerScreen
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    var uiState: UIState by mutableStateOf(
        UIState(
            isLoading = true,
            screen = ClockScreen,
            clock = ChessClock(minutes = 0, seconds = 0)
        )
    )
        private set

    init {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = false)
        }
    }

    fun onEvent(event: UIEvent) {
        uiState = when (event) {
            is UIEvent.ClockSelected -> {
                uiState.copy(
                    clock = event.clock,
                    screen = ClockScreen
                )
            }
            UIEvent.SettingsClicked -> {
                uiState.copy(screen = PickerScreen)
            }
        }
    }
}
