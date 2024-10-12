package com.example.tictactoe.utilities.gamemodes

import android.util.Log
import com.example.tictactoe.utilities.abstracts.GameMode
import com.example.tictactoe.utilities.enums.GameResultEnum
import com.example.tictactoe.utilities.enums.MovesEnum
import com.example.tictactoe.utilities.enums.PlayersEnum
import com.example.tictactoe.utilities.gameobjs.Board
import com.example.tictactoe.utilities.gameobjs.PlayerInGame

// Class for local multiplayer that extends the abstract GameMode class
class LocalPlayervsPlayer : GameMode(
    playerX = PlayerInGame("Player X", PlayersEnum.X),
    playerO = PlayerInGame("Player O", PlayersEnum.O),
    board = Board()
) {
    init {
        Log.d("LocalPlayervsPlayer", "LocalPlayervsPlayer called")
    }

    override var turn_X =
        true // To track whose turn it is, true indicates Player X's turn and false indicates AI's turn

    // Handling the moves made based on whose turn it is
    override fun move(move: MovesEnum): GameResultEnum {
        if (this.turn_X) {
            game.move(true, false, move)
            this.turn_X = false // Switch turn to Player O
        } else {
            game.move(false, true, move)
            this.turn_X = true // Switch turn to Player X
        }
        val gameState =
            game.checkWinner() // Check the game state (win, loss, draw or not over) after each move
        return gameState
    }

    // Local multiplayer does not use AI, so this method returns null
    override fun getMoveAI(): MovesEnum? {
        return null
    }
}