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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tictactoe.ui.theme.ButtonBackgroundColor
import com.example.tictactoe.ui.theme.ButtonContentColor
import com.example.tictactoe.ui.theme.DifficultyGradientEnd
import com.example.tictactoe.ui.theme.DifficultyGradientStart
import com.example.tictactoe.ui.theme.PrimaryTextColor
import com.example.tictactoe.ui.theme.SliderActiveColor
import com.example.tictactoe.ui.theme.SliderInactiveColor
import com.example.tictactoe.ui.theme.SliderThumbColor
import com.example.tictactoe.ui.theme.TicTacToeTheme

class DifficultySelectionActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TicTacToeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    DifficultySelectionScreen()
                }
            }
        }
    }
}

@Composable
fun DifficultySelectionScreen(context: Context = LocalContext.current) {
    val gradientBackground = Brush.verticalGradient(
        colors = listOf(DifficultyGradientStart, DifficultyGradientEnd)
    )

    var sliderPosition by remember { mutableFloatStateOf(0f) }
    var difficulty by remember { mutableStateOf("Easy") }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(gradientBackground),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Select Difficulty",
            fontWeight = FontWeight.Bold,
            fontSize = 32.sp,
            color = PrimaryTextColor,
            modifier = Modifier.padding(bottom = 50.dp)
        )

        Slider(
            value = sliderPosition,
            onValueChange = {
                sliderPosition = it
                difficulty = when (sliderPosition.toInt()) {
                    0 -> "Easy"
                    1 -> "Medium"
                    else -> "Hard"
                }
            },
            colors = SliderDefaults.colors(
                thumbColor = SliderThumbColor,
                activeTrackColor = SliderActiveColor,
                inactiveTrackColor = SliderInactiveColor,
            ),
            steps = 1,
            valueRange = 0f..2f,
            modifier = Modifier.padding(horizontal = 32.dp)
        )
        Text(
            text = difficulty,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = PrimaryTextColor
        )

        Button(
            onClick = {
                val gameIntent = Intent(context, GameScreen::class.java)
                gameIntent.putExtra("isSinglePlayer", true)
                gameIntent.putExtra("difficulty", difficulty)
                context.startActivity(gameIntent)
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = ButtonBackgroundColor,
                contentColor = ButtonContentColor
            ),
            modifier = Modifier.padding(top = 50.dp)
        ) {
            Text(text = "Start Game", fontSize = 20.sp)
        }
    }
}
