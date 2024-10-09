package com.example.tictactoe.utilities.selector

import android.util.Log
import com.example.tictactoe.utilities.enums.MovesEnum

class SelectRandom {
    companion object {
        fun select(availableMoves: List<MovesEnum>): MovesEnum {
            Log.d("SelectRandom", "available moves: $availableMoves")
            if (availableMoves.isNotEmpty()) {
                val random = availableMoves.random()
                Log.d("SelectRandom", "random move: $random")
                return random
            }
            throw Exception("No available moves")
        }
    }
}