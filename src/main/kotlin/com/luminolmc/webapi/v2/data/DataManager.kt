package com.luminolmc.webapi.v2.data

object DataManager {
    var started = false
    val interval = 1000L * 60 * 10

    fun refreshBuilds() {

    }

    fun refreshBranches() {

    }

    fun mainLoop() {
        while (true) {
            refreshBuilds()
            refreshBranches()
            Thread.sleep(interval)
        }
    }
}