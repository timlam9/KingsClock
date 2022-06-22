package com.lamti.kingsclock.ui.uistate

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lamti.kingsclock.ui.screens.Screen
import com.lamti.kingsclock.ui.screens.Screen.ClockScreen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    var currentScreen: Screen by mutableStateOf(ClockScreen)
        private set

    var chessClock: ChessClock by mutableStateOf(ChessClock(1, 0))
        private set

    init {
        viewModelScope.launch {
            _isLoading.value = false
        }
    }

    fun onEvent(event: UIEvent) {
        when (event) {
            is UIEvent.ClockSelected -> {
                chessClock = event.clock
                currentScreen = ClockScreen
            }
            UIEvent.SettingsClicked -> {
                currentScreen = Screen.PickerScreen
            }
        }
    }
}
