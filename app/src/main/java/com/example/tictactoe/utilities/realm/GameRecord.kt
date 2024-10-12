package com.example.tictactoe.utilities.realm

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

// A game record consists of a unique id, datetime, difficulty, connection, opponent, and result
class GameRecord: RealmObject {
    @PrimaryKey
    var id: ObjectId = ObjectId()
    var datetime: Long = System.currentTimeMillis()
    var difficulty: String = ""
    var connection: String = ""
    var opponent: String = ""
    var result: String = ""
}