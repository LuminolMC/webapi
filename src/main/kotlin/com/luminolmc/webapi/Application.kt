package com.luminolmc.webapi

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

fun Application.loadRoutes() {
    loadProjectRouteV2()
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

fun Application.module() {
    installPlugins()
    loadRoutes()
    loadConfig()
}
