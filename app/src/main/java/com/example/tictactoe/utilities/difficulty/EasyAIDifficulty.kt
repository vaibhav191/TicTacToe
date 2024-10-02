package com.example.tictactoe.utilities.difficulty

import com.example.tictactoe.utilities.enums.MovesEnum
import kotlin.random.Random

// Easy difficulty: AI picks a random move from available moves
class EasyAIDifficulty : AIDifficulty {
    override fun makeMove(availableMoves: List<MovesEnum>): MovesEnum {
        // Return a random available move
        if (availableMoves.isNotEmpty()) {
            return availableMoves.random()  // Choose a random move from the available ones
        }
        throw IllegalStateException("No moves available for AI to make")
    }
}