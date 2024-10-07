package com.example.tictactoe.utilities.runners

import com.example.tictactoe.utilities.Board
import com.example.tictactoe.utilities.Game
import com.example.tictactoe.utilities.PlayerInGame
import com.example.tictactoe.utilities.bluetooth.BluetoothGameManager
import com.example.tictactoe.utilities.enums.GameResultEnum
import com.example.tictactoe.utilities.enums.MovesEnum
import com.example.tictactoe.utilities.enums.PlayersEnum
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class TwoPlayerMode(private val bluetoothGameManager: BluetoothGameManager? = null) {
    private val game: Game
    private val _turn_X = MutableStateFlow(true)
    val turn_X: StateFlow<Boolean> = _turn_X

    val playerX: PlayerInGame
        get() = game.playerX

    val playerO: PlayerInGame
        get() = game.playerO

    init {
        val playerX = PlayerInGame("Player X", PlayersEnum.X)
        val playerO = PlayerInGame("Player O", PlayersEnum.O)
        game = Game(playerX, playerO, Board())
    }

    suspend fun move(move: MovesEnum): GameResultEnum {
        if (bluetoothGameManager != null) {
            // Send move to opponent
            bluetoothGameManager.sendMove(move)
        }

        val result = if (_turn_X.value) {
            game.move(true, false, move)
        } else {
            game.move(false, true, move)
        }

        _turn_X.value = !_turn_X.value
        return result
    }

    fun canMakeMove(move: MovesEnum): Boolean {
        return game.board.availableMoves.moves[move.index].state == com.example.tictactoe.utilities.enums.StatesEnum.AVAILABLE
    }

    fun resetGame() {
        game.board = Board()
        _turn_X.value = true
    }
}