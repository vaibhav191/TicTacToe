package com.example.tictactoe.utilities.gamemodes

import com.example.tictactoe.utilities.gameobjs.Game
import com.example.tictactoe.utilities.gameobjs.PlayerInGame
import com.example.tictactoe.utilities.abstracts.GameMode
import com.example.tictactoe.utilities.enums.GameResultEnum
import com.example.tictactoe.utilities.enums.MovesEnum
import com.example.tictactoe.utilities.enums.PlayersEnum

class LocalPlayervsPlayer: GameMode() {
    override val playerX = PlayerInGame("Player X", PlayersEnum.X)
    override val playerO = PlayerInGame("Player O", PlayersEnum.O)
    override val game = Game(playerX, playerO, board)
    override var turn_X = true

    override fun move(move: MovesEnum): GameResultEnum {
        if (this.turn_X) {
            game.move(true, false, move)
            this.turn_X = false
        }
        else {
            game.move(false, true, move)
            this.turn_X = true
        }
        val gameState = game.checkWinner()
        return gameState
    }
}