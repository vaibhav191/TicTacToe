package com.example.tictactoe.utilities.bluetooth

import kotlinx.serialization.Serializable

// data classes for the game data, game state, and metadata, choice and minigame
// used for playing game and defining game state over bluetooth
@Serializable
data class GameData(
    var gameState: GameState = GameState(),
    var metadata: Metadata = Metadata(listOf(), MiniGame())
)

@Serializable
data class GameState(
    var board: List<List<String>> = List(3) { List(3) { " " } },
    var turn: String = "0",
    var winner: String = " ",
    var draw: Boolean = false,
    var connectionEstablished: Boolean = true,
    var reset: Boolean = false
)

@Serializable
data class Metadata(
    var choices: List<Choice>,
    var miniGame: MiniGame
)

@Serializable
data class Choice(
    val id: String,
    val name: String
)

@Serializable
data class MiniGame(
    var player1Choice: String = "",
    var player2Choice: String = ""
)