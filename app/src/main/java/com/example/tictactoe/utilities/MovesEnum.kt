package com.example.tictactoe.utilities

enum class MovesEnum {
    TOP_LEFT {
        val row = 0
        val column = 0
        val index = 0
    },
    TOP_CENTER {
        val row = 0
        val column = 1
        val index = 1
    },
    TOP_RIGHT {
        val row = 0
        val column = 1
        val index = 2
    },
    MIDDLE_LEFT {
        val row = 1
        val column = 0
        val index = 3
    },
    MIDDLE_CENTER {
        val row = 1
        val column = 1
        val index = 4
    },
    MIDDLE_RIGHT {
        val row = 1
        val column = 2
        val index = 5
    },
    BOTTOM_LEFT {
        val row = 2
        val column = 0
        val index = 6
    },
    BOTTOM_CENTER {
        val row = 2
        val column = 1
        val index = 7
    },
    BOTTOM_RIGHT {
        val row = 2
        val column = 2
        val index = 8
    }
}