package com.lamti.kingsclock.ui.uistate

enum class ChessMode(val chessClock: ChessClock) {

    Bullet(ChessClock(1, 0)),
    Blitz(ChessClock(3, 0)),
    Rapid(ChessClock(10, 0)),
    Classical(ChessClock(15, 0)),
    Custom(ChessClock(5, 0)),
}
