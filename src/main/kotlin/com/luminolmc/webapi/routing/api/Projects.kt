package com.luminolmc.webapi.routing.api

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.loadProjectsRoute() {
    routing {
        route("/v1/projects") {
            get {
                var response = listOf(
                    mapOf(
                        "id" to "luminol",
                        "name" to "Luminol",
                        "repo" to "LuminolMC/Luminol"
                    )
                )
                call.respond(response)
            }
        }
        route("/v1/projects/luminol") {
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
        route("/v1/projects/lightingluminol") {
            get {
                var response = mapOf(
                    "code" to 200,
                    "project_id" to "lightingluminol",
                    "project_name" to "LightingLuminol",
                    "version_groups" to listOf("1.20"),
                    "versions" to listOf("1.20.4")
                )
                call.respond(response)
            }
        }
    }
}