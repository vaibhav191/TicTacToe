package com.example.tictactoe.utilities.enums

// used to define the connection type between the device and the game
// Local means the player is playing locally on the device
// Bluetooth means the player is playing over Bluetooth
enum class ConnectionTypeEnum(val value: Int) {
    Local(0),
    Bluetooth(1);

    companion object {
        // returns the connection type based on the value
        fun getConnectionType(value: Int): ConnectionTypeEnum? {
            return entries.find { it.value == value }
        }
    }
}