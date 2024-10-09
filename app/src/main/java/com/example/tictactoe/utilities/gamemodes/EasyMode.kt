package com.example.tictactoe.utilities.gamemodes

import android.util.Log
import com.example.tictactoe.utilities.abstracts.GameMode
import com.example.tictactoe.utilities.enums.GameResultEnum
import com.example.tictactoe.utilities.enums.MovesEnum
import com.example.tictactoe.utilities.enums.PlayersEnum
import com.example.tictactoe.utilities.enums.StatesEnum
import com.example.tictactoe.utilities.gameobjs.Game
import com.example.tictactoe.utilities.gameobjs.PlayerInGame
import com.example.tictactoe.utilities.selector.SelectRandom

class EasyMode : GameMode() {
    override val playerX = PlayerInGame("Player X", PlayersEnum.X)
    override val playerO = PlayerInGame("Player O", PlayersEnum.O)
    override var game  = Game(playerX, playerO, board)
    override var turn_X = true


    override fun move(move: MovesEnum): GameResultEnum {
        Log.d("EasyMode", "move: $move")
        /* To be implemented */
        if (this.turn_X) {
            Log.d("EasyMode", "turn_X: $turn_X")
            game.move(true, false, move)
            Log.d("EasyMode", "user move rendered: $move")
            this.turn_X = false
            Log.d("EasyMode", "flipped turn_X: $turn_X")
        } else {
            Log.d("EasyMode", "turn_X: $turn_X")
            // randomly select from available moves

            game.move(false, true, move)
            Log.d("EasyMode", "computer move rendered: $move")
            this.turn_X = true
            Log.d("EasyMode", "flipped turn_X: $turn_X")
        }
        val gameState = game.checkWinner()
        Log.d("EasyMode", "game state: $gameState")
        return gameState
    }

    override fun getMoveAI(): MovesEnum {
        Log.d("EasyMode", "moveAI")
        val availableMoves =
            game.board.availableMoves.moves.filter { it.state == StatesEnum.AVAILABLE }
        val randomMove = SelectRandom.select(availableMoves.map { it.move })
        return randomMove
    }
}
