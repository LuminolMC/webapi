package com.luminolmc.webapi.v2.misc

enum class HTTPError(
    private val code: Int,
    private val message: String
) {
    NOT_FOUND(404, "Not found"),
    FORBIDDEN(403, "Forbidden"),;

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
