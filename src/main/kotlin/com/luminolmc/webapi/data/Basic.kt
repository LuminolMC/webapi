package com.luminolmc.webapi.data

import io.ktor.util.logging.*

object Basic {
    lateinit var logger: Logger
    val projects = mapOf(
        "luminol" to "Luminol",
        "lightingluminol" to "LightingLuminol"
    )
    val repo = mapOf(
        "luminol" to "LuminolMC/Luminol",
        "lightingluminol" to "LuminolMC/LightingLuminol"
    )
    var version = mutableMapOf<String, List<String>>()
    val builds = mutableMapOf<String, MutableList<DataStructure.WorkflowRuns>>()
}