package com.example.tictactoe.utilities.abstracts

import com.example.tictactoe.utilities.gameobjs.Board
import com.example.tictactoe.utilities.gameobjs.Game
import com.example.tictactoe.utilities.gameobjs.PlayerInGame
import com.example.tictactoe.utilities.enums.GameResultEnum
import com.example.tictactoe.utilities.enums.MovesEnum
import com.example.tictactoe.utilities.enums.PlayersEnum

abstract class GameMode(
    open val playerX: PlayerInGame = PlayerInGame("Player X", PlayersEnum.X),
    open val playerO: PlayerInGame = PlayerInGame("Player O", PlayersEnum.O),
    val board: Board = Board(),
    open val turn_X: Boolean = true
) {
    open val game: Game = Game(playerX, playerO, board)
    abstract suspend fun move(move: MovesEnum): GameResultEnum
    abstract fun getMoveAI(): MovesEnum?
}