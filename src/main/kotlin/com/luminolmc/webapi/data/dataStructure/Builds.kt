package com.luminolmc.webapi.data.dataStructure

class Builds {
    data class Build(
        val build: Int,
        val time: String,
        val channel: String,
        val changes: List<Change>,
        val promoted: Boolean,
        val downloads: Downloads
    )

    data class Change(
        val commit: String,
        val summary: String,
        val message: String
    )

    data class Downloads(
        val application: File,
        val ghproxy: File
    )

    data class File(
        val name: String,
        val sha256: String
    )

    data class Project(
        val code: Int,
        val projectId: String,
        val projectName: String,
        val version: String,
        val builds: List<Build>
    )
}