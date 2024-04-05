package com.luminolmc.webapi.v2.routing

import com.luminolmc.webapi.ConfigManager
import com.luminolmc.webapi.v2.data.DatabaseManager
import com.luminolmc.webapi.v2.data.Struct
import com.luminolmc.webapi.v2.misc.HTTPErrorEnum
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import javax.xml.crypto.Data

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
            get {
                val project = call.parameters["project"]
                if (project !in ConfigManager.projects) {
                   call.respond(HTTPErrorEnum.NOT_FOUND.getHTTPResponse())
                }
                val versions = DatabaseManager.getVersion(project!!)
                val response = mutableMapOf(
                    "code" to 200,
                    "project_id" to project,
                    "project_name" to ConfigManager.projects[project]!![0],
                    "version_groups" to emptyList<String>().toMutableList(),
                    "versions" to emptyList<String>().toMutableList()
                )
                versions.keys.forEach {
                    (response["version_groups"] as MutableList<String>).add(it)
                }
                versions.values.forEach {
                    it.forEach { it ->
                        (response["versions"] as MutableList<String>).add(it)
                    }
                }
                call.respond(response)
            }
        }
    }
}