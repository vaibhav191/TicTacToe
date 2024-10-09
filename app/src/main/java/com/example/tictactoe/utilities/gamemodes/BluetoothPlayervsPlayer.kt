package com.example.tictactoe.utilities.gamemodes

import com.example.tictactoe.utilities.abstracts.GameMode
import com.example.tictactoe.utilities.enums.GameResultEnum
import com.example.tictactoe.utilities.enums.MovesEnum
import com.example.tictactoe.utilities.enums.PlayersEnum
import com.example.tictactoe.utilities.gameobjs.Game
import com.example.tictactoe.utilities.gameobjs.PlayerInGame

class BluetoothPlayervsPlayer: GameMode() {
    override val playerX = PlayerInGame("Player X", PlayersEnum.X)
    override val playerO = PlayerInGame("Player O", PlayersEnum.O)
    override val game = Game(playerX, playerO, board)
    override var turn_X = true

    override fun move(move: MovesEnum): GameResultEnum {
        /* To be implemented */
        return GameResultEnum.NotOver
    }
    override fun getMoveAI(): MovesEnum? {
        return null
    }
}