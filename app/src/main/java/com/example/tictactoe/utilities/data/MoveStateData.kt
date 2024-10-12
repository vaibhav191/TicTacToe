package com.example.tictactoe.utilities.data

import com.example.tictactoe.utilities.enums.MovesEnum
import com.example.tictactoe.utilities.enums.StatesEnum

data class MoveStateData(
    val move: MovesEnum,
    var state: StatesEnum
)
