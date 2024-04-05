package com.luminolmc.webapi.v2.misc

enum class HTTPErrorEnum(
    val code: Int,
    val message: String
) {
    NOT_FOUND(404, "Not found");

    override fun toString(): String {
        return message
    }

    fun getHTTPResponse(): Map<Any, Any> {
        return mapOf(
            "code" to code,
            "message" to message
        )
    }
}