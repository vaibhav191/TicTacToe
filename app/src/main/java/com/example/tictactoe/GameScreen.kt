package com.example.tictactoe

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tictactoe.ui.theme.TicTacToeTheme
import com.example.tictactoe.utilities.enums.GameResultEnum
import com.example.tictactoe.utilities.enums.MovesEnum
import com.example.tictactoe.utilities.enums.PlayersEnum
import com.example.tictactoe.utilities.runners.TwoPlayerMode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class GameScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TicTacToeTheme {
                val game = TwoPlayerMode()
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = { topBar(Modifier) }
                ) { innerPadding ->
                    Board(modifier = Modifier.padding(innerPadding), game)
                }
            }
        }
    }
}

@Composable
fun Board(modifier: Modifier, game: TwoPlayerMode) {
    val scope = rememberCoroutineScope()
    val turn_X by game.turn_X.collectAsState()

    Column(modifier = modifier) {
        Column(
            modifier = Modifier
                .padding(top = 100.dp, start = 50.dp, end = 50.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "Hard",
                fontStyle = FontStyle.Italic,
                fontSize = 30.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
        Spacer(modifier = Modifier.height(50.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(500.dp)
                .clip(RoundedCornerShape(40.dp))
                .padding(start = 20.dp, end = 20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            val buttonColor = MaterialTheme.colorScheme.primaryContainer
            val buttonElevation = 10.dp
            // top row
            Row(
                modifier = Modifier
                    .weight(1f)
                    .background(Color.Black)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                for (move in listOf(MovesEnum.TOP_LEFT, MovesEnum.TOP_CENTER, MovesEnum.TOP_RIGHT)) {
                    tile(
                        Modifier
                            .weight(1f)
                            .padding(end = if (move != MovesEnum.TOP_RIGHT) 5.dp else 0.dp, bottom = 5.dp),
                        game,
                        move,
                        buttonColor,
                        buttonElevation,
                        remember { mutableStateOf<PlayersEnum?>(null) },
                        remember { mutableStateOf<GameResultEnum>(GameResultEnum.NotOver) },
                        scope
                    )
                }
            }
            // middle row
            Row(
                modifier = Modifier
                    .weight(1f)
                    .background(Color.Black)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                for (move in listOf(MovesEnum.MIDDLE_LEFT, MovesEnum.MIDDLE_CENTER, MovesEnum.MIDDLE_RIGHT)) {
                    tile(
                        Modifier
                            .weight(1f)
                            .padding(end = if (move != MovesEnum.MIDDLE_RIGHT) 5.dp else 0.dp, bottom = 5.dp),
                        game,
                        move,
                        buttonColor,
                        buttonElevation,
                        remember { mutableStateOf<PlayersEnum?>(null) },
                        remember { mutableStateOf<GameResultEnum>(GameResultEnum.NotOver) },
                        scope
                    )
                }
            }
            // bottom row
            Row(
                modifier = Modifier
                    .weight(1f)
                    .background(Color.Black)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                for (move in listOf(MovesEnum.BOTTOM_LEFT, MovesEnum.BOTTOM_CENTER, MovesEnum.BOTTOM_RIGHT)) {
                    tile(
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = if (move != MovesEnum.BOTTOM_RIGHT) 5.dp else 0.dp),
                        game,
                        move,
                        buttonColor,
                        buttonElevation,
                        remember { mutableStateOf<PlayersEnum?>(null) },
                        remember { mutableStateOf<GameResultEnum>(GameResultEnum.NotOver) },
                        scope
                    )
                }
            }
        }
    }
}

@Composable
fun renderMark(playerType: PlayersEnum, modifier: Modifier) {
    if (playerType == PlayersEnum.X) {
        Image(
            painter = painterResource(id = R.drawable.cross),
            contentDescription = null
        )
    } else {
        Image(
            painter = painterResource(id = R.drawable.o),
            contentDescription = null
        )
    }
}

@Composable
fun tile(
    modifier: Modifier,
    game: TwoPlayerMode,
    id: MovesEnum,
    buttonColor: Color,
    buttonElevation: Dp,
    buttonState: MutableState<PlayersEnum?>,
    gameResult: MutableState<GameResultEnum>,
    scope: CoroutineScope,
    context: Context = LocalContext.current,
) {
    var showDialog by remember { mutableStateOf(false) }
    val turn_X by game.turn_X.collectAsState()

    Button(
        onClick = {
            if (game.canMakeMove(id)) {
                buttonState.value = if (turn_X) PlayersEnum.X else PlayersEnum.O
                scope.launch {
                    val result = game.move(id)
                    gameResult.value = result
                    if (result != GameResultEnum.NotOver) {
                        showDialog = true
                    }
                }
            }
        },
        colors = ButtonDefaults.buttonColors(buttonColor),
        modifier = modifier.fillMaxSize(),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = buttonElevation),
        shape = RectangleShape,
        enabled = game.canMakeMove(id)
    ) {
        buttonState.value?.let { renderMark(it, Modifier) }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Game Over!") },
            text = {
                val message = when (gameResult.value) {
                    GameResultEnum.Win -> "${if (turn_X) game.playerO.playerName else game.playerX.playerName} won!"
                    GameResultEnum.Lose -> "${if (turn_X) game.playerX.playerName else game.playerO.playerName} won!"
                    GameResultEnum.Draw -> "It's a draw!"
                    GameResultEnum.NotOver -> "" // This case shouldn't occur here
                }
                Text(message)
            },
            confirmButton = {
                Button(onClick = {
                    showDialog = false
                    val mainIntent = Intent(context, MainActivity::class.java)
                    context.startActivity(mainIntent)
                }) {
                    Text("OK")
                }
            }
        )
    }
}