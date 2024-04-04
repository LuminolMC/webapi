package com.luminolmc.webapi.routing.api

import com.luminolmc.webapi.data.Basic
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.loadProjectsRoute() {
    routing {
        route("/v1/projects") {
            get {
//                var response = listOf(
//                    mapOf(
//                        "id" to "luminol",
//                        "name" to "Luminol",
//                        "repo" to "LuminolMC/Luminol"
//                    )
//                )
                val response = mutableListOf<Map<String, String>>()
                Basic.projects.forEach { (t, u) ->
                    response.add(
                        mapOf(
                            "id" to t,
                            "name" to u,
                            "repo" to Basic.repo[t]!!
                        )
                    )
                }
                call.respond(response)
            }
        }

        route("/v1/projects/{project}") {
            get {
                val project = call.parameters["project"]
                if (project !in Basic.projects.keys) {
                    call.respondText("404")
                }
                val response = mapOf(
                    "project_id" to project,
                    "project_name" to Basic.projects[project],
                    "version_groups" to listOf("1.20"),
                    "versions" to Basic.version[project]
                )
                call.respond(response)
            }
        }
    }
}