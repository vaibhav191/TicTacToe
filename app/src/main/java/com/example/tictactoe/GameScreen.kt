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
import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.compose.ui.layout.ContentScale
import com.example.tictactoe.ui.theme.TicTacToeTheme
import com.example.tictactoe.utilities.enums.GameResultEnum
import com.example.tictactoe.utilities.enums.MovesEnum
import com.example.tictactoe.utilities.enums.PlayersEnum
import com.example.tictactoe.utilities.enums.StatesEnum
import com.example.tictactoe.utilities.runners.TwoPlayerMode
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.delay
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import androidx.compose.foundation.Canvas
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke



class GameScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Retrieve the difficulty level from the Intent
        val difficulty = intent.getStringExtra("difficulty") ?: "Easy"

        setContent {
            TicTacToeTheme {
                // Pass the difficulty level to TwoPlayerMode when initializing
                val game = TwoPlayerMode(difficulty)
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = { topBar(Modifier) }) { innerPadding ->
                    Board(modifier = Modifier.padding(innerPadding), game)
                }
            }
        }
    }
}

@Composable
fun Board(modifier: Modifier, game: TwoPlayerMode) {
    // Shared game state that all tiles can observe and react to.
    val gameState = remember { mutableStateOf(GameResultEnum.NotOver) }
    val isThinking = remember { mutableStateOf(false) }  // State to show thinking message
    val winningLine = remember { mutableStateOf<List<Int>?>(null) }  // State to store winning line indices

    // States for each tile (one per cell)
    val buttonStates = remember { Array(9) { mutableStateOf<PlayersEnum?>(null) } }

    Column {
        // Displaying the selected difficulty level.
        Column(
            modifier = Modifier
                .padding(top = 100.dp, start = 50.dp, end = 50.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = game.difficulty,
                fontStyle = FontStyle.Italic,
                fontSize = 30.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Show "AI is thinking" message when AI is making its move
        if (isThinking.value) {
            Text(
                text = "AI is thinking...",
                fontStyle = FontStyle.Italic,
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(20.dp))
        }

        // Displaying the Tic Tac Toe board.
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

            // Create the rows and columns for the board.
            val rows = listOf(
                listOf(MovesEnum.TOP_LEFT, MovesEnum.TOP_CENTER, MovesEnum.TOP_RIGHT),
                listOf(MovesEnum.MIDDLE_LEFT, MovesEnum.MIDDLE_CENTER, MovesEnum.MIDDLE_RIGHT),
                listOf(MovesEnum.BOTTOM_LEFT, MovesEnum.BOTTOM_CENTER, MovesEnum.BOTTOM_RIGHT)
            )

            var buttonIndex = 0
            rows.forEach { row ->
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    row.forEach { move ->
                        tile(
                            Modifier
                                .weight(1f)
                                .padding(5.dp),
                            game = game,
                            id = move,
                            buttonColor = buttonColor,
                            buttonElevation = buttonElevation,
                            buttonState = buttonStates[buttonIndex],  // Use shared button states
                            gameState = gameState,  // Pass the shared game state
                            buttonStates = buttonStates,  // Pass the buttonStates array
                            isThinking = isThinking,  // Pass the isThinking state
                            winningLine = winningLine  // Pass the winningLine state
                        )
                        buttonIndex++  // Increase index to track each tile separately
                    }
                }
            }

            // Draw the winning line over the board if the game is won
            winningLine.value?.let { indices ->
                DrawWinningLine(indices = indices)
            }
        }
    }


    // Monitor changes in the game state to update the winning line
    androidx.compose.runtime.LaunchedEffect(gameState.value) {
        if (gameState.value != GameResultEnum.NotOver) {
            winningLine.value = game.getWinningLine()  // Get the winning combination
        }
    }
}

@Composable
fun renderMark(playerType: PlayersEnum, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(
                id = when (playerType) {
                    PlayersEnum.X -> R.drawable.cross
                    PlayersEnum.O -> R.drawable.o
                }
            ),
            contentDescription = playerType.name,
            modifier = Modifier
                .fillMaxSize(0.6f)  // Use 60% of the available space
                .padding(4.dp),     // Add some padding
            contentScale = ContentScale.Fit
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
    gameState: MutableState<GameResultEnum>,
    buttonStates: Array<MutableState<PlayersEnum?>>,  // Add this parameter
    isThinking: MutableState<Boolean>,  // Add state to track AI thinking
    winningLine: MutableState<List<Int>?>,  // Track the winning line
    context: Context = LocalContext.current,
) {
    var showDialog = remember { mutableStateOf(false) }

    // Create a coroutine scope for handling delay in onClick
    val scope = rememberCoroutineScope()  // Remember a coroutine scope

    Button(
        onClick = {
            if (game.turn_X && buttonState.value == null) {
                // Player makes a move
                buttonState.value = PlayersEnum.X
                gameState.value = game.move(id)

                // Check if the game is not over after player's move
                if (gameState.value == GameResultEnum.NotOver) {
                    // Set thinking state to true before AI makes its move
                    isThinking.value = true

                    // Use the coroutine scope to handle delay in the click handler
                    scope.launch {
                        kotlinx.coroutines.delay(1000)  // 2-second delay to simulate AI thinking

                        // Let AI make its move
                        game.makeAIMove()

                        // Update button state to reflect AI's move in the UI
                        val aiMove = game.lastAIMove
                        if (aiMove != null) {
                            val aiMoveIndex = aiMove.moveIndex
                            buttonStates[aiMoveIndex].value = PlayersEnum.O
                        }

                        // Update game state after AI move
                        gameState.value = game.checkWinner()

                        // After AI move is complete, set thinking to false
                        isThinking.value = false
                    }
                }
            }
        },
        enabled = buttonState.value == null && game.turn_X && !isThinking.value,  // Disable if AI is thinking
        colors = ButtonDefaults.buttonColors(
            containerColor = buttonColor,
            disabledContainerColor = buttonColor
        ),
        modifier = modifier.fillMaxSize(),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = buttonElevation),
        shape = RectangleShape
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            buttonState.value?.let { playerType ->
                renderMark(
                    playerType = playerType,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }

    // Handle game over dialog
    if (gameState.value != GameResultEnum.NotOver) {
        showDialog.value = true
    }

    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = { showDialog.value = false },
            title = { Text("Game Over!") },
            text = {
                val message = when (gameState.value) {
                    GameResultEnum.Win -> "Player X won!"
                    GameResultEnum.Lose -> "Player O won!"
                    GameResultEnum.Draw -> "It's a draw!"
                    else -> ""
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

@Composable
fun DrawWinningLine(
    indices: List<Int>, // The indices of the winning combination
    modifier: Modifier = Modifier,
    lineColor: Color = Color.Red // Set the color of the winning line
) {
    Canvas(modifier = modifier.fillMaxSize()) {
        // Define the start and end positions based on the indices
        val (startX, startY) = getOffsetForIndex(indices.first())
        val (endX, endY) = getOffsetForIndex(indices.last())

        // Draw a line to represent the winning combination
        drawLine(
            color = lineColor,
            start = Offset(startX, startY),
            end = Offset(endX, endY),
            strokeWidth = 8f // Adjust stroke width as needed
        )
    }
}

// Helper function to get the Offset (X, Y) position for a given index
fun getOffsetForIndex(index: Int): Pair<Float, Float> {
    // Replace these with actual cell size and positions based on your board layout
    val row = index / 3
    val col = index % 3
    val cellSize = 100f // Set to your board's cell size (modify as needed)

    // Calculate the center of the cell for the given index
    val x = col * cellSize + cellSize / 2
    val y = row * cellSize + cellSize / 2

    return Pair(x, y)
}



