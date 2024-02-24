package com.luminolmc.webapi.data

object DataManager {
    val fetchThread = Thread(::mainLoop)
    var interval = 1000L * 60 * 10 // ten minutes

    fun startMainLoop() {
        fetchThread.start()
    }

    private fun mainLoop() {
        while (true) {
            // Get versions/branches
            Basic.repo.forEach { (project, repo) ->
                val version = GithubAPI.fetchVersions(repo)
                Basic.version[project] = version
            }
            Basic.repo.forEach { (t, u) ->
                val buildList = GithubAPI.fetchActions(u)
                Basic.builds[t] = buildList.toMutableList()
            }
            Basic.logger.info("Refreshed data")
            Thread.sleep(interval)
        }
    }
}