package com.example.tictactoe.utilities.gamemodes

import android.util.Log
import com.example.tictactoe.utilities.gameobjs.Game
import com.example.tictactoe.utilities.gameobjs.PlayerInGame
import com.example.tictactoe.utilities.abstracts.GameMode
import com.example.tictactoe.utilities.enums.GameResultEnum
import com.example.tictactoe.utilities.enums.MovesEnum
import com.example.tictactoe.utilities.enums.PlayersEnum
import com.example.tictactoe.utilities.gameobjs.Board

class LocalPlayervsPlayer: GameMode(
    playerX = PlayerInGame("Player X", PlayersEnum.X),
    playerO = PlayerInGame("Player O", PlayersEnum.O),
    board = Board()
) {
    init {
        Log.d("LocalPlayervsPlayer", "LocalPlayervsPlayer called")
    }
    override var turn_X = true

    override fun move(move: MovesEnum): GameResultEnum {
        if (this.turn_X) {
            game.move(true, false, move)
            this.turn_X = false
        }
        else {
            game.move(false, true, move)
            this.turn_X = true
        }
        val gameState = game.checkWinner()
        return gameState
    }
    override fun getMoveAI(): MovesEnum? {
        return null
    }
}