package com.example.tictactoe.utilities.enums

enum class MovesEnum(val row: Int, val column: Int, val moveIndex: Int) {
    TOP_LEFT(0, 0, 0),
    TOP_CENTER(0, 1, 1),
    TOP_RIGHT(0, 2, 2),
    MIDDLE_LEFT(1, 0, 3),
    MIDDLE_CENTER(1, 1, 4),
    MIDDLE_RIGHT(1, 2, 5),
    BOTTOM_LEFT(2, 0, 6),
    BOTTOM_CENTER(2, 1, 7),
    BOTTOM_RIGHT(2, 2, 8)
}