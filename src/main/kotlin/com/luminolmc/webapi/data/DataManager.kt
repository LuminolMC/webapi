package com.luminolmc.webapi.data

import io.ktor.server.application.*
import io.netty.util.concurrent.CompleteFuture
import java.util.concurrent.CompletableFuture

object DataManager {
    val fetchThread = Thread(::mainLoop)
    var interval = 5000L

    fun startMainLoop() {
        fetchThread.start()
    }

    private fun mainLoop() {
        basic.repo.forEach { (project, repo) ->
            val version = GithubAPI.fetchVersions(repo)
            basic.version[project] = version
        }
        println("Refreshed")
        println(basic.version)
        // Get versions/branches
        while (true) {
            Thread.sleep(interval)
        }
    }
}