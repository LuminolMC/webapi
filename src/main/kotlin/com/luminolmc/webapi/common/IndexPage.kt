package com.luminolmc.webapi.common

import io.ktor.server.application.*
import io.ktor.server.freemarker.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*

fun Application.loadIndexPageRoute() {
    routing {
        get("/") {
            val dsl = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date())
            val instant = Instant.ofEpochMilli(System.currentTimeMillis())
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                .withZone(ZoneOffset.UTC)
            val param = mapOf(
                "time" to dsl,
                "time_fmt" to formatter.format(instant)
            )
            call.respond(
                FreeMarkerContent("index.ftl", param)
            )
        }
    }
}



