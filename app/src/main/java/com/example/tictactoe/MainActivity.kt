package com.example.tictactoe

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tictactoe.ui.theme.MainGradientEnd
import com.example.tictactoe.ui.theme.MainGradientStart
import com.example.tictactoe.ui.theme.PrimaryTextColor
import com.example.tictactoe.ui.theme.TicTacToeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TicTacToeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen()
                }
            }
        }
    }
}

@Composable
fun MainScreen(context: Context = LocalContext.current) {
    val gradientBackground = Brush.verticalGradient(
        colors = listOf(MainGradientStart, MainGradientEnd)
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(gradientBackground),
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.tic_tac_toe), // Ensure the drawable exists
            contentDescription = "Tic Tac Toe Logo",
            modifier = Modifier.padding(bottom = 50.dp)
        )

        Text(
            text = "Welcome to Tic Tac Toe!",
            fontWeight = FontWeight.Bold,
            fontSize = 32.sp,
            color = PrimaryTextColor,
            modifier = Modifier.padding(bottom = 60.dp)
        )

        Button(
            onClick = {
                val intent = Intent(context, DifficultySelectionActivity::class.java)
                context.startActivity(intent)
            },
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = "Single Player", fontSize = 20.sp)
        }

        Button(
            onClick = {
                val intent = Intent(context, MultiplayerSelectionActivity::class.java)
                context.startActivity(intent)
            },
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = "Multiplayer", fontSize = 20.sp)
        }
    }
}
