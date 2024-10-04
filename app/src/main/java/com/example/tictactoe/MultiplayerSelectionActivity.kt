package com.example.tictactoe

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tictactoe.ui.theme.MultiplayerGradientEnd
import com.example.tictactoe.ui.theme.MultiplayerGradientStart
import com.example.tictactoe.ui.theme.PrimaryTextColor
import com.example.tictactoe.ui.theme.TicTacToeTheme

class MultiplayerSelectionActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            TicTacToeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MultiplayerSelectionScreen()
                }
            }
        }
    }
}

@Composable
fun MultiplayerSelectionScreen(context: Context = LocalContext.current) {
    val gradientBackground = Brush.verticalGradient(
        colors = listOf(MultiplayerGradientStart, MultiplayerGradientEnd)
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(gradientBackground),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Multiplayer Mode",
            fontWeight = FontWeight.Bold,
            fontSize = 32.sp,
            color = PrimaryTextColor,
            modifier = Modifier.padding(bottom = 60.dp)
        )

        Button(
            onClick = {
                val intent = Intent(context, GameScreen::class.java)
                intent.putExtra("isSinglePlayer", false)
                context.startActivity(intent)
            },
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = "Play on Same Device", fontSize = 20.sp)
        }

        Button(
            onClick = {
                // Placeholder for Bluetooth Multiplayer
            },
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = "Play via Bluetooth", fontSize = 20.sp)
        }
    }
}
