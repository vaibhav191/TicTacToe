package com.example.tictactoe.utilities.difficulty

import com.example.tictactoe.utilities.enums.MovesEnum

// Define an interface for AI difficulty levels
interface AIDifficulty {
    fun makeMove(availableMoves: List<MovesEnum>): MovesEnum
}