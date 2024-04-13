package com.luminolmc.webapi

import com.luminolmc.webapi.v2.data.DatabaseManager

fun main(args: Array<String>) {
    ConfigManager.loadConfig()

    ConfigManager.database.apply {
        DatabaseManager.connect(
            url = get("url")!!,
            username = get("username")!!,
            password = get("password")!!,
            dbname = get("dbname")!!
        )
    }

    val a = DatabaseManager.getSubVersion("luminol")
    println(a)
}