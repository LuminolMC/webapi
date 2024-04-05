package com.luminolmc.webapi.v2.routing

import com.luminolmc.webapi.ConfigManager
import com.luminolmc.webapi.v2.data.DatabaseManager
import com.luminolmc.webapi.v2.data.Struct
import com.luminolmc.webapi.v2.misc.HTTPError
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.loadBuildRouteV2() {
    routing {
        route("/v2/projects/{project}/versions/{version}/builds") {
            get {
                val project = call.parameters["project"]!!
                val version = call.parameters["version"]!!
                if (project !in ConfigManager.projects.keys
                    || version !in DatabaseManager.getSubVersion(project)
                    || DatabaseManager.whichVersionGroup(project, version) == null)
                    call.respond(HTTPError.NOT_FOUND)
                val builds = DatabaseManager.getBuild(project,
                    Struct.Version(DatabaseManager.whichVersionGroup(project, version)!!, version))

            }
        }
    }
}
