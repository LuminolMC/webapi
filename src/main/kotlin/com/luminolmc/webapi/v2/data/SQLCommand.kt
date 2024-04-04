package com.luminolmc.webapi.v2.data

enum class SQLCommand(private var command: String) {
    GET_VERSION(
        "SELECT * FROM"
    );

    override fun toString(): String {
        return this.command
    }
}