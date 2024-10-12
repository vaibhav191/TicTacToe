package com.example.tictactoe.utilities.realm

import android.util.Log
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlin.coroutines.CoroutineContext

// A realm manager used to write game records to realm
// interface for writing to realm
class RealmManager(private val context: CoroutineContext) {
    private val realm: Realm

    init {
        // create a realm configuration
        val config = RealmConfiguration.create(schema = setOf(GameRecord::class))
        realm = Realm.open(config)
    }
    // use realm config to write a game record to realm
    fun writeGameRecord(gameRecord: GameRecord) {
        Log.d("RealmManager", "writeGameRecord")
        realm.writeBlocking {
            val record = copyToRealm(gameRecord)
        }
        CoroutineScope(context).async {
            realm.write {
                val record = copyToRealm(gameRecord)
            }
        }
    }
    // safely close realm
    fun close(){
        realm.close()
    }
}