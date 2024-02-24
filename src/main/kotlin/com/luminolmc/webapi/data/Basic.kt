package com.luminolmc.webapi.data

object Basic {
    val projects = mapOf(
        "luminol" to "Luminol",
        "lightingluminol" to "LightingLuminol"
    )
    val repo = mapOf(
        "luminol" to "LuminolMC/Luminol",
        "lightingluminol" to "LuminolMC/LightingLuminol"
    )
    var version = mutableMapOf<String, List<String>>()
    val builds = mutableMapOf<String, mutableList<DataStructure.WorkflowRuns>()
}