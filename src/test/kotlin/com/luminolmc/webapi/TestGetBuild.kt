package com.luminolmc.webapi

import com.luminolmc.webapi.v2.data.DatabaseManager
import com.luminolmc.webapi.v2.data.Struct

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

    val a = DatabaseManager.getBuild(
        "luminol",
        Struct.Version("1.20", "1.20.4")
    )

    println(a.toString())
}