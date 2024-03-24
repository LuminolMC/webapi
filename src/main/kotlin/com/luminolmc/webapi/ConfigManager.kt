package com.luminolmc.webapi

import com.electronwill.nightconfig.core.Config
import com.electronwill.nightconfig.core.file.FileConfig
import java.io.File


object ConfigManager {
    val configName = "config.toml"
    val configFile = File(configName)
    val config = FileConfig.of(configFile)

    val projects = mutableMapOf(
        "luminol" to mutableListOf("Luminol", "LuminolMC/Luminol"),
        "lightingluminol" to mutableListOf("LightingLuminol", "LuminolMC/Luminol")
    )
    var jenkin = mutableMapOf(
        "url" to "http://example.com",
        "token" to "token"
    )

    fun loadConfig() {
        if (configFile.exists()) {
            config.load()
            readConfig()
        } else {
            configFile.createNewFile()
            config.load()
            initConfig()
        }

    }

    private fun readConfig() {
        projects.clear()
        for (key in projects.keys) {
            projects[key] = mutableListOf(
                config.get<String>("projects.${key}.name"),
                config.get<String>("projects.${key}.repo")
            )
        }

        jenkin["url"] = config.get<String>("jenkins.url")
        jenkin["token"] = config.get<String>("jenkins.token")
    }

    private fun initConfig() {
        config.set<List<String>>("projects.projects", projects.keys)
        projects.forEach { (t, u) ->
            config.set<String>("projects.${t}.name", u[0])
            config.set<String>("projects.${t}.repo", u[1])
        }
        config.set<String>("jenkins.url", jenkin["url"])
        config.set<String>("jenkins.token", jenkin["token"])
        config.save()
    }
}