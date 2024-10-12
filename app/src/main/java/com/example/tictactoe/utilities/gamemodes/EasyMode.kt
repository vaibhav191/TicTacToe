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

class EasyMode(playerX: PlayerInGame = PlayerInGame("Player X", PlayersEnum.X),
               playerO: PlayerInGame = PlayerInGame("AI", PlayersEnum.O),
               board: Board = Board(),
               turn_X: Boolean = true
) : GameMode(
    playerX = playerX,
    playerO = playerO,
    board = board
) {
    override var game  = Game(playerX, playerO, board)
    override var turn_X = turn_X

    override suspend fun move(move: MovesEnum): GameResultEnum {
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
        val randomMove = RandomSelector.select(availableMoves.map { it.move })
        return randomMove
    }
}
