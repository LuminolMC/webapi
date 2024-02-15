package com.luminolmc.api

import com.luminolmc.api.routing.api.loadProjectsRoute
import com.luminolmc.api.routing.loadMiscRoute
import freemarker.cache.ClassTemplateLoader
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.freemarker.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*

fun main() {
    embeddedServer(Netty, port = 5555, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.loadRoutes() {
    loadMiscRoute()
    loadProjectsRoute()
}

fun Application.installPlugins() {
    install(FreeMarker) {
        templateLoader = ClassTemplateLoader(this::class.java.classLoader, "templates")
    }
    install(ContentNegotiation) {
        jackson {}
    }
}

fun Application.module() {
    installPlugins()
    loadRoutes()
}
