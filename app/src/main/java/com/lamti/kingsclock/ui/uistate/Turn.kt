package com.lamti.kingsclock.ui.uistate

enum class Turn {
    Blacks,
    Whites;

    fun changeClock() = if (this == Blacks) Whites else Blacks
}
