package com.example.tictactoe.utilities.difficulty

import com.example.tictactoe.utilities.enums.MovesEnum

// GameAI class to hold and manage the current difficulty strategy
class GameAI(private var difficulty: AIDifficulty) {

    // Function to set a new difficulty strategy
    fun setDifficulty(difficulty: AIDifficulty) {
        this.difficulty = difficulty
    }

    // Function to make a move using the current difficulty strategy
    fun makeMove(availableMoves: List<MovesEnum>): MovesEnum {
        return difficulty.makeMove(availableMoves)
    }
}