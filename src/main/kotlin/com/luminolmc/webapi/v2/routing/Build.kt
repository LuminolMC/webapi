package com.luminolmc.webapi.v2.routing

import com.luminolmc.webapi.ConfigManager
import com.luminolmc.webapi.v2.data.DatabaseManager
import com.luminolmc.webapi.v2.data.Struct
import com.luminolmc.webapi.v2.misc.HTTPError
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

fun Application.loadBuildRouteV2() {
    routing {
        route("/v2/projects/{project}/versions/{version}/builds") {
            get {
                val project = call.parameters["project"]!!
                val version = call.parameters["version"]!!
                if (project !in ConfigManager.projects.keys
                    || version !in DatabaseManager.getSubVersion(project)
                    || DatabaseManager.whichVersionGroup(project, version) == null
                )
                    call.respond(HTTPError.NOT_FOUND)
                val builds = DatabaseManager.getAllBuild(
                    project,
                    Struct.Version(DatabaseManager.whichVersionGroup(project, version)!!, version)
                )

                val response = mutableMapOf(
                    "code" to 200,
                    "project_id" to project,
                    "project_name" to ConfigManager.projects[project]!![0],
                    "version" to version,
                    "builds" to emptyList<Struct.DownloadBuild>().toMutableList()
                )

                builds.forEach {
                    val instant = Instant.ofEpochMilli(it.time)
                    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                        .withZone(ZoneOffset.UTC)
                    val downloads = mapOf(
                        "application" to Struct.Download(
                            name = it.jarName,
                            sha256 = it.sha256
                        ),
                        "ghproxy" to Struct.Download(
                            name = it.jarName,
                            sha256 = "ghproxy"
                        )
                    )
                    val downloadBuild = Struct.DownloadBuild(
                        build = it.buildId!!,
                        time = formatter.format(instant),
                        channel = it.channel,
                        changes = it.changes,
                        downloads = downloads
                    )
                    (response["builds"] as MutableList<Struct.DownloadBuild>).add(downloadBuild)
                }

                call.respond(response)
            }
        }
    }
}
