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
    val database = mutableMapOf(
        "url" to "",
        "username" to "",
        "password" to "",
        "dbname" to ""
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
        database.apply {
            set("url", config.get<String>("database.url"))
            set("username", config.get<String>("database.username"))
            set("password", config.get<String>("database.password"))
            set("dbname", config.get<String>("database.dbname"))
        }
    }

    private fun initConfig() {
        config.set<List<String>>("projects.projects", projects.keys)
        projects.forEach { (t, u) ->
            config.set<String>("projects.${t}.name", u[0])
            config.set<String>("projects.${t}.repo", u[1])
        }
        database.forEach { (t, u) ->
            config.set<String>("database.${t}", u)
        }
        config.save()
    }
}