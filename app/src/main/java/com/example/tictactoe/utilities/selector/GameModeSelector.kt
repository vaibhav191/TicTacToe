package com.example.tictactoe.utilities.selector

import android.util.Log
import com.example.tictactoe.utilities.abstracts.GameMode
import com.example.tictactoe.utilities.enums.ConnectionTypeEnum
import com.example.tictactoe.utilities.enums.LocalDifficultyEnum
import com.example.tictactoe.utilities.enums.PlayersEnum
import com.example.tictactoe.utilities.gamemodes.BluetoothPlayervsPlayer
import com.example.tictactoe.utilities.gamemodes.EasyMode
import com.example.tictactoe.utilities.gamemodes.HardMode
import com.example.tictactoe.utilities.gamemodes.LocalPlayervsPlayer
import com.example.tictactoe.utilities.gamemodes.MediumMode
import com.example.tictactoe.utilities.gameobjs.Board
import com.example.tictactoe.utilities.gameobjs.PlayerInGame

class GameModeSelector(val difficulty: LocalDifficultyEnum, val connection: ConnectionTypeEnum,
                       val board:Board = Board(),
                       val playerX: PlayerInGame = PlayerInGame("Player X", PlayersEnum.X),
                       val playerO: PlayerInGame = PlayerInGame("Player O", PlayersEnum.O),
                       val turn_X: Boolean = true
) {
    fun getGameMode(): GameMode{
        when(connection){
            ConnectionTypeEnum.Local -> {
                when (difficulty) {
                    LocalDifficultyEnum.Easy -> {
                        val game = EasyMode(board = board, playerX = playerX, playerO = playerO, turn_X = turn_X)
                        Log.d("GameModeSelector", "game: $game")
                        Log.d("GameModeSelector", "game.turn_X: ${game.turn_X}")
                        Log.d("GameModeSelector", "game.board: ${game.game.board.availableMoves}")
                        return game
                    }
                    LocalDifficultyEnum.Medium -> {
                        val game = MediumMode(board = board, playerX = playerX, playerO = playerO, turn_X = turn_X)
                        Log.d("GameModeSelector", "game: $game")
                        Log.d("GameModeSelector", "game.turn_X: ${game.turn_X}")
                        Log.d("GameModeSelector", "game.board: ${game.game.board.availableMoves}")
                        return  game
                    }
                    LocalDifficultyEnum.Hard -> {
                        val game = HardMode(board = board, playerX = playerX, playerO = playerO, turn_X = turn_X)
                        Log.d("GameModeSelector", "game: $game")
                        Log.d("GameModeSelector", "game.turn_X: ${game.turn_X}")
                        Log.d("GameModeSelector", "game.board: ${game.game.board.availableMoves}")
                        return game
                    }
                    LocalDifficultyEnum.PlayervsPlayer -> {
                        return LocalPlayervsPlayer()
                    }
                    else -> {
                        throw Exception("Invalid difficulty")
                    }
                }
            }
            ConnectionTypeEnum.Bluetooth -> {
                return BluetoothPlayervsPlayer()
            }
            else -> {
                throw Exception("Invalid connection")
            }
        }
    }
}