package com.example.tictactoe.utilities.realm

import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlin.coroutines.CoroutineContext

class RealmManager(private val context: CoroutineContext) {
    private val realm: Realm

    init {
        val config = RealmConfiguration.create(schema = setOf(GameRecord::class))
        realm = Realm.open(config)
    }

    fun writeGameRecord(gameRecord: GameRecord) {
        realm.writeBlocking {
            val record = copyToRealm(gameRecord)
        }
        CoroutineScope(context).async {
            realm.write {
                val record = copyToRealm(gameRecord)
            }
        }
    }
    fun close(){
        realm.close()
    }
}