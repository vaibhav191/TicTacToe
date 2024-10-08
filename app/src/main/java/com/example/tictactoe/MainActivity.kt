package com.example.tictactoe

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tictactoe.ui.theme.TicTacToeTheme
import com.example.tictactoe.utilities.enums.ModesEnum
import com.example.tictactoe.utilities.enums.SinglePlayerModesEnum

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TicTacToeTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = { topBar(Modifier) }) { innerPadding ->
                    main(modifier = Modifier.padding(innerPadding))

                }
            }
        }
    }
}

@Composable
fun main(modifier: Modifier, context: Context = LocalContext.current) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        // ready for a challenge
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize()
                .padding(top = 100.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Ready for a Challenge?!",
                fontWeight = FontWeight.Bold,
                fontSize = 30.sp,
                modifier = Modifier
            )
        }

        //card for single player
        Card(
            modifier = Modifier
                .padding(10.dp)
                .weight(0.7f),
            elevation = CardDefaults.cardElevation(),
            shape = CardDefaults.elevatedShape,
            colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface)

        ) {
            Column(
                modifier = Modifier,
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Slider
                val difficulty = slider(Modifier)
                Button(onClick = {
                    val gameintent = Intent(context, GameScreen::class.java)
                    gameintent.putExtra("mode", ModesEnum.SinglePlayer)
                    gameintent.putExtra("difficulty", difficulty)
                    context.startActivity(gameintent)
                }) {
                    Text(text = "Single Player vs AI")
                }
            }
        }
        // card for single screen multiplayer
        Column(modifier = Modifier.weight(1f)) {
            Button(onClick = { /*TODO*/ }, modifier = Modifier) {
                Text(text = "Two Player Mode")
            }
        }
        // card for multiplayer using bluetooth
        Column(modifier = Modifier.weight(1f)) {
            Button(onClick = { /*TODO*/ }, modifier = Modifier) {
                Text(text = "Multiplayer Bluetooth")
            }
        }
        // card for records
        Column(modifier = Modifier.weight(0.5f)) {
            Button(onClick = { /*TODO*/ }, modifier = Modifier) {
                Text(text = "Records")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun topBar(modifier: Modifier) {
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        title = {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(1.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.tic_tac_toe),
                    contentDescription = null,
                    modifier = Modifier.size(30.dp),
                )
                Text(
                    "marks the spot",
                    style = MaterialTheme.typography.headlineSmall,
                    fontStyle = FontStyle.Italic,
                    textDecoration = TextDecoration.None,
                    fontWeight = FontWeight.Light,
                )
            }
        }
    )
}

@Composable
fun slider(modifier: Modifier): SinglePlayerModesEnum {
    var difficulty by remember { mutableStateOf(SinglePlayerModesEnum.Easy) }
    var sliderPosition by remember { mutableFloatStateOf(0f) }
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier) {
        Slider(
            value = sliderPosition,
            onValueChange = { sliderPosition = it },
            colors = SliderDefaults.colors(
                thumbColor = MaterialTheme.colorScheme.secondary,
                activeTrackColor = MaterialTheme.colorScheme.secondary,
                inactiveTrackColor = MaterialTheme.colorScheme.secondaryContainer,
            ),

            steps = 1,
            valueRange = 0f..2f
        )
        difficulty = when (sliderPosition.toInt()) {
            0 -> SinglePlayerModesEnum.Easy
            1 -> SinglePlayerModesEnum.Medium
            else -> SinglePlayerModesEnum.Hard
        }
        Text(text = difficulty.toString())
    }
    return difficulty
}

