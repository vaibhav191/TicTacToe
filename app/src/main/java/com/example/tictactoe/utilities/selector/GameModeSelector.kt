package com.example.tictactoe.utilities.selector

import com.example.tictactoe.utilities.abstracts.GameMode
import com.example.tictactoe.utilities.enums.ConnectionTypeEnum
import com.example.tictactoe.utilities.enums.LocalDifficultyEnum
import com.example.tictactoe.utilities.gamemodes.BluetoothPlayervsPlayer
import com.example.tictactoe.utilities.gamemodes.EasyMode
import com.example.tictactoe.utilities.gamemodes.HardMode
import com.example.tictactoe.utilities.gamemodes.LocalPlayervsPlayer
import com.example.tictactoe.utilities.gamemodes.MediumMode
import com.example.tictactoe.utilities.gameobjs.Game
import com.example.tictactoe.utilities.gameobjs.PlayerInGame

class GameModeSelector(val difficulty: LocalDifficultyEnum, val connection: ConnectionTypeEnum, ) {
    fun getGameMode(): GameMode{
        when(connection){
            ConnectionTypeEnum.Local -> {
                when (difficulty) {
                    LocalDifficultyEnum.Easy -> {
                        return EasyMode()
                    }
                    LocalDifficultyEnum.Medium -> {
                        return MediumMode()
                    }
                    LocalDifficultyEnum.Hard -> {
                        return HardMode()
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