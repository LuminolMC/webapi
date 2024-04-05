package com.luminolmc.webapi.v2.data

import org.jetbrains.annotations.Nullable

class Struct {
    data class Version(
        val versionGroup: String,
        val version: String
    )

    data class Build(
        @Nullable var buildId: Int?,
        val project: String,
        val version: Version,
        val time: Long,
        val jarName: String,
        val sha256: String,
        val changes: List<Change>,
        val releaseTag: String,
        val channel: String
    )

    data class Project(
        val id: String,
        val name: String,
        val repo: String
    )

    data class Change(
        val commit: String,
        val summary: String,
        val message: String
    )

    data class Download(
        val name: String,
        val sha256: String
    )

    data class DownloadBuild(
        val build: Int,
        val time: String,
        val channel: String = "default",
        val changes: List<Change>,
        val promoted: Boolean = false,
        val downloads: Map<String, Download>
    )
}