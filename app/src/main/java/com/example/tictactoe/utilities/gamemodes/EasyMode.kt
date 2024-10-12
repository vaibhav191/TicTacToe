package com.example.tictactoe.utilities.gamemodes

import android.util.Log
import com.example.tictactoe.utilities.abstracts.GameMode
import com.example.tictactoe.utilities.enums.GameResultEnum
import com.example.tictactoe.utilities.enums.MovesEnum
import com.example.tictactoe.utilities.enums.PlayersEnum
import com.example.tictactoe.utilities.enums.StatesEnum
import com.example.tictactoe.utilities.gameobjs.Board
import com.example.tictactoe.utilities.gameobjs.Game
import com.example.tictactoe.utilities.gameobjs.PlayerInGame
import com.example.tictactoe.utilities.selector.RandomSelector

// Extends the abstract GameMode class
// AI makes random moves in Easy Mode
class EasyMode(
    playerX: PlayerInGame = PlayerInGame("Player X", PlayersEnum.X),
    playerO: PlayerInGame = PlayerInGame("AI", PlayersEnum.O),
    board: Board = Board(),
    turn_X: Boolean = true // Start with Player X's turn
) : GameMode(
    playerX = playerX,
    playerO = playerO,
    board = board
) {
    override var game = Game(playerX, playerO, board)
    override var turn_X =
        turn_X // To track whose turn it is, true indicates Player X's turn and false indicates AI's turn

    // Handling the moves made based on whose turn it is
    override fun move(move: MovesEnum): GameResultEnum {
        Log.d("EasyMode", "move: $move")
        /* To be implemented */
        if (this.turn_X) {
            Log.d("EasyMode", "turn_X: $turn_X")
            game.move(true, false, move)
            Log.d("EasyMode", "user move rendered: $move")
            this.turn_X = false // Switch turn to AI
            Log.d("EasyMode", "flipped turn_X: $turn_X")
        } else {
            Log.d("EasyMode", "turn_X: $turn_X")
            game.move(false, true, move)
            Log.d("EasyMode", "computer move rendered: $move")
            this.turn_X = true // Switch turn to Player X
            Log.d("EasyMode", "flipped turn_X: $turn_X")
        }
        val gameState =
            game.checkWinner() // Check the game state (win, loss, draw or not over) after each move
        Log.d("EasyMode", "game state: $gameState")
        return gameState
    }

    override fun getMoveAI(): MovesEnum {
        Log.d("EasyMode", "moveAI")
        // Filter the available moves that haven't been taken yet
        val availableMoves =
            game.board.availableMoves.moves.filter { it.state == StatesEnum.AVAILABLE }
        // Randomly select one of the available moves
        val randomMove = RandomSelector.select(availableMoves.map { it.move })
        return randomMove
    }
}
