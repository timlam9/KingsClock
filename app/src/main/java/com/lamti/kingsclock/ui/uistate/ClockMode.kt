package com.lamti.kingsclock.ui.uistate

import com.lamti.kingsclock.ui.uistate.UIState.Companion.INITIAL_INCREMENT
import com.lamti.kingsclock.ui.uistate.UIState.Companion.INITIAL_MINUTES
import com.lamti.kingsclock.ui.uistate.UIState.Companion.INITIAL_SECONDS

private val BulletClock = ChessClock(1, 0, INITIAL_INCREMENT)
private val BlitzClock = ChessClock(3, 0, INITIAL_INCREMENT)
private val RapidClock = ChessClock(10, 0, INITIAL_INCREMENT)
private val ClassicalClock = ChessClock(15, 0, INITIAL_INCREMENT)
private val CustomClock = ChessClock(INITIAL_MINUTES, INITIAL_SECONDS, INITIAL_INCREMENT)

enum class ClockMode(val clock: ChessClock) {
    Bullet(BulletClock),
    Blitz(BlitzClock),
    Rapid(RapidClock),
    Classical(ClassicalClock),
    Custom(CustomClock)
}
