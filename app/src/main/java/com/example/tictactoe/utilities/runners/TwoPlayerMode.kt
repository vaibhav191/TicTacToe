package com.example.tictactoe.utilities.runners

import android.util.Log
import com.example.tictactoe.utilities.Board
import com.example.tictactoe.utilities.Game
import com.example.tictactoe.utilities.PlayerInGame
import com.example.tictactoe.utilities.difficulty.AIDifficulty
import com.example.tictactoe.utilities.difficulty.EasyAIDifficulty
import com.example.tictactoe.utilities.difficulty.GameAI
import com.example.tictactoe.utilities.enums.GameResultEnum
import com.example.tictactoe.utilities.enums.MovesEnum
import com.example.tictactoe.utilities.enums.PlayersEnum

class TwoPlayerMode(val difficulty: String) {
    val playerX = PlayerInGame("Player X", PlayersEnum.X)
    val playerO = PlayerInGame("Player O", PlayersEnum.O)
    val board = Board()
    val game = Game(playerX, playerO, board)
    var turn_X = true // Track whose turn it is: true for X (Player), false for O (AI)

    // Initialize the GameAI with the appropriate difficulty strategy
    private var gameAI: GameAI = GameAI(selectDifficultyStrategy(difficulty))

    // Array and map to hold available moves and their indices
    private var availableMovesArray: MutableList<MovesEnum> = mutableListOf()
    private var moveIndexMap: MutableMap<MovesEnum, Int> = mutableMapOf()

    init {
        initializeMoves()
    }

    // Initialize the available moves array and map at the start of the game
    private fun initializeMoves() {
        availableMovesArray = MovesEnum.values().toMutableList()  // Corrected to use values() instead of entries
        for (i in availableMovesArray.indices) {
            moveIndexMap[availableMovesArray[i]] = i
        }
    }

    // Function to select the correct AI difficulty strategy
    private fun selectDifficultyStrategy(difficulty: String): AIDifficulty {
        return when (difficulty) {
            "Easy" -> EasyAIDifficulty()
            // Add more strategies like MediumAIDifficulty(), HardAIDifficulty() here
            else -> EasyAIDifficulty()  // Default to Easy for now
        }
    }

    // Function to handle the player and AI moves
    fun move(move: MovesEnum): GameResultEnum {
        if (this.turn_X) {
            // Player X makes a move
            game.move(true, false, move)
            this.turn_X = false  // Lock the turn so AI gets a chance to play

            // Remove this move from available moves
            removeMove(move)

            // Check for game result after player move
            val gameState = game.checkWinner()
            Log.d("GameFlow", "Game state after player move: $gameState")

            if (gameState != GameResultEnum.NotOver) {
                Log.d("GameFlow", "Player move resulted in game end: $gameState")
                return gameState  // Return if game is over
            }

            // Make AI move after player move
            makeAIMove()
        }

        return game.checkWinner()  // Always return the current game state
    }

    // Function to make a move using the AI based on the difficulty level
    fun makeAIMove() {
        Log.d("GameFlow", "AI is making its move")

        if (availableMovesArray.isNotEmpty()) {
            // Use the AI strategy to select a move
            val aiMove = gameAI.makeMove(availableMovesArray)
            Log.d("GameFlow", "AI chose move: $aiMove")

            // Apply the move on the game board
            game.move(false, true, aiMove)  // AI makes its move
            this.turn_X = true  // Switch back to player's turn

            // Remove the AI's move from available moves as it's now occupied
            removeMove(aiMove)

            Log.d("GameFlow", "AI move completed. It's now player's turn")
        }
        else {
            Log.d("GameFlow", "No available moves for AI to make")
        }
    }

    // Function to remove a move from the array and map in constant time
    private fun removeMove(move: MovesEnum) {
        if (!moveIndexMap.containsKey(move)) return

        // Get the index of the move to be removed
        val index = moveIndexMap[move] ?: return

        // Swap the move with the last element in the array
        val lastIndex = availableMovesArray.size - 1
        val lastMove = availableMovesArray[lastIndex]

        // Swap positions in the array
        availableMovesArray[index] = lastMove
        availableMovesArray.removeAt(lastIndex)

        // Update the index of the last element in the map
        moveIndexMap[lastMove] = index

        // Remove the deleted move from the map
        moveIndexMap.remove(move)
    }
}
