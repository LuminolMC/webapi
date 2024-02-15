package com.luminolmc.api.routing.api

import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.loadProjectsRoute() {
    routing {
        route("/v2/projects") {
            get {

            }
        }
    }
}