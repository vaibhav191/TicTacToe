package com.example.tictactoe.utilities.enums

import android.util.Log

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