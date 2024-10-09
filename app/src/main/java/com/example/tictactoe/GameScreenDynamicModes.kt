package com.example.tictactoe

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
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
import com.example.tictactoe.utilities.abstracts.GameMode
import com.example.tictactoe.utilities.enums.ConnectionTypeEnum
import com.example.tictactoe.utilities.enums.GameResultEnum
import com.example.tictactoe.utilities.enums.LocalDifficultyEnum
import com.example.tictactoe.utilities.enums.MovesEnum
import com.example.tictactoe.utilities.enums.PlayersEnum
import com.example.tictactoe.utilities.gamemodes.EasyMode
import com.example.tictactoe.utilities.gamemodes.HardMode
import com.example.tictactoe.utilities.gamemodes.MediumMode
import com.example.tictactoe.utilities.selector.GameModeSelector
import kotlinx.coroutines.delay

class GameScreenDynamicModes : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TicTacToeTheme {
                Log.d("GameScreen", "GameScreenDynamicModes")
                val difficulty = intent.getIntExtra("Difficulty", 0)
                val connection = intent.getIntExtra("Connection", 0)
                Log.d("GameScreen", "difficulty: $difficulty")
                Log.d("GameScreen", "connection: $connection")
                val game = GameModeSelector(
                    LocalDifficultyEnum.getDifficulty(difficulty)!!,
                    ConnectionTypeEnum.getConnectionType(connection)!!
                ).getGameMode()
                Log.d("GameScreen", "game: $game")
                var buttonStates = remember {mutableStateListOf<PlayersEnum?>(null, null, null, null, null, null, null, null, null)}
                var firstChangeStates = remember {mutableStateListOf<Boolean>(false, false, false, false, false, false, false, false, false)}
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = { topBar(Modifier) }) { innerPadding ->
                    BoardDy(
                        modifier = Modifier.padding(innerPadding),
                        game,
                        LocalDifficultyEnum.getDifficulty(difficulty)!!,
                        ConnectionTypeEnum.getConnectionType(connection)!!,
                        buttonStates,
                        firstChangeStates = firstChangeStates
                    )
                }
            }
        }
    }
}

@Composable
fun BoardDy(
    modifier: Modifier,
    game: GameMode,
    difficulty: LocalDifficultyEnum,
    connection: ConnectionTypeEnum,
    buttonStates: SnapshotStateList<PlayersEnum?>,
    firstChangeStates: SnapshotStateList<Boolean>
) {
    Column {
        Column(

            modifier = Modifier
                .padding(top = 100.dp, start = 50.dp, end = 50.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        )
        {
            // show connection type
            Text(
                text = connection.name,
                fontStyle = FontStyle.Italic,
                fontSize = 30.sp,
                fontWeight = FontWeight.SemiBold
            )
            // show difficulty
            Text(
                text = difficulty.name,
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
            // check if game is an instance of Easy mode, if so cast it to EasyMode
            val turnXState = remember { mutableStateOf(game.turn_X) }
            LaunchedEffect(turnXState.value) {
                while (true) {
                    if (game is EasyMode || game is MediumMode || game is HardMode) {
                        Log.d("GameScreen", "Game is an instance of EasyMode or MediumMode or HardMode")
                        if (!game.turn_X) {
                            Log.d("GameScreen", "Game is O turn")
                            var game = game as EasyMode
                            val aiMove = game.getMoveAI()
                            Log.d("GameScreen", "AI move: $aiMove")
//                            game.turn_X = true
                            buttonStates[aiMove.ordinal] = PlayersEnum.O
                            Log.d("GameScreen", "buttonStates changed: $buttonStates")
                        }
                        else {
                            Log.d("GameScreen", "Game is X turn")
                        }
                    }
                    delay(3000)
                }
            }
            Log.d("GameScreen", "Outside LaunchedEffect")

            // top row
            Row(
                modifier = Modifier
                    .weight(1f)
                    .background(Color.Black)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                tileDy(
                    Modifier
                        .weight(1f)
                        .padding(end = 5.dp, bottom = 5.dp),
                    game,
                    MovesEnum.TOP_LEFT,
                    buttonColor,
                    buttonElevation,
//                    buttonState = remember { mutableStateOf(buttonStates.value[0]) },
                    buttonState = buttonStates,
                    remember { mutableStateOf<GameResultEnum>(GameResultEnum.NotOver) },
                    firstChangeStates = firstChangeStates
                )
                tileDy(
                    Modifier
                        .weight(1f)
                        .padding(end = 5.dp, bottom = 5.dp),
                    game,
                    MovesEnum.TOP_CENTER,
                    buttonColor,
                    buttonElevation,
//                    buttonState = remember { mutableStateOf(buttonStates.value[1]) },
                    buttonState = buttonStates,
                    remember { mutableStateOf<GameResultEnum>(GameResultEnum.NotOver) },
                    firstChangeStates = firstChangeStates
                )
                tileDy(
                    Modifier
                        .weight(1f)
                        .padding(bottom = 5.dp),
                    game,
                    MovesEnum.TOP_RIGHT,
                    buttonColor,
                    buttonElevation,
//                    buttonState = remember { mutableStateOf(buttonStates.value[2]) },
                    buttonState = buttonStates,
                    remember { mutableStateOf<GameResultEnum>(GameResultEnum.NotOver) },
                    firstChangeStates = firstChangeStates
                )
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
                tileDy(
                    Modifier
                        .weight(1f)
                        .padding(end = 5.dp, bottom = 5.dp),
                    game,
                    MovesEnum.MIDDLE_LEFT,
                    buttonColor,
                    buttonElevation,
//                    buttonState = remember { mutableStateOf(buttonStates.value[3]) },
                    buttonState = buttonStates,
                    remember { mutableStateOf<GameResultEnum>(GameResultEnum.NotOver) },
                    firstChangeStates = firstChangeStates
                )
                tileDy(
                    Modifier
                        .weight(1f)
                        .padding(end = 5.dp, bottom = 5.dp),
                    game,
                    MovesEnum.MIDDLE_CENTER,
                    buttonColor,
                    buttonElevation,
//                    buttonState = remember { mutableStateOf(buttonStates.value[4]) },
                    buttonState = buttonStates,
                    remember { mutableStateOf<GameResultEnum>(GameResultEnum.NotOver) },
                    firstChangeStates = firstChangeStates
                )
                tileDy(
                    Modifier
                        .weight(1f)
                        .padding(bottom = 5.dp),
                    game,
                    MovesEnum.MIDDLE_RIGHT,
                    buttonColor,
                    buttonElevation,
//                    buttonState = remember { mutableStateOf(buttonStates.value[5]) },
                    buttonState = buttonStates,
                    remember { mutableStateOf<GameResultEnum>(GameResultEnum.NotOver) },
                    firstChangeStates = firstChangeStates
                )
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
                tileDy(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 5.dp),
                    game,
                    MovesEnum.BOTTOM_LEFT,
                    buttonColor,
                    buttonElevation,
//                    buttonState = remember { mutableStateOf(buttonStates.value[6]) },
                    buttonState = buttonStates,
                    remember { mutableStateOf<GameResultEnum>(GameResultEnum.NotOver) },
                    firstChangeStates = firstChangeStates
                )
                tileDy(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 5.dp),
                    game,
                    MovesEnum.BOTTOM_CENTER,
                    buttonColor,
                    buttonElevation,
//                    buttonState = remember { mutableStateOf(buttonStates.value[7]) },
                    buttonState = buttonStates,
                    remember { mutableStateOf<GameResultEnum>(GameResultEnum.NotOver) },
                    firstChangeStates = firstChangeStates
                )
                tileDy(
                    modifier = Modifier.weight(1f),
                    game,
                    MovesEnum.BOTTOM_RIGHT,
                    buttonColor,
                    buttonElevation,
//                    buttonState = remember { mutableStateOf(buttonStates.value[8]) },
                    buttonState = buttonStates,
                    remember { mutableStateOf<GameResultEnum>(GameResultEnum.NotOver) },
                    firstChangeStates = firstChangeStates
                )
            }
        }
    }
}


@Composable
fun renderMarkDy(playerType: PlayersEnum, modifier: Modifier) {
    if (playerType == PlayersEnum.X) {
        Log.d("GameScreen", "renderMarkDy called with X")
        Image(
            painter = painterResource(id = R.drawable.cross),
            contentDescription = null
        )
    } else {
        Log.d("GameScreen", "renderMarkDy called with O")
        Image(
            painter = painterResource(id = R.drawable.o),
            contentDescription = null
        )
    }
}

@Composable
fun tileDy(
    modifier: Modifier,
    game: GameMode,
    id: MovesEnum,
    buttonColor: Color,
    buttonElevation: Dp,
//    buttonState: MutableState<PlayersEnum?>,
    buttonState: SnapshotStateList<PlayersEnum?>,
    gameResult: MutableState<GameResultEnum>,
    context: Context = LocalContext.current,
    firstChangeStates: SnapshotStateList<Boolean>
) {
    var showDialog = remember { mutableStateOf<Boolean>(false) }
    Button(
        onClick = {
            if (game.turn_X) {
                buttonState[id.ordinal] = PlayersEnum.X
            } else {
                buttonState[id.ordinal] = PlayersEnum.O
            }
        },
        colors = ButtonDefaults.buttonColors(buttonColor),
        modifier = modifier
            .fillMaxSize(),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = buttonElevation),
        shape = RectangleShape
    ) {
        if (!firstChangeStates[id.ordinal] && buttonState[id.ordinal] != null) {
            Log.d("GameScreen", "tileDy called with id: $id")
            Log.d("GameScreen", "buttonState is not null")
            gameResult.value = game.move(id)
            Log.d("GameScreen", "gameResult: ${gameResult.value}")
            renderMarkDy(buttonState[id.ordinal]!!, Modifier)
//            game.turn_X = !game.turn_X
            Log.d("GameScreen", "Flipped game.turn_X: ${game.turn_X}")
            firstChangeStates[id.ordinal] = true
        }
        if (gameResult.value == GameResultEnum.Win || gameResult.value == GameResultEnum.Lose || gameResult.value == GameResultEnum.Draw) {
            showDialog.value = true
        }
        if (showDialog.value) {
            AlertDialog(
                onDismissRequest = { showDialog.value = false },
                title = {
                    Text("Game Over!")
                },
                text = {
                    val message = when (gameResult.value) {
                        GameResultEnum.Win -> {
                            "${game.playerX.playerName} won!"
                        }

                        GameResultEnum.Lose -> {
                            "${game.playerO.playerName} won!"
                        }

                        GameResultEnum.Draw -> {
                            "It's a draw!"
                        }

                        GameResultEnum.NotOver -> TODO()
                    }
                    Text(message)
                },
                confirmButton = {
                    Button(onClick = {
                        showDialog.value = false
                        val mainIntent = Intent(context, MainActivity::class.java)
                        context.startActivity(mainIntent)
                    }) {
                        Text("OK")
                    }
                }
            )
        }
    }
}