package com.example.tictactoe.utilities.gameobjs

import com.example.tictactoe.utilities.data.MoveListData
import com.example.tictactoe.utilities.data.MoveStateData
import com.example.tictactoe.utilities.enums.MovesEnum
import com.example.tictactoe.utilities.enums.StatesEnum

class Board() {
    var availableMoves: MoveListData
    init {
        var availableMoves = MoveListData(
            mutableListOf(
                MoveStateData(MovesEnum.TOP_LEFT, StatesEnum.AVAILABLE),
                MoveStateData(MovesEnum.TOP_CENTER, StatesEnum.AVAILABLE),
                MoveStateData(MovesEnum.TOP_RIGHT, StatesEnum.AVAILABLE),
                MoveStateData(MovesEnum.MIDDLE_LEFT, StatesEnum.AVAILABLE),
                MoveStateData(MovesEnum.MIDDLE_CENTER, StatesEnum.AVAILABLE),
                MoveStateData(MovesEnum.MIDDLE_RIGHT, StatesEnum.AVAILABLE),
                MoveStateData(MovesEnum.BOTTOM_LEFT, StatesEnum.AVAILABLE),
                MoveStateData(MovesEnum.BOTTOM_CENTER, StatesEnum.AVAILABLE),
                MoveStateData(MovesEnum.BOTTOM_RIGHT, StatesEnum.AVAILABLE)
            )
        )
        this.availableMoves = availableMoves
    }
    fun resetBoard() {
        availableMoves = MoveListData(
            mutableListOf(
                MoveStateData(MovesEnum.TOP_LEFT, StatesEnum.AVAILABLE),
                MoveStateData(MovesEnum.TOP_CENTER, StatesEnum.AVAILABLE),
                MoveStateData(MovesEnum.TOP_RIGHT, StatesEnum.AVAILABLE),
                MoveStateData(MovesEnum.MIDDLE_LEFT, StatesEnum.AVAILABLE),
                MoveStateData(MovesEnum.MIDDLE_CENTER, StatesEnum.AVAILABLE),
                MoveStateData(MovesEnum.MIDDLE_RIGHT, StatesEnum.AVAILABLE),
                MoveStateData(MovesEnum.BOTTOM_LEFT, StatesEnum.AVAILABLE),
                MoveStateData(MovesEnum.BOTTOM_CENTER, StatesEnum.AVAILABLE),
                MoveStateData(MovesEnum.BOTTOM_RIGHT, StatesEnum.AVAILABLE)
            )
        )
    }
}