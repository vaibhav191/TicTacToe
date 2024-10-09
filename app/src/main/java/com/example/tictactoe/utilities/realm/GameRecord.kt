package com.example.tictactoe.utilities.realm

import com.example.tictactoe.utilities.enums.ConnectionTypeEnum
import com.example.tictactoe.utilities.enums.DifficultyEnum
import com.example.tictactoe.utilities.enums.GameResultEnum
import io.realm.kotlin.types.RealmInstant
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class GameRecord: RealmObject {
    @PrimaryKey
    var id: Long = 0
    var datetime: RealmInstant = RealmInstant.now()
    var difficulty: DifficultyEnum = DifficultyEnum.Easy
    var connection: ConnectionTypeEnum = ConnectionTypeEnum.Local
    var opponent: String = ""
    var result: GameResultEnum = GameResultEnum.NotOver
}