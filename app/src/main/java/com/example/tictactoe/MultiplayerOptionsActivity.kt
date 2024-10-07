package com.example.tictactoe

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.tictactoe.ui.theme.TicTacToeTheme

class MultiplayerOptionsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TicTacToeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MultiplayerOptionsScreen(
                        onOnDevicePlayClicked = {
                            // Start on-device multiplayer game
                            // You'll need to implement this
                        },
                        onTwoDevicePlayClicked = {
                            val intent = Intent(this, BluetoothGameActivity::class.java)
                            startActivity(intent)
                        },
                        onBackPressed = { finish() }
                    )
                }
            }
        }
    }
}

@Composable
fun MultiplayerOptionsScreen(
    onOnDevicePlayClicked: () -> Unit,
    onTwoDevicePlayClicked: () -> Unit,
    onBackPressed: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = onOnDevicePlayClicked,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text("On-device Play")
        }
        Button(
            onClick = onTwoDevicePlayClicked,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text("Two-device Play")
        }
        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = onBackPressed,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text("Back")
        }
    }
}