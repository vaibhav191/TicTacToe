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
// Here, the AI alternates between random and strategic (minimax) moves
class MediumMode(
    playerX: PlayerInGame = PlayerInGame("Player X", PlayersEnum.X),
    playerO: PlayerInGame = PlayerInGame("AI", PlayersEnum.O),
    board: Board = Board(),
    turn_X: Boolean = true // Start with Player X's turn
) : GameMode(
    board = board,
    playerX = playerX,
    playerO = playerO
) {
    override val game = Game(playerX, playerO, board)
    override var turn_X =
        turn_X // To track whose turn it is, true indicates Player X's turn and false indicates AI's turn
    var randomTurn = false // A flag to toggle between random AI moves and strategic moves (minimax)

    // Handling the moves made based on whose turn it is
    override suspend fun move(move: MovesEnum): GameResultEnum {
        Log.d("MediumMode", "move: $move")
        /* To be implemented */
        if (this.turn_X) {
            Log.d("MediumMode", "turn_X: $turn_X")
            game.move(true, false, move)
            Log.d("MediumMode", "user move rendered: $move")
            this.turn_X = false // Switch turn to AI
            Log.d("MediumMode", "flipped turn_X: $turn_X")
        } else {
            Log.d("MediumMode", "turn_X: $turn_X")
            game.move(false, true, move)
            Log.d("MediumMode", "computer move rendered: $move")
            this.turn_X = true // Switch turn to Player X
            Log.d("MediumMode", "flipped turn_X: $turn_X")
        }
        val gameState =
            game.checkWinner() // Check the game state (win, loss, draw or not over) after each move
        Log.d("MediumMode", "game state: $gameState")
        return gameState
    }

    override fun getMoveAI(): MovesEnum {
        Log.d("MediumMode", "getMoveAI")
        val availableMoves =
            game.board.availableMoves.moves.filter { it.state == StatesEnum.AVAILABLE }
        Log.d("MediumMode", "available moves: $availableMoves")
        Log.d("MediumMode", "randomTurn: $randomTurn")
        // If random then select a random move
        if (randomTurn) {
            this.randomTurn = !this.randomTurn
            val move = RandomSelector.select(availableMoves.map { it.move })
            Log.d("MediumMode", "random move: $move")
            return move
        }
        // If not random, then select the best move using minimax
        val move = minimaxRoot(true)
        Log.d("MediumMode", "best move: $move")
        this.randomTurn = !this.randomTurn
        return move
    }

    // Minimax root function that evaluates all available moves and returns the best move for the AI
    private fun minimaxRoot(useAlphaBeta: Boolean): MovesEnum {
        val availableMoves = board.availableMoves.moves.filter { it.state == StatesEnum.AVAILABLE }
        var bestScore = Int.MIN_VALUE
        var bestMove = availableMoves[0].move

        // Iterate through available moves to find the one with the best score using minimax
        for (moveState in availableMoves) {
            makeMove(moveState.move, PlayersEnum.O) // Simulate AI's move
            val score = minimax(
                Int.MIN_VALUE,
                Int.MAX_VALUE,
                false
            ) // Get the score for the particular move
            undoMove(moveState.move) // Undo the move after evaluating it

            // Update best move if the score is better than the current best score
            if (score > bestScore) {
                bestScore = score
                bestMove = moveState.move
            }
        }

        return bestMove
    }

    // `isMaximizing` determines if it's the AI's turn (true for AI, false for Player X)
    private fun minimax(alpha: Int, beta: Int, isMaximizing: Boolean): Int {
        val gameState =
            game.checkWinner() // Check the game state (win, loss, draw or not over) after each move
        // Base case: If the game is over, return the evaluation of the board
        if (gameState != GameResultEnum.NotOver) {
            return evaluateBoard(gameState)
        }

        val availableMoves = board.availableMoves.moves.filter { it.state == StatesEnum.AVAILABLE }

        // AI is maximizing its score (trying to win)
        if (isMaximizing) {
            var bestScore = Int.MIN_VALUE
            var currentAlpha = alpha
            for (moveState in availableMoves) {
                makeMove(moveState.move, PlayersEnum.O)
                val score = minimax(currentAlpha, beta, false) // Recursive minimax call
                undoMove(moveState.move)
                bestScore = maxOf(bestScore, score)
                if (bestScore >= beta) break // Beta cutoff for pruning
                currentAlpha = maxOf(currentAlpha, bestScore)
            }
            return bestScore
        } else {
            // Player X is minimizing the score (trying to avoid losing)
            var bestScore = Int.MAX_VALUE
            var currentBeta = beta
            for (moveState in availableMoves) {
                makeMove(moveState.move, PlayersEnum.X)
                val score = minimax(alpha, currentBeta, true) // Recursive minimax call
                undoMove(moveState.move)
                bestScore = minOf(bestScore, score)
                if (bestScore <= alpha) break // Alpha cutoff for pruning
                currentBeta = minOf(currentBeta, bestScore)
            }
            return bestScore
        }
    }

    // Evaluating the board state at the end of the game and returning a score based on who won
    private fun evaluateBoard(gameResult: GameResultEnum): Int {
        return when (gameResult) {
            GameResultEnum.Win -> -10 // Player win
            GameResultEnum.Lose -> 10 // AI win
            GameResultEnum.Draw -> 0
            GameResultEnum.NotOver -> 0
        }
    }

    // Simulating moves
    private fun makeMove(move: MovesEnum, player: PlayersEnum) {
        if (player == PlayersEnum.X) {
            game.move(true, false, move)
        } else {
            game.move(false, true, move)
        }
    }

    // Reverting moves and setting the state back to available
    private fun undoMove(move: MovesEnum) {
        board.availableMoves.moves[move.index].state = StatesEnum.AVAILABLE
        playerX.moveList.moves[move.index].state = StatesEnum.AVAILABLE
        playerO.moveList.moves[move.index].state = StatesEnum.AVAILABLE
    }
}