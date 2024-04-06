package com.luminolmc.webapi.v2.routing

import com.google.gson.Gson
import com.luminolmc.webapi.ConfigManager
import com.luminolmc.webapi.v2.data.DatabaseManager
import com.luminolmc.webapi.v2.data.Struct
import com.luminolmc.webapi.v2.misc.HTTPError
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.loadCommitBuildRouteV2() {
    routing {
        route("/v2/projects/{project}/{version}/build/commit") {
            post {
                if (call.request.headers["Authorization"] != ConfigManager.token) {
                    call.respond(HTTPError.FORBIDDEN.getHTTPResponse())
                }

                val project = call.parameters["project"]!!
                val versionGroup = call.parameters["version_group"]!!
                val version = call.parameters["version"]!!
                val jarName = call.parameters["jar_name"]!!
                val changesJson = call.parameters["changes"]!!  // 这里要直接给一个json进来
                val releaseTag = call.parameters["release_tag"]!!
                val sha256 = call.parameters["sha256"]!!
                val time = call.parameters["time"]!!
                val channel = call.parameters["channel"]!!

                val changes = Gson().fromJson(changesJson, Array<Struct.Change>::class.java).toList()
                val build = Struct.Build(
                    buildId = null,
                    project = project,
                    version = Struct.Version(versionGroup, version),
                    changes = changes,
                    sha256 = sha256,
                    jarName = jarName,
                    releaseTag = releaseTag,
                    time = time.toLong(),
                    channel = channel
                )
                DatabaseManager.commitBuild(build)
                call.respond(
                    mapOf(
                        "code" to 200,
                        "message" to "OK"
                    )
                )
            }
        }
    }
}