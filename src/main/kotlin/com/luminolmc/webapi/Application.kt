package com.luminolmc.webapi

import com.luminolmc.webapi.common.loadIndexPageRoute
import com.luminolmc.webapi.v2.data.DatabaseManager
import com.luminolmc.webapi.v2.routing.loadBuildRouteV2
import com.luminolmc.webapi.v2.routing.loadCommitBuildRouteV2
import com.luminolmc.webapi.v2.routing.loadDownloadRouteV2
import com.luminolmc.webapi.v2.routing.loadProjectRouteV2
import freemarker.cache.ClassTemplateLoader
import io.ktor.serialization.gson.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.freemarker.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*


fun main() {
    val app = embeddedServer(Netty, port = 5555, host = "0.0.0.0", module = Application::module)
    app.start(wait = true)
}

fun Application.module() {
    installPlugins()
    loadRoutes()
    loadConfig()
    ConfigManager.database.apply {
        DatabaseManager.connect(
            url = get("url")!!,
            username = get("username")!!,
            password = get("password")!!,
            dbname = get("dbname")!!
        )
    }
}

fun Application.installPlugins() {
    install(FreeMarker) {
        templateLoader = ClassTemplateLoader(this::class.java.classLoader, "templates")
    }
    install(ContentNegotiation) {
        gson { }
    }

    intercept(ApplicationCallPipeline.Plugins) {
        // configure CORS
        call.response.header("Access-Control-Allow-Origin", "*")
    }
}

fun Application.loadRoutes() {
    loadIndexPageRoute()
    loadProjectRouteV2()
    loadBuildRouteV2()
    loadCommitBuildRouteV2()
    loadDownloadRouteV2()
}

fun loadConfig() {
    ConfigManager.loadConfig()
}


