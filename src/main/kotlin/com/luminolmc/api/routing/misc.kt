package com.luminolmc.api.routing

import io.ktor.server.application.*
import io.ktor.server.freemarker.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.text.SimpleDateFormat
import java.util.Date

fun Application.loadMiscRoute() {
    routing {
        get("/") {
            val dsl = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date())
            val param = mapOf("time" to dsl)
            call.respond(
                FreeMarkerContent("index.ftl", param)
            )
        }
    }
}