package com.luminolmc.webapi.v2.routing

import com.google.gson.Gson
import com.luminolmc.webapi.ConfigManager
import com.luminolmc.webapi.v2.data.DatabaseManager
import com.luminolmc.webapi.v2.data.Struct
import com.luminolmc.webapi.v2.misc.HTTPError
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.loadCommitBuildRouteV2() {
    routing {
        route("/v2/projects/{project}/{version}/build/commit") {
            post {
                if (call.request.headers["Authorization"] != ConfigManager.token) {
                    call.respond(HTTPError.FORBIDDEN.getHTTPResponse())
                }



                val paramList = listOf(
                    "jar_name",
                    "changes",
                    "release_tag",
                    "sha256",
                    "time",
                    "channel"
                )
                val nullList = mutableListOf<String>()


                paramList.forEach {
                    if (call.receiveParameters()[it] == null)
                        nullList.add(it)
                }

                if (nullList.isNotEmpty()) {
                    call.respond(mapOf(
                        "code" to "400",
                        "msg" to "param ${nullList.toString()} is not provided"
                    ))
                }

                val project = call.parameters["project"]!!
                val version = call.parameters["version"]!!
                val jarName = call.receiveParameters()["jar_name"]!!
                val changesJson = call.receiveParameters()["changes"]!!  // 这里要直接给一个json进来
                val releaseTag = call.receiveParameters()["release_tag"]!!
                val sha256 = call.receiveParameters()["sha256"]!!
                val time = call.receiveParameters()["time"]!!
                val channel = call.receiveParameters()["channel"]!!
                val versionGroup = DatabaseManager.whichVersionGroup(project, version)!!

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