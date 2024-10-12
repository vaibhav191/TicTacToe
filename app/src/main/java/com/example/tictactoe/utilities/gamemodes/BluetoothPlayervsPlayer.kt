package com.example.tictactoe.utilities.gamemodes

import com.example.tictactoe.utilities.abstracts.GameMode
import com.example.tictactoe.utilities.gameobjs.Game
import com.example.tictactoe.utilities.gameobjs.PlayerInGame
import com.example.tictactoe.utilities.bluetooth.BluetoothGameManager
import com.example.tictactoe.utilities.enums.GameResultEnum
import com.example.tictactoe.utilities.enums.MovesEnum
import com.example.tictactoe.utilities.enums.PlayersEnum
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

// implementation of the game mode for Bluetooth
class BluetoothPlayervsPlayer(private val bluetoothGameManager: BluetoothGameManager? = null) : GameMode() {
    override val playerX = PlayerInGame("Player X", PlayersEnum.X)
    override val playerO = PlayerInGame("Player O", PlayersEnum.O)
    override var turn_X = true
    override val game = Game(playerX, playerO, board)

    private val _turnXFlow = MutableStateFlow(true)
    val turnXFlow: StateFlow<Boolean> = _turnXFlow

    override suspend fun move(move: MovesEnum): GameResultEnum {
        bluetoothGameManager?.sendMove(move)

        val result = if (_turnXFlow.value) {
            game.move(true, false, move)
        } else {
            game.move(false, true, move)
        }

        _turnXFlow.value = !_turnXFlow.value
        turn_X = _turnXFlow.value
        return result
    }

    fun canMakeMove(move: MovesEnum): Boolean {
        return game.board.availableMoves.moves[move.index].state == com.example.tictactoe.utilities.enums.StatesEnum.AVAILABLE
    }

    fun resetGame() {
        board.resetBoard()
        _turnXFlow.value = true
        turn_X = true
    }

    override fun getMoveAI(): MovesEnum? {
        return null
    }
}