package com.example.tictactoe.utilities.runners

import com.example.tictactoe.utilities.Board
import com.example.tictactoe.utilities.Game
import com.example.tictactoe.utilities.PlayerInGame
import com.example.tictactoe.utilities.enums.GameResultEnum
import com.example.tictactoe.utilities.enums.MovesEnum
import com.example.tictactoe.utilities.enums.PlayersEnum
import com.example.tictactoe.utilities.enums.StatesEnum
import kotlin.random.Random

class SinglePlayerMode(val difficulty: String) {
    val playerX = PlayerInGame("Player", PlayersEnum.X)
    val playerO = PlayerInGame("Computer", PlayersEnum.O)
    val board = Board()
    val game = Game(playerX, playerO, board)
    var isPlayerTurn = true

    fun playerMove(move: MovesEnum): Pair<GameResultEnum, MovesEnum?> {
        if (board.availableMoves.moves[move.index].state == StatesEnum.CONSUMED) {
            // Invalid move
            return Pair(GameResultEnum.NotOver, null)
        }

        game.move(true, false, move)
        isPlayerTurn = false

        // Check for game over after player's move
        var gameState = game.checkWinner()
        if (gameState != GameResultEnum.NotOver) {
            isPlayerTurn = true
            return Pair(gameState, null)
        }

        // AI's turn
        val aiMove = getBestMove()
        game.move(false, true, aiMove)

        // Check for game over after AI's move
        gameState = game.checkWinner()
        isPlayerTurn = true
        return Pair(gameState, aiMove)
    }

    fun getBestMove(): MovesEnum {
        return when (difficulty) {
            "Easy" -> getRandomMove()
            "Medium" -> {
                if (Random.nextBoolean()) {
                    getRandomMove()
                } else {
                    minimaxRoot(true) // Use minimax with alpha-beta pruning for optimal moves
                }
            }
            "Hard" -> minimaxRoot(true) // Always use minimax with alpha-beta pruning
            else -> getRandomMove()
        }
    }

    private fun getRandomMove(): MovesEnum {
        val availableMoves = board.availableMoves.moves.filter { it.state == StatesEnum.AVAILABLE }
        return availableMoves.random().move
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
