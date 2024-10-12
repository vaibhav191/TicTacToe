package com.example.tictactoe.utilities.enums

import android.util.Log

// used to define the local difficulty level of the game
// Easy, Medium and Hard are modes to play against the AI
// PlayervsPlayer is a mode to play against another player locally on the same screen
enum class LocalDifficultyEnum(val value: Int) {
    Easy(0),
    Medium(1),
    Hard(2),
    PlayervsPlayer(3);

    companion object {
        fun getDifficulty(value: Int): LocalDifficultyEnum? {
            Log.d("LocalDifficultyEnum", "getDifficulty: $value")
            return entries.find { it.value == value }
        }
    }
}