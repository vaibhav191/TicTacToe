package com.example.tictactoe

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tictactoe.utilities.GameHistory
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.ext.query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun fetchGameHistory(realm: Realm): List<GameHistory> {
    return withContext(Dispatchers.IO) {
        realm.query<GameHistory>().find()
    }
}

@Composable
fun GameHistoryScreen(innerPadding: PaddingValues) {
    val realm = Realm.open(RealmConfiguration.create(schema = setOf(GameHistory::class)))
    val gameHistoryList = remember { mutableStateListOf<GameHistory>() }

    LaunchedEffect(Unit) {
        val history = fetchGameHistory(realm)
        gameHistoryList.clear()
        gameHistoryList.addAll(history)
    }

    GameHistoryTable(gameHistoryList, innerPadding)
}

@Composable
fun GameHistoryTable(gameHistoryList: List<GameHistory>, innerPadding: PaddingValues) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
    ) {
        // Display the header with the total stats
        GameHistoryHeader(gameHistoryList)

        Spacer(modifier = Modifier.height(16.dp))

        // Scrollable list for the game history
        LazyColumn {
            items(gameHistoryList) { game ->
                GameHistoryRow(game)
            }
        }
    }
}

@Composable
fun GameHistoryHeader(gameHistoryList: List<GameHistory>) {
    // Calculate statistics
    val totalGames = gameHistoryList.size
    val totalWins = gameHistoryList.count { it.result == "Win" }
    val totalDraws = gameHistoryList.count { it.result == "Draw" }
    val totalLosses = gameHistoryList.count { it.result == "Loss" }

    val singlePlayerGames = gameHistoryList.filter { it.mode == "Single" }
    val multiplayerGames = gameHistoryList.filter { it.mode == "Multiplayer" }

    Column {
        Text("Total Games: $totalGames", fontSize = 18.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
        Text("Wins: $totalWins, Draws: $totalDraws, Losses: $totalLosses", fontSize = 16.sp)

        Spacer(modifier = Modifier.height(8.dp))

        // Breakdown by mode
        Text("Single Player Games: ${singlePlayerGames.size}", fontSize = 16.sp)
        Text("Multiplayer Games: ${multiplayerGames.size}", fontSize = 16.sp)
    }
}

@Composable
fun GameHistoryRow(game: GameHistory) {
    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("Date: ${game.date}", fontSize = 14.sp, color = Color.Black)
                Text("Time: ${game.time}", fontSize = 14.sp, color = Color.Black)
            }
            Column {
                Text("Opponent: ${game.opponent}", fontSize = 14.sp, color = Color.Black)
                Text("Result: ${game.result}", fontSize = 14.sp, color = Color.Black)
            }
            Text(
                text = if (game.mode == "Single") "Mode: ${game.mode} (${game.difficulty})"
                else "Mode: ${game.mode}",
                fontSize = 14.sp,
                color = Color.Black
            )
        }
    }
}