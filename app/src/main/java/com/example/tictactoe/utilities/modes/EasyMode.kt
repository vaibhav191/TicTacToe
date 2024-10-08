package com.example.tictactoe.utilities.modes

import com.example.tictactoe.utilities.enums.PlayersEnum
import com.example.tictactoe.utilities.gameobjs.Board
import com.example.tictactoe.utilities.gameobjs.Game
import com.example.tictactoe.utilities.gameobjs.PlayerInGame

class EasyMode {
    val playerX = PlayerInGame("Player X", PlayersEnum.X)
    val playerO = PlayerInGame("Player O", PlayersEnum.O)
    val board = Board()
    val game = Game(playerX, playerO, board)

}