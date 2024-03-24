package com.luminolmc.webapi

import com.luminolmc.webapi.v1.data.Basic
import com.luminolmc.webapi.v1.data.DataManager
import com.luminolmc.webapi.v1.routing.api.loadProjectsRoute
import com.luminolmc.webapi.v1.routing.api.loadVersionRoute
import com.luminolmc.webapi.v1.routing.loadMiscRoute
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
    Basic.logger = app.application.log
    app.start(wait = true)
}

fun Application.loadRoutes() {
    loadMiscRoute()
    loadProjectsRoute()
    loadVersionRoute()
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

fun Application.loadConfig() {
    ConfigManager.loadConfig()
}

fun startDataManager() {
    DataManager.startMainLoop()
}

fun Application.module() {
    installPlugins()
    loadRoutes()
    startDataManager()
    loadConfig()
}
