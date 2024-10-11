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

class MediumMode(
    playerX: PlayerInGame = PlayerInGame("Player X", PlayersEnum.X),
    playerO: PlayerInGame = PlayerInGame("AI", PlayersEnum.O),
    board: Board = Board(),
    turn_X: Boolean = true
): GameMode(
    board = board,
    playerX = playerX,
    playerO = playerO
) {
    override val game = Game(playerX, playerO, board)
    override var turn_X = turn_X
    var randomTurn = false

    override fun move(move: MovesEnum): GameResultEnum {
        Log.d("MediumMode", "move: $move")
        /* To be implemented */
        if (this.turn_X) {
            Log.d("MediumMode", "turn_X: $turn_X")
            game.move(true, false, move)
            Log.d("MediumMode", "user move rendered: $move")
            this.turn_X = false
            Log.d("MediumMode", "flipped turn_X: $turn_X")
        } else {
            Log.d("MediumMode", "turn_X: $turn_X")
            // randomly select from available moves

            game.move(false, true, move)
            Log.d("MediumMode", "computer move rendered: $move")
            this.turn_X = true
            Log.d("MediumMode", "flipped turn_X: $turn_X")
        }
        val gameState = game.checkWinner()
        Log.d("MediumMode", "game state: $gameState")
        return gameState
    }
    override fun getMoveAI(): MovesEnum {
        Log.d("MediumMode", "getMoveAI")
        val availableMoves = game.board.availableMoves.moves.filter { it.state == StatesEnum.AVAILABLE }
        Log.d("MediumMode", "available moves: $availableMoves")
        Log.d("MediumMode", "randomTurn: $randomTurn")
        // if random then select a random move
        if (randomTurn) {
            this.randomTurn = !this.randomTurn
            val move = RandomSelector.select(availableMoves.map { it.move })
            Log.d("MediumMode", "random move: $move")
            return move
        }
        // if not random, then select the best move
        val move = minimaxRoot(true)
        Log.d("MediumMode", "best move: $move")
        this.randomTurn = !this.randomTurn
        return move
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