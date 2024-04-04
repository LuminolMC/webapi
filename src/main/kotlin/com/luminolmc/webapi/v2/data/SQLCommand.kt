package com.luminolmc.webapi.v2.data

enum class SQLCommand(private var command: String) {
    GET_VERSION(
        "SELECT * FROM versions"
    ),
    GET_BUILD(
        "SELECT * FROM builds"
    );

    override fun toString(): String {
        return this.command
    }
}