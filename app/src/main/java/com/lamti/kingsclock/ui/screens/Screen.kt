package com.lamti.kingsclock.ui.screens

sealed class Screen(route: String) {
    object ClockScreen : Screen("clock")
    object PickerScreen : Screen("picker")
}
