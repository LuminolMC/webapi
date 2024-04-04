package com.luminolmc.webapi.v2.data

class Struct {
    data class Version(
        val versionGroup: String,
        val version: String
    )

    data class Build(
        val buildId: Int,
        val project: String,
        val version: Version,
        val time: Long,
        val jarName: String,
        val sha256: String,
        val summary: String,
        val message: String
    )

    data class Project(
        val id: String,
        val name: String,
        val repo: String
    )
}