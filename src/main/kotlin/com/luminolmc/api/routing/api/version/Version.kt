package com.luminolmc.api.routing.api.version

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.loadVersionRoute() {
    routing {
        route("/v1/projects/luminol/versions/{version}/builds") {
            get {
                val version = call.parameters["version"]
                if (version != "1.20.4" && version != "1.20.2")
                    call.respondText("FUck")

                val response = mapOf(
                    "code" to 200,
                    "project_id" to "luminol",
                    "project_name" to "Luminol",
                    "version" to version,
                    "builds" to listOf(
                        mapOf(
                            "build" to 1,
                            "time" to "2023-12-10T13:26:04.175Z",
                            "channel" to "default",
                            "changes" to listOf(
                                mapOf(
                                    "commit" to "c68364775201b3968c41ad22b3cd8fd816813e74",
                                    "summary" to "FUCK",
                                    "message" to "message"
                                )
                            ),
                            "promoted" to false,
                            "download" to mapOf(
                                "application" to mapOf(
                                    "name" to "1.20.4-luminol.jar",
                                    "sha256" to "114514ff",
                                ),
                                "ghproxy" to mapOf(
                                    "name" to "1.20.4-luminol.jar",
                                    "sha256" to "114514ff",
                                )
                            )
                        )
                    )
                )

                call.respond(response)
            }
        }
    }
}