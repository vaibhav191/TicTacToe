package com.example.tictactoe.utilities.realm

import com.example.tictactoe.utilities.enums.ConnectionTypeEnum
import com.example.tictactoe.utilities.enums.DifficultyEnum
import com.example.tictactoe.utilities.enums.GameResultEnum
import com.example.tictactoe.utilities.enums.LocalDifficultyEnum
import io.realm.kotlin.types.RealmInstant
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

class GameRecord: RealmObject {
    @PrimaryKey
    var id: ObjectId = ObjectId()
    var datetime: Long = System.currentTimeMillis()
    var difficulty: String = ""
    var connection: String = ""
    var opponent: String = ""
    var result: String = ""
}