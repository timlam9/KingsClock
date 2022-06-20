package com.lamti.kingsclock.ui.uistate

private val BulletClock = ChessClock(1, 0)
private val BlitzClock = ChessClock(3, 0)
private val RapidClock = ChessClock(10, 0)
private val ClassicalClock = ChessClock(15, 0)
private val CustomClock = ChessClock(5, 0)

enum class ChessMode(val clock: ChessClock) {
    Bullet(BulletClock),
    Blitz(BlitzClock),
    Rapid(RapidClock),
    Classical(ClassicalClock),
    Custom(CustomClock)
}
