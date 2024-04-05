package com.luminolmc.webapi.v2.routing

import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.loadCommitBuildRouteV2() {
    routing {
        route("/v2/projects/{project}/{version}/build/commit") {
            get {
                val project = call.parameters["project"]
                val version = call.parameters["version"]
                val jar_name = call.parameters["jar_name"]
                val summary = call.parameters["summary"]
            }
        }
    }
}