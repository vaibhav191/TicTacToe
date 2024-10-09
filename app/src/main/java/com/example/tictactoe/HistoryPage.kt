package com.example.tictactoe

import android.inputmethodservice.Keyboard
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tictactoe.ui.theme.TicTacToeTheme
import com.example.tictactoe.utilities.realm.GameRecord
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll


class HistoryPage : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TicTacToeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    History(modifier = Modifier.padding(innerPadding))

                }
            }
        }
    }
}

@Composable
fun History(modifier: Modifier = Modifier) {
    val config = RealmConfiguration.create(schema = setOf(GameRecord::class))
    val realm = Realm.open(config)
    val records = realm.query(GameRecord::class).find()
    Row(modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState())) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize().verticalScroll(rememberScrollState())
        ) {
            // Table Header
            Row(
                Modifier
                    .fillMaxWidth()
            ) {
                TableCell(text = "ID", height = 50, width = 100, backgroundColor = Color.LightGray)
                TableCell(text = "Datetime", height = 50, width = 100, backgroundColor = Color.LightGray)
                TableCell(text = "connection", height = 50, width = 100, backgroundColor = Color.LightGray)
                TableCell(text = "difficulty", height = 50, width = 100, backgroundColor = Color.LightGray)
                TableCell(text = "opponent", height = 50, width = 100, backgroundColor = Color.LightGray)
                TableCell(text = "result", height = 50, width = 100, backgroundColor = Color.LightGray)
            }
            Log.d("HistoryPage", "Table header row added")
            // Table Rows
            records.forEach { record ->
                Row(
                ) {
                    TableCell(text = record.id.toString(), height = 50, width = 100)
                    TableCell(
                        text = record.datetime.toString(),
                        height = 50,
                        width = 100
                    )
                    TableCell(
                        text = record.connection,
                        height = 50,
                        width = 100
                    )
                    TableCell(
                        text = record.difficulty,
                        height = 50,
                        width = 100
                    )
                    TableCell(
                        text = record.opponent,
                        height = 50,
                        width = 100
                    )
                    TableCell(
                        text = record.result,
                        height = 50,
                        width = 100
                    )
                }
            }
            Log.d("HistoryPage", "Table rows added")
        }
        Log.d("HistoryPage", "Realm closing")
        realm.close()

    }
}
@Composable
fun RowScope.TableCell(
    text: String,
    modifier: Modifier = Modifier,
    height: Int,
    width: Int,
    color: Color = Color.White,
    backgroundColor: Color = Color.White
) {
    Text(
        text = text,
        Modifier
            .border(1.dp, Color.Black)
            .width(width.dp)
            .height(height.dp).background(backgroundColor),
        softWrap = true,
    )
}