package com.example.tictactoe.utilities.abstracts

import com.example.tictactoe.utilities.enums.GameResultEnum
import com.example.tictactoe.utilities.enums.MovesEnum
import com.example.tictactoe.utilities.enums.PlayersEnum
import com.example.tictactoe.utilities.gameobjs.Board
import com.example.tictactoe.utilities.gameobjs.Game
import com.example.tictactoe.utilities.gameobjs.PlayerInGame

// Abstract class representing the structure of a game mode
abstract class GameMode(
    open val playerX: PlayerInGame = PlayerInGame("Player X", PlayersEnum.X),
    open val playerO: PlayerInGame = PlayerInGame("Player O", PlayersEnum.O),
    val board: Board = Board(),
    open val turn_X: Boolean = true
) {
    open val game: Game = Game(playerX, playerO, board) // Object that holds information about the current game state (players and board)
    abstract fun move(move: MovesEnum): GameResultEnum // Abstract method that handles a move and returns the result of the game
    abstract fun getMoveAI(): MovesEnum? // Abstract method that is implemented differently in each game mode to return the AI's next move
}