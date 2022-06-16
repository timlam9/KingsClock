package com.lamti.kingsclock.ui.uistate

enum class EnabledClock {
    Blacks,
    Whites;

    fun changeClock() = if (this == Blacks) Whites else Blacks
}
