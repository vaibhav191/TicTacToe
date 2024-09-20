package com.example.tictactoe.utilities

class Board {
    private var consumedYMoves: MoveList
    private var consumedXMoves: MoveList
    private var availableMoves: MoveList

    init {
        // setting up the moves
        var availableMoves = MoveList(mutableListOf(
            MoveState(MovesEnum.TOP_LEFT, StatesEnum.AVAILABLE),
            MoveState(MovesEnum.TOP_CENTER, StatesEnum.AVAILABLE),
            MoveState(MovesEnum.TOP_RIGHT, StatesEnum.AVAILABLE),
            MoveState(MovesEnum.MIDDLE_LEFT, StatesEnum.AVAILABLE),
            MoveState(MovesEnum.MIDDLE_CENTER, StatesEnum.AVAILABLE),
            MoveState(MovesEnum.MIDDLE_RIGHT, StatesEnum.AVAILABLE),
            MoveState(MovesEnum.BOTTOM_LEFT, StatesEnum.AVAILABLE),
            MoveState(MovesEnum.BOTTOM_CENTER, StatesEnum.AVAILABLE),
            MoveState(MovesEnum.BOTTOM_RIGHT, StatesEnum.AVAILABLE)
        ))
        var consumedXMoves = availableMoves.copy()
        var consumedYMoves = availableMoves.copy()

        this.availableMoves = availableMoves
        this.consumedXMoves = consumedXMoves
        this.consumedYMoves = consumedYMoves

        // setting up players
        // idea - create a player class, gets PlayersEnum
        // Player class initialized by getting Board as a parameter, and then it can move
        
    }
}