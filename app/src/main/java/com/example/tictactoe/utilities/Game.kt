package com.example.tictactoe.utilities

import com.example.tictactoe.utilities.enums.GameResultEnum
import com.example.tictactoe.utilities.enums.MovesEnum
import com.example.tictactoe.utilities.enums.PlayersEnum
import com.example.tictactoe.utilities.enums.StatesEnum

class Game(
    val playerX: PlayerInGame,
    val playerO: PlayerInGame,
    val board: Board
) {

    val winConditions = listOf(
        listOf(0, 1, 2),
        listOf(3, 4, 5),
        listOf(6, 7, 8),
        listOf(0, 3, 6),
        listOf(1, 4, 7),
        listOf(2, 5, 8),
        listOf(0, 4, 8),
        listOf(2, 4, 6)
    )
    fun move(playerXmove: Boolean, playerOMove: Boolean, move: MovesEnum) {
        // check if only one player can move, only receive one true
        if (playerXmove and playerOMove) {
            throw Exception("Only one player can move!")
        }
        val player = if (playerXmove) playerX else playerO

        val moveIndex = move.moveIndex
        // check if move is available or already consumed, consumed moves cannot move again
        if (player.moveList.moves[moveIndex].state == StatesEnum.CONSUMED) {
            return
        }

        board.availableMoves.moves[moveIndex].state = StatesEnum.CONSUMED
        player.moveList.moves[moveIndex].state = StatesEnum.CONSUMED
    }

    fun checkWinner(): GameResultEnum {
        // check if any player won
        val players = listOf(playerX, playerO)
        for (player in players) {
            for (condition in winConditions) {
                // for each win condition check if all moves in the condition are consumed
                if (player.moveList.moves[condition[0]].state == StatesEnum.CONSUMED &&
                    player.moveList.moves[condition[1]].state == StatesEnum.CONSUMED &&
                    player.moveList.moves[condition[2]].state == StatesEnum.CONSUMED)
                {
                    // check if player is X or O
                    if (player.playerType == PlayersEnum.X) {
                        // if player is X, return win
                     return GameResultEnum.Win
                    }
                    else {
                        return GameResultEnum.Lose
                    }
                }
            }
        }

        // If no player has won, check if no moves are available (i.e., a draw)
        return if (board.availableMoves.moves.none { it.state == StatesEnum.AVAILABLE }) {
            GameResultEnum.Draw
        } else {
            GameResultEnum.NotOver
        }
    }

    fun getWinningLine(): List<Int>? {
        val players = listOf(playerX, playerO)
        for (player in players) {
            for (condition in winConditions) {
                // Check if this win condition is satisfied by the player
                if (player.moveList.moves[condition[0]].state == StatesEnum.CONSUMED &&
                    player.moveList.moves[condition[1]].state == StatesEnum.CONSUMED &&
                    player.moveList.moves[condition[2]].state == StatesEnum.CONSUMED
                ) {
                    return condition // Return the indices that form the winning line
                }
            }
        }
        return null // Return null if no winning line is found
    }
}