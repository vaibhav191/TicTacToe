package com.example.tictactoe.utilities.enums

enum class ConnectionTypeEnum(val value: Int) {
    // Each enum value (connection type) has been associated with an integer for easier reference
    Local(0),
    Bluetooth(1);

    companion object {
        fun getConnectionType(value: Int): ConnectionTypeEnum? {
            return entries.find { it.value == value } // Returns null if no matching enum if found
        }
    }
}