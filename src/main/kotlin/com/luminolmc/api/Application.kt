package com.luminolmc.api

import com.luminolmc.api.routing.api.loadProjectsRoute
import com.luminolmc.api.routing.api.version.loadVersionRoute
import com.luminolmc.api.routing.loadMiscRoute
import freemarker.cache.ClassTemplateLoader
import io.ktor.serialization.gson.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.freemarker.*
import io.ktor.http.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*


fun main() {
    embeddedServer(Netty, port = 5555, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
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
    intercept(ApplicationCallPipeline.Features) {
        call.response.header("Access-Control-Allow-Origin", "*")
    }
}

fun Application.module() {
    installPlugins()
    loadRoutes()
}
