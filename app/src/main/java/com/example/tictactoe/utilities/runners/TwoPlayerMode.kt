package com.example.tictactoe.utilities.runners

import com.example.tictactoe.utilities.Board
import com.example.tictactoe.utilities.Game
import com.example.tictactoe.utilities.PlayerInGame
import com.example.tictactoe.utilities.enums.GameResultEnum
import com.example.tictactoe.utilities.enums.MovesEnum
import com.example.tictactoe.utilities.enums.PlayersEnum

class TwoPlayerMode() {
    val playerX = PlayerInGame("Player X", PlayersEnum.X)
    val playerO = PlayerInGame("Player O", PlayersEnum.O)
    val board = Board()
    val game = Game(playerX, playerO, board)
    var turn_X = true

    fun move(move: MovesEnum): GameResultEnum {
        if (this.turn_X) {
            game.move(true, false, move)
            this.turn_X = false
        } else {
            game.move(false, true, move)
            this.turn_X = true
        }
        val gameState = game.checkWinner()
        return gameState
    }
}