package com.example.tictactoe.utilities.gamemodes

import com.example.tictactoe.utilities.abstracts.GameMode
import com.example.tictactoe.utilities.enums.GameResultEnum
import com.example.tictactoe.utilities.enums.MovesEnum
import com.example.tictactoe.utilities.enums.PlayersEnum
import com.example.tictactoe.utilities.gameobjs.Board
import com.example.tictactoe.utilities.gameobjs.Game
import com.example.tictactoe.utilities.gameobjs.PlayerInGame

class BluetoothPlayervsPlayer(
    playerX: PlayerInGame = PlayerInGame("Player X", PlayersEnum.X),
    playerO: PlayerInGame = PlayerInGame("Player O", PlayersEnum.O),
    board: Board = Board()
): GameMode() {
    override val game = Game(playerX, playerO, board)
    override var turn_X = true

    override fun move(move: MovesEnum): GameResultEnum {
        /* To be implemented */
        return GameResultEnum.NotOver
    }
    // bluetooth will not leverage AI move
    override fun getMoveAI(): MovesEnum? {
        return null
    }
}