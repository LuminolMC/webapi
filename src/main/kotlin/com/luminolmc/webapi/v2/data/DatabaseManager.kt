package com.luminolmc.webapi.v2.data

import java.sql.Connection
import java.sql.DriverManager
import java.sql.Statement

object DatabaseManager {
    lateinit var conn: Connection
    lateinit var dbname: String


    fun connect(url: String, username: String, password: String, dbname: String) {
        conn = DriverManager.getConnection(url, username, password)
        this.dbname = dbname
    }

    fun getVersion(project: String): Map<String, String> {
        val statement = conn.createStatement()
        val resultSet = statement.executeQuery(SQLCommand.GET_VERSION.toString())
        val versions = emptyMap<String, String>().toMutableMap()

        while (resultSet.next()) {
            if (resultSet.getString("project") == project) {
                resultSet.apply {
                    val versionGroup = getString("version_group")
                    val version = getString("version")
                    versions[versionGroup] = version
                }
            }
        }
        return versions
    }

    fun getBuild(project: String, version: Struct.Version): List<Struct.Build> {
        val versionGroup = version.versionGroup
        val versionStr = version.version
        val statement = conn.createStatement()
        val resultSet = statement.executeQuery(SQLCommand.GET_BUILD.toString())
        val builds = emptyList<Struct.Build>().toMutableList()

        while (resultSet.next()) {
            if (resultSet.getString("version_group") != versionGroup
                || resultSet.getString("version_group") != version.versionGroup
                || resultSet.getString("version") != version.version)
                continue
            resultSet.apply {
                val build = Struct.Build(
                    buildId = getInt("id"),
                    time = getTimestamp("time").time,
                    jarName = getString("jar_name"),
                    sha256 = getString("sha256"),
                    summary = getString("summary"),
                    message = getString("message"),
                    version = Struct.Version(versionGroup, versionStr),
                    project = project
                )
                builds.add(build)
            }
        }

        return builds
    }
}