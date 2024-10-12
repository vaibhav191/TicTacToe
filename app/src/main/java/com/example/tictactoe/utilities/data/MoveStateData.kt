package com.example.tictactoe.utilities.data

import com.example.tictactoe.utilities.enums.MovesEnum
import com.example.tictactoe.utilities.enums.StatesEnum

// Data class representing the state of a move in the game
data class MoveStateData(val move: MovesEnum, var state: StatesEnum)
