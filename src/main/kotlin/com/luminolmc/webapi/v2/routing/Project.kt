package com.luminolmc.webapi.v2.routing

import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.loadProjectRouteV2() {
    routing {
        route("v2/projects") {
            get {

            }
        }
    }
}