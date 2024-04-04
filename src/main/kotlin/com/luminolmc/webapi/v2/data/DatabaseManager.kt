package com.luminolmc.webapi.v2.data

import java.sql.Connection
import java.sql.DriverManager
import java.sql.Statement

object DatabaseManager {
    lateinit var conn: Connection
    lateinit var dbname: String
    lateinit var statement: Statement


    fun connect(url: String, username: String, password: String, dbname: String) {
        conn = DriverManager.getConnection(url, username, password)
        this.dbname = dbname
        this.statement = conn.createStatement()
    }

//    fun getVersion(project: String): List<String> {
//
//    }
}