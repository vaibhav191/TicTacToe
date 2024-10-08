package com.example.tictactoe.utilities.enums

enum class ConnectionTypeEnum(val value: Int) {
    Local(0),
    Bluetooth(1);
    companion object {
        fun getConnectionType(value: Int): ConnectionTypeEnum? {
            return entries.find { it.value == value }
        }
    }
}