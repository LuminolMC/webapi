package com.luminolmc.webapi.v2.data

enum class SQLCommand(private var command: String) {
    GET_VERSION(
        "SELECT * FROM versions"
    ),
    GET_BUILD(
        "SELECT * FROM builds"
    ),
    COMMIT_BUILD(
        "INSERT INTO BUILD" +
                " (id, time, jar_name, sha256, changes, project, version_group, version, release_tag)" +
                " VALUES (?,?,?,?,?,?,?,?,?,?)",
    )
    ;

    override fun toString(): String {
        return this.command
    }
}