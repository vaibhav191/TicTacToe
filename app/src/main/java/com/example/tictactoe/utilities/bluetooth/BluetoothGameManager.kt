package com.example.tictactoe.utilities.bluetooth

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothServerSocket
import android.bluetooth.BluetoothSocket
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import com.example.tictactoe.utilities.enums.MovesEnum
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.IOException
import java.util.*

class BluetoothGameManager(private val context: Context) {
    private val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
    private var socket: BluetoothSocket? = null
    private var serverSocket: BluetoothServerSocket? = null

    private val _connectionState = MutableStateFlow<ConnectionState>(ConnectionState.Disconnected)
    val connectionState: StateFlow<ConnectionState> = _connectionState

    private val _scannedDevices = MutableStateFlow<List<BluetoothDevice>>(emptyList())
    val scannedDevices: StateFlow<List<BluetoothDevice>> = _scannedDevices

    private val _gameData = MutableStateFlow(GameData())
    val gameData: StateFlow<GameData> = _gameData

    private val _isMyTurn = MutableStateFlow(false)
    val isMyTurn: StateFlow<Boolean> = _isMyTurn

    private var previousTurn: Int = -1

    private val json = Json { ignoreUnknownKeys = true }

    private var myMacAddress: String = ""

    fun setMyMacAddress(address: String) {
        myMacAddress = address
        _gameData.value = _gameData.value.copy(
            metadata = Metadata(
                choices = listOf(
                    Choice("player1", myMacAddress),
                    Choice("player2", "")
                ),
                miniGame = MiniGame()
            )
        )
    }

    private val receiver = object : BroadcastReceiver() {
        @SuppressLint("MissingPermission")
        override fun onReceive(context: Context?, intent: Intent?) {
            when(intent?.action) {
                BluetoothDevice.ACTION_FOUND -> {
                    val device: BluetoothDevice? = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                    device?.let {
                        _scannedDevices.value += it
                    }
                }
            }
        }
    }

    init {
        val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
        context.registerReceiver(receiver, filter)
    }

    @SuppressLint("MissingPermission")
    suspend fun startServer() {
        withContext(Dispatchers.IO) {
            try {
                _connectionState.value = ConnectionState.Connecting
                serverSocket = bluetoothAdapter?.listenUsingRfcommWithServiceRecord("TicTacToeServer", UUID.fromString(SERVICE_UUID))
                socket = serverSocket?.accept()
                _connectionState.value = ConnectionState.Connected
                listenForIncomingData()
            } catch (e: IOException) {
                _connectionState.value = ConnectionState.Error("Failed to start server: ${e.message}")
            }
        }
    }

    @SuppressLint("MissingPermission")
    suspend fun connectToDevice(device: BluetoothDevice) {
        withContext(Dispatchers.IO) {
            try {
                _connectionState.value = ConnectionState.Connecting
                socket = device.createRfcommSocketToServiceRecord(UUID.fromString(SERVICE_UUID))
                socket?.connect()
                _connectionState.value = ConnectionState.Connected
                listenForIncomingData()
            } catch (e: IOException) {
                _connectionState.value = ConnectionState.Error("Failed to connect: ${e.message}")
            }
        }
    }

    @SuppressLint("MissingPermission")
    fun startDiscovery() {
        _scannedDevices.value = emptyList()
        bluetoothAdapter?.startDiscovery()
    }

    @SuppressLint("MissingPermission")
    fun stopDiscovery() {
        bluetoothAdapter?.cancelDiscovery()
    }

    suspend fun sendFirstPlayerChoice(isMeFirst: Boolean) {
        val updatedGameData = _gameData.value.copy(
            metadata = _gameData.value.metadata.copy(
                miniGame = MiniGame(
                    player1Choice = if (isMeFirst) myMacAddress else "",
                    player2Choice = if (!isMeFirst) myMacAddress else ""
                )
            ),
            gameState = _gameData.value.gameState.copy(
                turn = "0"
            )
        )
        _gameData.value = updatedGameData
        _isMyTurn.value = isMeFirst
        previousTurn = -1
        sendGameData(updatedGameData)
        Log.d("BluetoothGameManager", "First player choice sent. isMeFirst: $isMeFirst, isMyTurn: ${_isMyTurn.value}")
    }

    suspend fun sendMove(move: MovesEnum) {
        val currentGameData = _gameData.value
        val newBoard = currentGameData.gameState.board.mapIndexed { r, rowList ->
            rowList.mapIndexed { c, cell ->
                if (r == move.row && c == move.column && cell == " ") {
                    if (currentGameData.gameState.turn.toInt() % 2 == 0) "X" else "O"
                } else cell
            }
        }
        val newTurn = (currentGameData.gameState.turn.toInt() + 1).toString()

        val (winner, isDraw) = checkWinOrDraw(newBoard)

        val updatedGameData = currentGameData.copy(
            gameState = currentGameData.gameState.copy(
                board = newBoard,
                turn = newTurn,
                winner = winner,
                draw = isDraw
            )
        )
        _gameData.value = updatedGameData
        previousTurn = currentGameData.gameState.turn.toInt()
        _isMyTurn.value = false  // After sending a move, it's not my turn
        sendGameData(updatedGameData)
        Log.d("BluetoothGameManager", "Move sent. Current turn: $newTurn, isMyTurn: ${_isMyTurn.value}, Winner: $winner, Draw: $isDraw")
    }

    private suspend fun sendGameData(gameData: GameData) {
        withContext(Dispatchers.IO) {
            try {
                val jsonData = json.encodeToString(gameData)
                socket?.outputStream?.write(jsonData.toByteArray())
                Log.d("BluetoothGameManager", "Data sent: $jsonData")
            } catch (e: IOException) {
                _connectionState.value = ConnectionState.Error("Failed to send data: ${e.message}")
            }
        }
    }

    private suspend fun listenForIncomingData() {
        withContext(Dispatchers.IO) {
            val buffer = ByteArray(1024)
            while (_connectionState.value is ConnectionState.Connected) {
                try {
                    val bytesRead = socket?.inputStream?.read(buffer)
                    bytesRead?.let {
                        if (it > 0) {
                            val receivedMessage = String(buffer, 0, it)
                            handleIncomingMessage(receivedMessage)
                        }
                    }
                } catch (e: IOException) {
                    _connectionState.value = ConnectionState.Error("Connection lost: ${e.message}")
                    break
                }
            }
        }
    }

    private fun handleIncomingMessage(message: String) {
        try {
            val receivedGameData = Json.decodeFromString<GameData>(message)
            val currentTurn = receivedGameData.gameState.turn.toInt()

            if (currentTurn != previousTurn) {
                // This is a new move, so it's now my turn
                _isMyTurn.value = true
                previousTurn = currentTurn
            }

            _gameData.value = receivedGameData

            Log.d("BluetoothGameManager", "Received game data: $receivedGameData")
            Log.d("BluetoothGameManager", "Updated isMyTurn: ${_isMyTurn.value}")
        } catch (e: Exception) {
            Log.e("BluetoothGameManager", "Error parsing message: ${e.message}")
        }
    }

    private fun determineIsMyTurn(gameData: GameData): Boolean {
        val firstPlayerMac = gameData.metadata.miniGame.player1Choice
        val currentTurn = gameData.gameState.turn.toInt()

        return if (myMacAddress == firstPlayerMac) {
            currentTurn % 2 == 0
        } else {
            currentTurn % 2 == 1
        }
    }

    fun setDeviceChoices(myMacAddress: String, opponentMacAddress: String) {
        _gameData.value = _gameData.value.copy(
            metadata = Metadata(
                choices = listOf(
                    Choice("player1", myMacAddress),
                    Choice("player2", opponentMacAddress)
                ),
                miniGame = MiniGame()
            )
        )
    }

    fun release() {
        context.unregisterReceiver(receiver)
        stopDiscovery()
        socket?.close()
        serverSocket?.close()
    }

    private fun checkWinOrDraw(board: List<List<String>>): Pair<String, Boolean> {
        // Check rows, columns, and diagonals
        for (i in 0..2) {
            if (board[i][0] != " " && board[i][0] == board[i][1] && board[i][1] == board[i][2]) {
                return Pair(board[i][0], false) // Row win
            }
            if (board[0][i] != " " && board[0][i] == board[1][i] && board[1][i] == board[2][i]) {
                return Pair(board[0][i], false) // Column win
            }
        }
        if (board[0][0] != " " && board[0][0] == board[1][1] && board[1][1] == board[2][2]) {
            return Pair(board[0][0], false) // Diagonal win
        }
        if (board[0][2] != " " && board[0][2] == board[1][1] && board[1][1] == board[2][0]) {
            return Pair(board[0][2], false) // Diagonal win
        }

        // Check for draw
        if (board.all { row -> row.all { it != " " } }) {
            return Pair(" ", true) // Draw
        }

        return Pair(" ", false) // Game still ongoing
    }

    fun resetGame() {
        _gameData.value = _gameData.value.copy(
            gameState = GameState(
                board = List(3) { List(3) { " " } },
                turn = "0",
                winner = " ",
                draw = false,
                reset = true
            ),
            metadata = _gameData.value.metadata.copy(
                miniGame = MiniGame()
            )
        )
        previousTurn = -1
        _isMyTurn.value = false
    }

    sealed class ConnectionState {
        object Disconnected : ConnectionState()
        object Connecting : ConnectionState()
        object Connected : ConnectionState()
        data class Error(val message: String) : ConnectionState()
    }

    companion object {
        private const val SERVICE_UUID = "00001101-0000-1000-8000-00805F9B34FB"
    }
}