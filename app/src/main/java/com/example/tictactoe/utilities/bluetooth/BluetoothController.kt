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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import java.io.IOException
import java.util.*

class BluetoothController(private val context: Context) {
    private val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
    private var socket: BluetoothSocket? = null
    private var serverSocket: BluetoothServerSocket? = null

    private val _connectionState = MutableStateFlow<ConnectionState>(ConnectionState.Disconnected)
    val connectionState: StateFlow<ConnectionState> = _connectionState.asStateFlow()

    private val _scannedDevices = MutableStateFlow<List<BluetoothDevice>>(emptyList())
    val scannedDevices: StateFlow<List<BluetoothDevice>> = _scannedDevices.asStateFlow()

    private val _pairedDevices = MutableStateFlow<List<BluetoothDevice>>(emptyList())
    val pairedDevices: StateFlow<List<BluetoothDevice>> = _pairedDevices.asStateFlow()

    private val _incomingGameData = MutableSharedFlow<String>()
    val incomingGameData: SharedFlow<String> = _incomingGameData.asSharedFlow()

    private val foundDeviceReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when(intent?.action) {
                BluetoothDevice.ACTION_FOUND -> {
                    val device = intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
                    device?.let {
                        _scannedDevices.update { devices ->
                            if (it !in devices) devices + it else devices
                        }
                    }
                }
            }
        }
    }

    init {
        updatePairedDevices()
        val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
        context.registerReceiver(foundDeviceReceiver, filter)
    }

    @SuppressLint("MissingPermission")
    fun updatePairedDevices() {
        bluetoothAdapter?.bondedDevices?.let { devices ->
            _pairedDevices.update { devices.toList() }
        }
    }

    @SuppressLint("MissingPermission")
    suspend fun startServer() {
        withContext(Dispatchers.IO) {
            _connectionState.value = ConnectionState.Connecting
            serverSocket = bluetoothAdapter?.listenUsingInsecureRfcommWithServiceRecord("TicTacToeServer", UUID.fromString(SERVICE_UUID))
            var shouldLoop = true
            while (shouldLoop) {
                try {
                    socket = withTimeoutOrNull(30000) { // 30 seconds timeout
                        serverSocket?.accept()
                    }
                    socket?.let {
                        _connectionState.value = ConnectionState.Connected
                        serverSocket?.close()
                        shouldLoop = false
                        listenForGameData()
                    } ?: run {
                        _connectionState.value = ConnectionState.ConnectionFailed("Connection timed out")
                        shouldLoop = false
                    }
                } catch (e: IOException) {
                    _connectionState.value = ConnectionState.ConnectionFailed(e.message ?: "Unknown error occurred")
                    shouldLoop = false
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    suspend fun connectToDevice(device: BluetoothDevice) {
        withContext(Dispatchers.IO) {
            try {
                _connectionState.value = ConnectionState.Connecting
                socket = device.createInsecureRfcommSocketToServiceRecord(UUID.fromString(SERVICE_UUID))
                socket?.let { socket ->
                    bluetoothAdapter?.cancelDiscovery()
                    socket.connect()
                    _connectionState.value = ConnectionState.Connected
                    listenForGameData()
                } ?: throw IOException("Socket is null")
            } catch (e: IOException) {
                _connectionState.value = ConnectionState.ConnectionFailed(e.message ?: "Unknown error occurred")
                socket?.close()
                socket = null
            }
        }
    }

    private suspend fun listenForGameData() {
        withContext(Dispatchers.IO) {
            val buffer = ByteArray(1024)
            while (_connectionState.value == ConnectionState.Connected) {
                try {
                    val bytesRead = socket?.inputStream?.read(buffer)
                    bytesRead?.let {
                        if (it > 0) {
                            val jsonData = String(buffer, 0, it)
                            _incomingGameData.emit(jsonData)
                            Log.d("BluetoothController", "Received game data: $jsonData")
                        }
                    }
                } catch (e: IOException) {
                    _connectionState.value = ConnectionState.ConnectionFailed(e.message ?: "Connection lost")
                    break
                }
            }
        }
    }

    suspend fun sendGameData(jsonData: String) {
        withContext(Dispatchers.IO) {
            try {
                socket?.outputStream?.write(jsonData.toByteArray())
                Log.d("BluetoothController", "Game data sent: $jsonData")
            } catch (e: IOException) {
                _connectionState.value = ConnectionState.ConnectionFailed(e.message ?: "Failed to send game data")
            }
        }
    }

    suspend fun disconnect() {
        withContext(Dispatchers.IO) {
            socket?.close()
            serverSocket?.close()
            socket = null
            serverSocket = null
            _connectionState.value = ConnectionState.Disconnected
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

    suspend fun release() {
        context.unregisterReceiver(foundDeviceReceiver)
        stopDiscovery()
        disconnect()
    }

    sealed class ConnectionState {
        object Disconnected : ConnectionState()
        object Connecting : ConnectionState()
        object Connected : ConnectionState()
        data class ConnectionFailed(val message: String) : ConnectionState()
    }

    companion object {
        const val SERVICE_UUID = "00001101-0000-1000-8000-00805F9B34FB"
    }
}