package com.luminolmc.webapi.v2.data

import com.google.gson.Gson
import java.sql.Connection
import java.sql.DriverManager
import java.sql.Timestamp

object DatabaseManager {
    lateinit var conn: Connection
    lateinit var dbname: String

    /**
     * connect to database
     */
    fun connect(url: String, username: String, password: String, dbname: String) {
        conn = DriverManager.getConnection(url, username, password)
        this.dbname = dbname
    }

    /**
     * Return all version group / version belongs to this project.
     * Map structure: <project_id, List<subversion>>
     *
     * @param project project id
     */
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

    /**
     * Return all subversion belongs to this project
     *
     * @param project project id
     */
    fun getSubVersion(project: String): List<String> {
        val versions = getVersion(project)
        val subVersions = emptyList<String>().toMutableList()
        versions.forEach { (t, u) ->
            u.forEach { subVersion ->
                if (subVersion.isNotEmpty())
                    subVersions.add(subVersion)
            }
        }
        return subVersions
    }

    /**
     * Give a subversion and this will return a version group.
     * Return null if the subversion doesn't exist in this project
     *
     * @project project id
     * @version subversion
     */
    fun whichVersionGroup(project: String, version: String): String? {
        val versions = getVersion(project)
        versions.forEach { (t, u) ->
            if (version in u)
                return t
        }
        return null
    }

    /**
     * Return all builds belongs to this version
     *
     * @param project project id
     * @param version version info
     */
    fun getAllBuild(project: String, version: Struct.Version): List<Struct.Build> {
        val versionGroup = version.versionGroup
        val versionStr = version.version
        val statement = conn.createStatement()
        val resultSet = statement.executeQuery(SQLCommand.GET_BUILD.toString())
        val builds = emptyList<Struct.Build>().toMutableList()

        while (resultSet.next()) {
            if (resultSet.getString("version_group") != versionGroup
                || resultSet.getString("version_group") != version.versionGroup
                || resultSet.getString("version") != version.version
                || resultSet.getString("project") != project)
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
                    releaseTag = getString("release_tag"),
                    channel = getString("channel")
                )
                builds.add(build)
            }
        }

        return builds
    }

    fun getBuild(project: String, version: Struct.Version, buildId: Int): Struct.Build? {
        val builds = getAllBuild(project, version)

        builds.forEach {
            if (it.buildId == buildId)
                return it
        }
        return null
    }

    private fun convertJsonChanges(changes: String): List<Struct.Change> {
        return Gson().fromJson(changes, Array<Struct.Change>::class.java).toList()
    }

    /**
     *
     */
    fun getLatestBuildId(project: String, version: Struct.Version): Int {
        val builds = getAllBuild(project, version)
        val buildIds = emptyList<Int>().toMutableList()
        try {
            builds.forEach {
                buildIds.add(it.buildId!!)
            }
            return buildIds.max()
        } catch (e: Exception) {  // 如果这里抛出异常代表数据库没有关于这个版本的构建, 直接返回0
            return 0
        }
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
    fun commitBuild(build: Struct.Build): Int {
        val newCommitId = getLatestBuildId(build.project, build.version) + 1
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
            setString(8, build.version.version)
            setString(9, build.releaseTag)
            setString(10, build.channel)
        }
        return statement.executeUpdate()
    }
}