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
import com.example.tictactoe.ui.theme.TicTacToeTheme
import com.example.tictactoe.utilities.enums.GameResultEnum
import com.example.tictactoe.utilities.enums.MovesEnum
import com.example.tictactoe.utilities.enums.PlayersEnum
import com.example.tictactoe.utilities.modes.TwoPlayerMode

class GameScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TicTacToeTheme {
                val game = TwoPlayerMode()

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
    Column {
        Column(

            modifier = Modifier
                .padding(top = 100.dp, start = 50.dp, end = 50.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        )
        {
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

                tile(
                    Modifier
                        .weight(1f)
                        .padding(end = 5.dp, bottom = 5.dp),
                    game,
                    MovesEnum.TOP_LEFT,
                    buttonColor,
                    buttonElevation,
                    remember { mutableStateOf<PlayersEnum?>(null) },
                    remember { mutableStateOf<GameResultEnum>(GameResultEnum.NotOver) }
                )
                tile(
                    Modifier
                        .weight(1f)
                        .padding(end = 5.dp, bottom = 5.dp),
                    game,
                    MovesEnum.TOP_CENTER,
                    buttonColor,
                    buttonElevation,
                    remember { mutableStateOf<PlayersEnum?>(null) },
                    remember { mutableStateOf<GameResultEnum>(GameResultEnum.NotOver) }
                )
                tile(
                    Modifier
                        .weight(1f)
                        .padding(bottom = 5.dp),
                    game,
                    MovesEnum.TOP_RIGHT,
                    buttonColor,
                    buttonElevation,
                    remember { mutableStateOf<PlayersEnum?>(null) },
                    remember { mutableStateOf<GameResultEnum>(GameResultEnum.NotOver) }
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
                tile(
                    Modifier
                        .weight(1f)
                        .padding(end = 5.dp, bottom = 5.dp),
                    game,
                    MovesEnum.MIDDLE_LEFT,
                    buttonColor,
                    buttonElevation,
                    remember { mutableStateOf<PlayersEnum?>(null) },
                    remember { mutableStateOf<GameResultEnum>(GameResultEnum.NotOver) }
                )
                tile(
                    Modifier
                        .weight(1f)
                        .padding(end = 5.dp, bottom = 5.dp),
                    game,
                    MovesEnum.MIDDLE_CENTER,
                    buttonColor,
                    buttonElevation,
                    remember { mutableStateOf<PlayersEnum?>(null) },
                    remember { mutableStateOf<GameResultEnum>(GameResultEnum.NotOver) }
                )
                tile(
                    Modifier
                        .weight(1f)
                        .padding(bottom = 5.dp),
                    game,
                    MovesEnum.MIDDLE_RIGHT,
                    buttonColor,
                    buttonElevation,
                    remember { mutableStateOf<PlayersEnum?>(null) },
                    remember { mutableStateOf<GameResultEnum>(GameResultEnum.NotOver) }
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
                tile(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 5.dp),
                    game,
                    MovesEnum.BOTTOM_LEFT,
                    buttonColor,
                    buttonElevation,
                    remember { mutableStateOf<PlayersEnum?>(null) },
                    remember { mutableStateOf<GameResultEnum>(GameResultEnum.NotOver) }
                )
                tile(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 5.dp),
                    game,
                    MovesEnum.BOTTOM_CENTER,
                    buttonColor,
                    buttonElevation,
                    remember { mutableStateOf<PlayersEnum?>(null) },
                    remember { mutableStateOf<GameResultEnum>(GameResultEnum.NotOver) }
                )
                tile(
                    modifier = Modifier.weight(1f),
                    game,
                    MovesEnum.BOTTOM_RIGHT,
                    buttonColor,
                    buttonElevation,
                    remember { mutableStateOf<PlayersEnum?>(null) },
                    remember { mutableStateOf<GameResultEnum>(GameResultEnum.NotOver) }
                )
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
    context: Context = LocalContext.current,
) {
    var showDialog = remember { mutableStateOf<Boolean>(false) }
    Button(
        onClick = {
            if (game.turn_X){
                buttonState.value = PlayersEnum.X
            }
            else{
                buttonState.value = PlayersEnum.O
            }

        },
        colors = ButtonDefaults.buttonColors(buttonColor),
        modifier = modifier
            .fillMaxSize(),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = buttonElevation),
        shape = RectangleShape
    ) {
        if(buttonState.value != null){
            gameResult.value = game.move(id)
            renderMark(buttonState.value!!, Modifier)
        }
        if (gameResult.value == GameResultEnum.Win || gameResult.value == GameResultEnum.Lose || gameResult.value == GameResultEnum.Draw){
            showDialog.value = true
        }
        if (showDialog.value){
            AlertDialog(
                onDismissRequest = { showDialog.value = false },
                title = {
                    Text("Game Over!") },
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
                    Text(message) },
                confirmButton = {
                    Button(onClick = { showDialog.value = false
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