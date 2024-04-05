package com.luminolmc.webapi.v2.data

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import io.ktor.http.ContentType.Application.Json
import java.sql.Connection
import java.sql.DriverManager
import java.sql.Statement
import java.sql.Timestamp

object DatabaseManager {
    lateinit var conn: Connection
    lateinit var dbname: String


    fun connect(url: String, username: String, password: String, dbname: String) {
        conn = DriverManager.getConnection(url, username, password)
        this.dbname = dbname
    }

    fun getVersion(project: String): Map<String, List<String>> {
        val statement = conn.createStatement()
        val resultSet = statement.executeQuery(SQLCommand.GET_VERSION.toString())
        val versions = emptyMap<String, MutableList<String>>().toMutableMap()

        while (resultSet.next()) {
            if (resultSet.getString("project") == project) {
                resultSet.apply {
                    val versionGroup = getString("version_group")
                    val version = getString("version")
                    if (versionGroup !in versions) {
                        versions[versionGroup] = mutableListOf(version)
                    } else {
                        versions[versionGroup]?.add(version)
                    }
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
                    changes = convertJsonChanges(getString("changes")),
                    version = Struct.Version(versionGroup, versionStr),
                    project = project,
                    releaseTag = getString("release_tag")
                )
                builds.add(build)
            }
        }

        return builds
    }

    fun convertJsonChanges(changes: String): List<Struct.Change> {
        return Gson().fromJson(changes, Array<Struct.Change>::class.java).toList()
    }

    /**
     *
     */
    fun getLatestBuildId(project: String, version: Struct.Version): Int {
        val builds = getBuild(project, version)
        val buildIds = emptyList<Int>().toMutableList()
        builds.forEach {
            buildIds.add(it.buildId)
        }
        return buildIds.max()
    }

    /**
     * Commit build to database
     * This is a thread unsafe operation since it will generate a new build ID based on the maximum value of the build ID in the database.
     * It must be ensured that this new build ID will not be duplicated in a concurrent environment
     *
     * @param project Project ID (In lower case)
     * @param build Build data (The build ID should be null, and the build ID value will be discarded even if it is not null)
     */
    @Synchronized
    fun commitBuild(project: String, build: Struct.Build): Int {
        val newCommitId = getLatestBuildId(project, build.version) + 1
        val statement = conn.prepareStatement(SQLCommand.COMMIT_BUILD.toString())
        build.buildId = newCommitId
        statement.apply {
            setInt(1, newCommitId)
            setTimestamp(2, Timestamp(build.time))
            setString(3, build.jarName)
            setString(4, build.sha256)
            setString(5, Gson().toJson(build.changes))
            setString(6, build.project)
            setString(7, build.version.versionGroup)
        }
        return statement.executeUpdate()
    }
}