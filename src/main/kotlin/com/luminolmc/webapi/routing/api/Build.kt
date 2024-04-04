package com.luminolmc.webapi.routing.api

import com.luminolmc.webapi.data.GithubAPI
import com.luminolmc.webapi.data.Basic
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.loadVersionRoute() {
    routing {
        route("/v1/projects/{project}/versions/{version}/builds") {
            get {
                val project = call.parameters["project"]!!
                val version = call.parameters["version"]!!

                if (project !in Basic.projects.keys)
                    call.respondText("project not found")
                if (version !in Basic.version[project]!!)
                    call.respondText("version not found")

                val response = mutableMapOf(
                    "code" to 200,
                    "project_id" to project,
                    "project_name" to Basic.projects[project],
                    "version" to version,
                    "builds" to mutableListOf<Any>()
                )

                val builds = mutableListOf<MutableMap<String, Any>>()
                val workflows = Basic.builds[project]
                var i = 1
                workflows!!.forEach {
                    builds.add(mutableMapOf(
                        "build" to i,
                        "time" to it.time,
                        "channel" to "default",
                        "changes" to listOf(
                            mapOf(
                                "commit" to it.commit,
                                "summary" to "获取还没实现，自己去github看",
                                "message" to "获取还没实现，自己去github看"
                            )
                        ),
                        "promoted" to false,
                        "downloads" to mapOf(
                            "application" to mapOf(
                                "name" to "${project}-${version}-${it.commit}",
                                "sha256" to "没有哈希，没实现"
                            ),
                            "ghproxy" to mapOf(
                                "name" to "1.20.4-luminol.jar",
                                "sha256" to "没有哈希",
                            )
                        )
                    ))

                    i += 1
                }
                response["builds"] = builds
                call.respond(response)
            }
        }
    }
}