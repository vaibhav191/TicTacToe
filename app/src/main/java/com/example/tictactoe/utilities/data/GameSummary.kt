package com.example.tictactoe.utilities.data

data class GameSummary(
    val totalGames: Int,
    val totalWins: Int,
    val totalDraws: Int,
    val totalLosses: Int
)

data class GameHistoryItem(
    val id: Long,
    val date: String,
    val time: String,
    val mode: String,
    val difficulty: String?,
    val opponent: String,
    val result: String
)
