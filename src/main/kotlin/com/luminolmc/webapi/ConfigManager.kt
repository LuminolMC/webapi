package com.luminolmc.webapi

import com.electronwill.nightconfig.core.file.FileConfig
import java.io.File


object ConfigManager {
    private val configName = "config.toml"
    private val configFile = File(configName)
    private val config = FileConfig.of(configFile)

    val projects = mutableMapOf(
        "luminol" to mutableListOf("Luminol", "LuminolMC/Luminol"),
        "lightingluminol" to mutableListOf("LightingLuminol", "LuminolMC/Luminol")
    )
    val database = mutableMapOf(
        "url" to "jdbc:mysql://lolicon.fit:3306/lumsite",
        "username" to "username",
        "password" to "password",
        "dbname" to "lumsite"
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
        val projectList = config.get<List<String>>("projects.projects")
        for (key in projectList) {
            projects[key] = mutableListOf(
                config.get("projects.${key}.name"),
                config.get("projects.${key}.repo")
            )
        }
        database.apply {
            set("url", config.get("database.url"))
            set("username", config.get("database.username"))
            set("password", config.get("database.password"))
            set("dbname", config.get("database.dbname"))
        }
    }

    private fun initConfig() {
        config.set<List<String>>("projects.projects", projects.keys.toList())
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