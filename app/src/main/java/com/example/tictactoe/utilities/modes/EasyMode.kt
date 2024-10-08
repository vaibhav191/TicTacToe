package com.example.tictactoe.utilities.modes

import com.example.tictactoe.utilities.enums.GameResultEnum
import com.example.tictactoe.utilities.enums.MovesEnum
import com.example.tictactoe.utilities.enums.PlayersEnum
import com.example.tictactoe.utilities.enums.StatesEnum
import com.example.tictactoe.utilities.gameobjs.Board
import com.example.tictactoe.utilities.gameobjs.Game
import com.example.tictactoe.utilities.gameobjs.PlayerInGame
import com.example.tictactoe.utilities.randomselector.SelectRandom

class EasyMode {
    val playerX = PlayerInGame("Player X", PlayersEnum.X)
    val playerO = PlayerInGame("Player O", PlayersEnum.O)
    val board = Board()
    val game = Game(playerX, playerO, board)
    var turn_X = true

    fun move(move: MovesEnum): GameResultEnum {
        if (this.turn_X) {
            game.move(true, false, move)
            this.turn_X = false
        }
        else {
            // AI random selector moves
            val availableMoves = game.board.availableMoves.moves.filter { it.state == StatesEnum.AVAILABLE }
            val randomMove = SelectRandom.select(availableMoves.map { it.move })
            game.move(false, true, randomMove)
            this.turn_X = true
        }
        val gameState = game.checkWinner()
        return gameState
    }
}