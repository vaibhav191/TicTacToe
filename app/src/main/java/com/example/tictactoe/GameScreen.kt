package com.example.tictactoe

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tictactoe.ui.theme.*
import com.example.tictactoe.utilities.enums.GameResultEnum
import com.example.tictactoe.utilities.enums.MovesEnum
import com.example.tictactoe.utilities.enums.PlayersEnum
import com.example.tictactoe.utilities.runners.SinglePlayerMode
import com.example.tictactoe.utilities.runners.TwoPlayerMode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class GameScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TicTacToeTheme {
                val isSinglePlayer = intent.getBooleanExtra("isSinglePlayer", true)
                val difficulty = intent.getStringExtra("difficulty") ?: "Easy"
                val game = if (isSinglePlayer) SinglePlayerMode(difficulty) else TwoPlayerMode()
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    GameScreenContent(game, isSinglePlayer, intent)
                }
            }
        }
    }
}

@Composable
fun GameScreenContent(game: Any, isSinglePlayer: Boolean, intent: Intent) {
    val context = LocalContext.current
    var gameResult by remember { mutableStateOf<GameResultEnum>(GameResultEnum.NotOver) }
    var showDialog by remember { mutableStateOf(false) }

    val tileStates = remember {
        mutableMapOf<MovesEnum, MutableState<PlayersEnum?>>().apply {
            MovesEnum.values().forEach { move ->
                this[move] = mutableStateOf(null)
            }
        }
    }

    val gradientBackground = Brush.verticalGradient(
        colors = listOf(GameGradientStart, GameGradientMiddle, GameGradientEnd)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradientBackground)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TopBar(if (isSinglePlayer) (game as SinglePlayerMode).difficulty else "Two Player")

            Spacer(modifier = Modifier.height(20.dp))

            Board(
                game = game,
                gameResult = { result ->
                    gameResult = result
                    if (result != GameResultEnum.NotOver) showDialog = true
                },
                tileStates = tileStates,
                isSinglePlayer = isSinglePlayer
            )
        }
    }

    if (showDialog) {
        GameOverDialog(
            gameResult = gameResult,
            onDismiss = { showDialog = false },
            context = context,
            isSinglePlayer = isSinglePlayer,
            intent = intent
        )
    }
}

@Composable
fun Board(
    game: Any,
    gameResult: (GameResultEnum) -> Unit,
    tileStates: MutableMap<MovesEnum, MutableState<PlayersEnum?>>,
    isSinglePlayer: Boolean
) {
    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .size(300.dp)
            .padding(16.dp)
    ) {
        // Draw the grid
        Canvas(modifier = Modifier.fillMaxSize()) {
            val strokeWidth = 4.dp.toPx()
            drawLine(
                color = GridColor,
                start = Offset(size.width / 3, 0f),
                end = Offset(size.width / 3, size.height),
                strokeWidth = strokeWidth
            )
            drawLine(
                color = GridColor,
                start = Offset(2 * size.width / 3, 0f),
                end = Offset(2 * size.width / 3, size.height),
                strokeWidth = strokeWidth
            )
            drawLine(
                color = GridColor,
                start = Offset(0f, size.height / 3),
                end = Offset(size.width, size.height / 3),
                strokeWidth = strokeWidth
            )
            drawLine(
                color = GridColor,
                start = Offset(0f, 2 * size.height / 3),
                end = Offset(size.width, 2 * size.height / 3),
                strokeWidth = strokeWidth
            )
        }

        // Create the grid of buttons
        Column(modifier = Modifier.fillMaxSize()) {
            for (i in 0..2) {
                Row(modifier = Modifier.weight(1f)) {
                    for (j in 0..2) {
                        val move = MovesEnum.values()[i * 3 + j]
                        Tile(
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f),
                            game = game,
                            id = move,
                            buttonState = tileStates[move]!!,
                            gameResult = gameResult,
                            tileStates = tileStates,
                            scope = scope,
                            isSinglePlayer = isSinglePlayer
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun Tile(
    modifier: Modifier,
    game: Any,
    id: MovesEnum,
    buttonState: MutableState<PlayersEnum?>,
    gameResult: (GameResultEnum) -> Unit,
    tileStates: MutableMap<MovesEnum, MutableState<PlayersEnum?>>,
    scope: CoroutineScope,
    isSinglePlayer: Boolean
) {
    val isEnabled = when {
        isSinglePlayer -> buttonState.value == null && (game as SinglePlayerMode).isPlayerTurn
        else -> buttonState.value == null
    }

    Button(
        onClick = {
            if (isEnabled) {
                if (isSinglePlayer) {
                    val singlePlayerGame = game as SinglePlayerMode
                    buttonState.value = PlayersEnum.X
                    val (result, aiMove) = singlePlayerGame.playerMove(id)
                    gameResult(result)
                    if (aiMove != null) {
                        scope.launch {
                            delay(1000L) // 1-second delay
                            tileStates[aiMove]?.value = PlayersEnum.O
                            gameResult(singlePlayerGame.game.checkWinner())
                        }
                    }
                } else {
                    val twoPlayerGame = game as TwoPlayerMode
                    val currentPlayer = if (twoPlayerGame.turn_X) PlayersEnum.X else PlayersEnum.O
                    buttonState.value = currentPlayer
                    val result = twoPlayerGame.move(id)
                    gameResult(result)
                }
            }
        },
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
        modifier = modifier.padding(2.dp),
        enabled = isEnabled
    ) {
        when (buttonState.value) {
            PlayersEnum.X -> Text("X", color = XColor, fontSize = 40.sp, fontWeight = FontWeight.Bold)
            PlayersEnum.O -> Text("O", color = OColor, fontSize = 40.sp, fontWeight = FontWeight.Bold)
            null -> Text("")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(title: String) {
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color.Transparent,
            titleContentColor = PrimaryTextColor,
        ),
        title = {
            Text(
                text = "Tic Tac Toe - $title",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        }
    )
}

@Composable
fun GameOverDialog(
    gameResult: GameResultEnum,
    onDismiss: () -> Unit,
    context: Context,
    isSinglePlayer: Boolean,
    intent: Intent
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = DialogBackgroundColor,
        titleContentColor = DialogTextColor,
        textContentColor = DialogTextColor,
        title = { Text("Game Over!") },
        text = {
            val message = when (gameResult) {
                GameResultEnum.Win -> if (isSinglePlayer) "You won!" else "Player X wins!"
                GameResultEnum.Lose -> if (isSinglePlayer) "Computer won!" else "Player O wins!"
                GameResultEnum.Draw -> "It's a draw!"
                GameResultEnum.NotOver -> ""
            }
            Text(message)
        },
        confirmButton = {
            Button(
                onClick = {
                    onDismiss()
                    val mainIntent = Intent(context, MainActivity::class.java)
                    context.startActivity(mainIntent)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = DialogButtonColor,
                    contentColor = ButtonContentColor
                )
            ) {
                Text("OK")
            }
        }
    )
}

