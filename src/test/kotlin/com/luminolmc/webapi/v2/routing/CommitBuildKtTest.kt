package com.luminolmc.webapi.v2.routing

import com.luminolmc.webapi.module
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlin.test.Test

class CommitBuildKtTest {

    @Test
    fun testPostV2ProjectsProjectVersionBuildCommit() = testApplication {
        application {
            module()
        }
        client.post("/v2/projects/{project}/{version}/build/commit") {
            contentType(ContentType.Application.Json)
            setBody("token", "")
        }
    }
}