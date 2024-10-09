package com.example.tictactoe.utilities.gamemodes

import android.util.Log
import com.example.tictactoe.utilities.abstracts.GameMode
import com.example.tictactoe.utilities.enums.GameResultEnum
import com.example.tictactoe.utilities.enums.MovesEnum
import com.example.tictactoe.utilities.enums.PlayersEnum
import com.example.tictactoe.utilities.enums.StatesEnum
import com.example.tictactoe.utilities.gameobjs.Game
import com.example.tictactoe.utilities.gameobjs.PlayerInGame

class HardMode : GameMode() {
    override val playerX = PlayerInGame("Player X", PlayersEnum.X)
    override val playerO = PlayerInGame("Player O", PlayersEnum.O)
    override val game = Game(playerX, playerO, board)
    override var turn_X = true

    override fun move(move: MovesEnum): GameResultEnum {

        Log.d("HardMode", "move: $move")
        /* To be implemented */
        if (this.turn_X) {
            Log.d("HardMode", "turn_X: $turn_X")
            game.move(true, false, move)
            Log.d("HardMode", "user move rendered: $move")
            this.turn_X = false
            Log.d("HardMode", "flipped turn_X: $turn_X")
        } else {
            Log.d("HardMode", "turn_X: $turn_X")
            // randomly select from available moves

            game.move(false, true, move)
            Log.d("HardMode", "computer move rendered: $move")
            this.turn_X = true
            Log.d("HardMode", "flipped turn_X: $turn_X")
        }
        val gameState = game.checkWinner()
        Log.d("HardMode", "game state: $gameState")
        return gameState

    }

    override fun getMoveAI(): MovesEnum {
        return minimaxRoot(true)
    }

    private fun minimaxRoot(useAlphaBeta: Boolean): MovesEnum {
        val availableMoves = board.availableMoves.moves.filter { it.state == StatesEnum.AVAILABLE }
        var bestScore = Int.MIN_VALUE
        var bestMove = availableMoves[0].move

        for (moveState in availableMoves) {
            makeMove(moveState.move, PlayersEnum.O)
            val score = minimax(Int.MIN_VALUE, Int.MAX_VALUE, false)
            undoMove(moveState.move)

            if (score > bestScore) {
                bestScore = score
                bestMove = moveState.move
            }
        }

        return bestMove
    }

    private fun minimax(alpha: Int, beta: Int, isMaximizing: Boolean): Int {
        val gameState = game.checkWinner()
        if (gameState != GameResultEnum.NotOver) {
            return evaluateBoard(gameState)
        }

        val availableMoves = board.availableMoves.moves.filter { it.state == StatesEnum.AVAILABLE }

        if (isMaximizing) {
            var bestScore = Int.MIN_VALUE
            var currentAlpha = alpha
            for (moveState in availableMoves) {
                makeMove(moveState.move, PlayersEnum.O)
                val score = minimax(currentAlpha, beta, false)
                undoMove(moveState.move)
                bestScore = maxOf(bestScore, score)
                if (bestScore >= beta) break // Beta cutoff
                currentAlpha = maxOf(currentAlpha, bestScore)
            }
            return bestScore
        } else {
            var bestScore = Int.MAX_VALUE
            var currentBeta = beta
            for (moveState in availableMoves) {
                makeMove(moveState.move, PlayersEnum.X)
                val score = minimax(alpha, currentBeta, true)
                undoMove(moveState.move)
                bestScore = minOf(bestScore, score)
                if (bestScore <= alpha) break // Alpha cutoff
                currentBeta = minOf(currentBeta, bestScore)
            }
            return bestScore
        }
    }

    private fun evaluateBoard(gameResult: GameResultEnum): Int {
        return when (gameResult) {
            GameResultEnum.Win -> -10 // Player win
            GameResultEnum.Lose -> 10 // AI win
            GameResultEnum.Draw -> 0
            GameResultEnum.NotOver -> 0
        }
    }

    private fun makeMove(move: MovesEnum, player: PlayersEnum) {
        if (player == PlayersEnum.X) {
            game.move(true, false, move)
        } else {
            game.move(false, true, move)
        }
    }

    private fun undoMove(move: MovesEnum) {
        board.availableMoves.moves[move.index].state = StatesEnum.AVAILABLE
        playerX.moveList.moves[move.index].state = StatesEnum.AVAILABLE
        playerO.moveList.moves[move.index].state = StatesEnum.AVAILABLE
    }
}