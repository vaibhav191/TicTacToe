package com.example.tictactoe.utilities.randomselector

import com.example.tictactoe.utilities.enums.MovesEnum

class SelectRandom {
    companion object {
        fun select(availableMoves: List<MovesEnum>): MovesEnum {
            if (availableMoves.isNotEmpty()) {
                return availableMoves.random()
            }
            throw Exception("No available moves")
        }
    }
}