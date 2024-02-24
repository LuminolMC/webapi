package com.luminolmc.webapi.data

object DataManager {
    val fetchThread = Thread(::mainLoop)
    var interval = 1000L * 60 * 10 // ten minutes

    fun startMainLoop() {
        fetchThread.start()
    }

    private fun mainLoop() {
        Basic.repo.forEach { (project, repo) ->
            val version = GithubAPI.fetchVersions(repo)
            Basic.version[project] = version
        }
        println("Refreshed")
        println(Basic.version)
        // Get versions/branches
        while (true) {

            Thread.sleep(interval)
        }
    }
}