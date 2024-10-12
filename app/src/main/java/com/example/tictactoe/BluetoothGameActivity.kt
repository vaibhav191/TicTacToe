package com.example.tictactoe

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.tictactoe.ui.theme.TicTacToeTheme
import com.example.tictactoe.utilities.bluetooth.BluetoothGameManager
import com.example.tictactoe.utilities.bluetooth.GameData
import com.example.tictactoe.utilities.enums.MovesEnum
import kotlinx.coroutines.launch

class BluetoothGameActivity : ComponentActivity() {
    private lateinit var bluetoothGameManager: BluetoothGameManager

    @SuppressLint("HardwareIds")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bluetoothGameManager = BluetoothGameManager(this)

        val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
        val myMacAddress = bluetoothAdapter?.address ?: "Unknown"
        bluetoothGameManager.setMyMacAddress(myMacAddress)

        setContent {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                BluetoothGameScreen(
                    bluetoothGameManager = bluetoothGameManager,
                    myMacAddress = myMacAddress,
                    onBackPressed = { finish() }
                )
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        bluetoothGameManager.release()
    }
}

@Composable
fun BluetoothGameScreen(
    bluetoothGameManager: BluetoothGameManager,
    myMacAddress: String,
    onBackPressed: () -> Unit
) {
    val connectionState by bluetoothGameManager.connectionState.collectAsState()
    val gameData by bluetoothGameManager.gameData.collectAsState()
    val isMyTurn by bluetoothGameManager.isMyTurn.collectAsState()
    val scannedDevices by bluetoothGameManager.scannedDevices.collectAsState()
    val scope = rememberCoroutineScope()
    val reset = remember { mutableStateOf(false) }
    if (reset.value) {
        bluetoothGameManager.resetGame()
        reset.value = false
    }
    var showFirstPlayerDialog by remember { mutableStateOf(false) }
    var showGameEndDialog by remember { mutableStateOf(false) }

    LaunchedEffect(gameData.gameState) {
        if (gameData.gameState.winner != " " || gameData.gameState.draw) {
            showGameEndDialog = true
        }
    }
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            when (connectionState) {
                is BluetoothGameManager.ConnectionState.Connected -> {
                    if (gameData.metadata.miniGame.player1Choice.isEmpty() && gameData.metadata.miniGame.player2Choice.isEmpty()) {
                        FirstPlayerDialog(
                            onSelection = { isMeFirst ->
                                scope.launch {
                                    bluetoothGameManager.sendFirstPlayerChoice(isMeFirst)
                                }
                            }
                        )
                    } else if (!showGameEndDialog) {
                        GameBoard(
                            gameData = gameData,
                            myMacAddress = myMacAddress,
                            isMyTurn = isMyTurn,
                            onMakeMove = { move ->
                                if (isMyTurn) {
                                    scope.launch {
                                        bluetoothGameManager.sendMove(move)
                                    }
                                }
                            }
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Button(onClick = {
                                reset.value = true
                            }, modifier = Modifier.size(80.dp), colors = ButtonDefaults.buttonColors(
                                Color.Transparent)
                            )
                            {
                                //Image(painter = painterResource(id = R.drawable.undo_arrow), contentDescription = null, contentScale = ContentScale.Inside)
                            }
                        }
                    }
                }
                is BluetoothGameManager.ConnectionState.Disconnected -> {
                    Button(
                        onClick = {
                            scope.launch {
                                bluetoothGameManager.startServer()
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Host Game")
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { bluetoothGameManager.startDiscovery() },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Join Game")
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    LazyColumn {
                        items(scannedDevices) { device ->
                            DeviceListItem(device = device) {
                                scope.launch {
                                    bluetoothGameManager.connectToDevice(device)
                                }
                            }
                        }
                    }
                }
                is BluetoothGameManager.ConnectionState.Connecting -> {
                    CircularProgressIndicator()
                    Text("Connecting...")
                }
                is BluetoothGameManager.ConnectionState.Error -> {
                    Text("Error: ${(connectionState as BluetoothGameManager.ConnectionState.Error).message}")
                }
            }

            if (showGameEndDialog) {
                GameEndDialog(
                    gameData = gameData,
                    mySymbol = if (gameData.metadata.miniGame.player1Choice == myMacAddress) "X" else "O",
                    onPlayAgain = {
                        bluetoothGameManager.resetGame()
                        showGameEndDialog = false
                        showFirstPlayerDialog = true
                    },
                    onBackToMenu = onBackPressed,
                    isMyTurn = isMyTurn
                )
            }

            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = onBackPressed,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Back to Main Menu")
            }
        }
    }

}

@Composable
fun GameBoard(
    gameData: GameData,
    myMacAddress: String,
    isMyTurn: Boolean,
    onMakeMove: (MovesEnum) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        gameData.gameState.board.forEachIndexed { rowIndex, row ->
            Row {
                row.forEachIndexed { colIndex, cell ->
                    val move = MovesEnum.values().first { it.row == rowIndex && it.column == colIndex }
                    Button(
                        onClick = { onMakeMove(move) },
                        enabled = cell == " " && isMyTurn,
                        modifier = Modifier.size(60.dp).padding(4.dp)
                    ) {
                        Text(cell, style = MaterialTheme.typography.headlineMedium, color = Color.Black)
                    }
                }
            }
        }

        Text(
            text = if (isMyTurn) "Your turn" else "Opponent's turn",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(vertical = 8.dp)
        )
    }
}

@SuppressLint("MissingPermission")
@Composable
fun DeviceListItem(device: BluetoothDevice, onConnect: () -> Unit) {
    Button(
        onClick = onConnect,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Text(device.name ?: "Unknown Device")
    }
}

@Composable
fun FirstPlayerDialog(onSelection: (Boolean) -> Unit) {
    AlertDialog(
        onDismissRequest = { /* Do nothing, force a choice */ },
        title = { Text("Who Goes First?") },
        text = { Text("Choose who will make the first move.") },
        confirmButton = {
            Button(onClick = { onSelection(true) }) {
                Text("Me")
            }
        },
        dismissButton = {
            Button(onClick = { onSelection(false) }) {
                Text("Opponent")
            }
        }
    )
}

@Composable
fun GameEndDialog(
    gameData: GameData,
    mySymbol: String,
    onPlayAgain: () -> Unit,
    onBackToMenu: () -> Unit,
    isMyTurn: Boolean = false
) {
    AlertDialog(
        onDismissRequest = { },
        title = { Text("Game Over") },
        text = {
            Log.d("GameEndDialog", "mySymbol: $mySymbol")
            Log.d("GameEndDialog", "gameData.gameState.winner: ${gameData.gameState.winner}")
            Log.d("GameEndDialog", "isMyTurn: $isMyTurn")
            Text(
                when {
                    gameData.gameState.draw -> "It's a draw!"
                    !isMyTurn -> "You won!"
                    isMyTurn -> "You lost!"
                    else -> "Something is wrong!"
                }
            )
        },
        confirmButton = {
            Button(onClick = onPlayAgain) {
                Text("Play Again")
            }
        },
        dismissButton = {
            Button(onClick = onBackToMenu) {
                Text("Back to Menu")
            }
        }
    )
}