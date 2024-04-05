package com.luminolmc.webapi.v2.routing

import com.luminolmc.webapi.ConfigManager
import com.luminolmc.webapi.v2.data.DatabaseManager
import com.luminolmc.webapi.v2.data.Struct
import com.luminolmc.webapi.v2.misc.HTTPError
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.awt.image.DataBufferShort

fun Application.loadDownloadRouteV2() {
    routing {
        route("/v2/projects/{project}/versions/{version}/builds/{build_id}/downloads/{method}") {
            get {
                val project = call.parameters["project"]!!
                val version = call.parameters["version"]!!
                val buildId = call.parameters["build_id"]!!
                val method = call.parameters["method"]!!

                if (project !in ConfigManager.projects.keys
                    || version !in DatabaseManager.getSubVersion(project)
                    || DatabaseManager.whichVersionGroup(project, version) == null)
                    call.respond(HTTPError.NOT_FOUND)

                val repo = ConfigManager.projects[project]!![1]
                val versionGroup = DatabaseManager.whichVersionGroup(project, version)!!
                val build = DatabaseManager.getBuild(project, Struct.Version(versionGroup, version), buildId.toInt())!!
                val githubUrl = "https://github.com/$repo/releases/download/${build.releaseTag}/${build.jarName}"
                val ghproxyUrl = "https://mirror.ghproxy.com/$githubUrl"

                if (method == "application") {
                    call.respondRedirect(githubUrl, permanent = false)
                } else if (method == "ghproxy") {
                    call.respondRedirect(ghproxyUrl, permanent = false)
                } else if (method == build.jarName) {
                    call.respondRedirect(githubUrl, permanent = false)
                } else {
                    call.respond(HTTPError.FORBIDDEN.getHTTPResponse())
                }
            }
        }
    }
}