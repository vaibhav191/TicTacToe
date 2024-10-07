package com.example.tictactoe.utilities

import com.example.tictactoe.utilities.enums.GameResultEnum
import com.example.tictactoe.utilities.enums.MovesEnum
import com.example.tictactoe.utilities.enums.PlayersEnum
import com.example.tictactoe.utilities.enums.StatesEnum

class Game(
    val playerX: PlayerInGame,
    val playerO: PlayerInGame,
    var board: Board
) {
    private val winConditions = listOf(
        listOf(0, 1, 2),
        listOf(3, 4, 5),
        listOf(6, 7, 8),
        listOf(0, 3, 6),
        listOf(1, 4, 7),
        listOf(2, 5, 8),
        listOf(0, 4, 8),
        listOf(2, 4, 6)
    )

    fun move(playerXmove: Boolean, playerOMove: Boolean, move: MovesEnum): GameResultEnum {
        if (playerXmove and playerOMove) {
            throw Exception("Only one player can move!")
        }
        val player = if (playerXmove) playerX else playerO

        val moveIndex = move.index
        if (player.moveList.moves[moveIndex].state == StatesEnum.CONSUMED) {
            return GameResultEnum.NotOver
        }

        board.availableMoves.moves[moveIndex].state = StatesEnum.CONSUMED
        player.moveList.moves[moveIndex].state = StatesEnum.CONSUMED

        return checkWinner()
    }

    private fun checkWinner(): GameResultEnum {
        if (board.availableMoves.moves.none { it.state == StatesEnum.AVAILABLE }) {
            return GameResultEnum.Draw
        }

        val players = listOf(playerX, playerO)
        for (player in players) {
            for (condition in winConditions) {
                if (player.moveList.moves[condition[0]].state == StatesEnum.CONSUMED &&
                    player.moveList.moves[condition[1]].state == StatesEnum.CONSUMED &&
                    player.moveList.moves[condition[2]].state == StatesEnum.CONSUMED
                ) {
                    return if (player.playerType == PlayersEnum.X) {
                        GameResultEnum.Win
                    } else {
                        GameResultEnum.Lose
                    }
                }
            }
        }

        return GameResultEnum.NotOver
    }
}