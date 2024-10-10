package com.example.tictactoe.utilities

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class GameHistory : RealmObject {
    @PrimaryKey
    var id: Long = 0
    var date: String = "" // "MM/dd/yyyy" format
    var time: String = "" // 24-hour format
    var mode: String = "" // "Single" or "Multiplayer"
    var difficulty: String? = null // For single player: "Easy", "Medium", "Hard"
    var opponent: String = "" // "AI" or opponent's name
    var result: String = "" // "Win", "Loss", "Draw"
}

@RequiresApi(Build.VERSION_CODES.O)
suspend fun saveGameResult(
    realm: Realm,
    mode: String,
    difficulty: String?,
    opponent: String,
    result: String
) {
    withContext(Dispatchers.IO) {
        realm.write {
            // Query the maximum existing id
            val maxId = this.query<GameHistory>().max("id", Long::class).find()
            val newId = if (maxId != null) maxId + 1 else 1L

            // Create a new game history entry
            val newGame = GameHistory().apply {
                id = newId
                date = getDate()
                time = getTime()
                this.mode = mode
                this.difficulty = difficulty
                this.opponent = opponent
                this.result = result
            }

            // Save the new game in Realm
            copyToRealm(newGame)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
private fun getDate(): String {
    val current = LocalDateTime.now()

    val dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy")
    val formattedDate = current.format(dateFormatter)

    Log.d("Date", "Current Date: $formattedDate")

    return formattedDate
}

@RequiresApi(Build.VERSION_CODES.O)
private fun getTime(): String {
    val current = LocalDateTime.now()

    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
    val formattedTime = current.format(timeFormatter)

    Log.d("Time", "Current Time: $formattedTime")

    return formattedTime
}