package com.luminolmc.api.routing.api

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.loadProjectsRoute() {
    routing {
        route("/v1/projects") {
            get {
                var response = mapOf(
                    "code" to 200,
                    "project_id" to "luminol",
                    "project_name" to "Luminol",
                    "version_groups" to listOf("1.20"),
                    "versions" to listOf("1.20.2", "1.20.4")
                )
                call.respond(response)
            }
        }
    }
}