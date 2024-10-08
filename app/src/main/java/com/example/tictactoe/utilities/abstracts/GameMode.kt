package com.example.tictactoe.utilities.abstracts

import com.example.tictactoe.utilities.gameobjs.Board
import com.example.tictactoe.utilities.gameobjs.Game
import com.example.tictactoe.utilities.gameobjs.PlayerInGame
import com.example.tictactoe.utilities.enums.GameResultEnum
import com.example.tictactoe.utilities.enums.MovesEnum

abstract class GameMode {
    abstract val playerX: PlayerInGame
    abstract val playerO: PlayerInGame
    abstract var turn_X: Boolean
    val board = Board()
    abstract val game: Game
    abstract fun move(move: MovesEnum): GameResultEnum
}