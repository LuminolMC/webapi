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
            get {
                handleRequest(call)
            }

            post {
                handleRequest(call)
            }
        }
    }
}

suspend fun handleRequest(call: ApplicationCall) {
    if (call.request.headers["Authorization"] != ConfigManager.token) {
        call.respond(HTTPError.FORBIDDEN.getHTTPResponse())
        return
    }

    val params = call.receiveParameters() // Only receive parameters once
    val nullList = mutableListOf<String>()

    val paramList = listOf("jar_name", "changes", "release_tag", "sha256", "time", "channel")
    paramList.forEach {
        if (params[it] == null)
            nullList.add(it)
    }

    if (nullList.isNotEmpty()) {
        call.respond(HttpStatusCode.BadRequest, mapOf("code" to "400", "msg" to "param $nullList is not provided"))
        return
    }

    val project = call.parameters["project"]!!
    val version = call.parameters["version"]!!
    val jarName = params["jar_name"]!!
    val changesJson = params["changes"]!!
    val releaseTag = params["release_tag"]!!
    val sha256 = params["sha256"]!!
    val time = params["time"]!!
    val channel = params["channel"]!!
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
    call.respond(HttpStatusCode.OK, mapOf("code" to 200, "message" to "OK"))
}

