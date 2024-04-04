package com.luminolmc.webapi.v2.routing

import com.luminolmc.webapi.ConfigManager
import com.luminolmc.webapi.v2.data.DatabaseManager
import com.luminolmc.webapi.v2.data.Struct
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.loadProjectRouteV2() {
    routing {
        route("v2/projects") {
            get {
                val projects = emptyList<Struct.Project>().toMutableList()
                ConfigManager.projects.forEach{(t, u) ->
                    val project = Struct.Project(
                        id = t,
                        name = u[0],
                        repo = u[1]
                    )
                    projects.add(project)
                }
                call.respond(projects)
            }
        }

        route("v2/projects/{project}") {
            
        }
    }
}