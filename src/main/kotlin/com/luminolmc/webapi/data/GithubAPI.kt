package com.luminolmc.webapi.data

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import io.ktor.server.application.*
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

object GithubAPI {
    val token = "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"

    data class Branches(
        val name: String,
        val commit: Commit,
        @SerializedName("protected") val isProtected: Boolean
    )

    data class Commit(
        val sha: String,
        val url: String
    )

    private fun fetchUrl(url: String): String {
        val urlObj = URL(url)
        val connection = urlObj.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"
        connection.setRequestProperty("Authorization", token)

        val responseCode = connection.responseCode
        if (responseCode == HttpURLConnection.HTTP_OK) {
            val reader = BufferedReader(InputStreamReader(connection.inputStream))
            val response = StringBuilder()
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                response.append(line)
            }
            reader.close()

            return response.toString()
        } else {
            return ""
        }
    }

    fun fetchVersions(repo: String): List<String> {
        val url = "https://api.github.com/repos/${repo}/branches"
        val gson = Gson()
        val json = fetchUrl(url)
        if (json.isEmpty()) {
            Basic.logger.info(json)
            return listOf()
        }
        val branches = gson.fromJson(json, Array<Branches>::class.java)
        val versions: MutableList<String> = mutableListOf()

        branches.forEach {
            versions.add(it.name.removeSuffix("ver/"))
        }

        return versions
    }

    fun fetchActions(repo: String): List<DataStructure.WorkflowRuns> {
        val url = "https://api.github.com/repos/${repo}/actions/runs"
        val gson = Gson()
        val json = fetchUrl(url)
        if (json.isEmpty())
            return listOf()
        val actionMapType = object : TypeToken<Map<String, Any>>() {}.type
        val action: Map<String, Any> = gson.fromJson(json, actionMapType)
        val run = action["workflow_runs"] as List<Map<String, Any>>
        val buildList = mutableListOf<DataStructure.WorkflowRuns>()
        for (i in run) {
            println(i["id"])
            val runId = (i["id"] as Double).toLong()
            val commit = i["head_sha"] as String
            val artifactUrl = fetchArtifact(repo, runId)
            val time = i["created_at"] as String
            if (artifactUrl == "") // 如果没有工件
                continue
            buildList.add(
                DataStructure.WorkflowRuns(
                    id = runId,
                    commit = commit,
                    artifactUrl = artifactUrl,
                    time = time
                )
            )
        }
        return buildList
    }

    private fun fetchArtifact(repo: String, runId: Long): String {
        val url = "https://api.github.com/repos/${repo}/actions/runs/${runId}/artifacts"
        val gson = Gson()
        val json = fetchUrl(url)
        if (json.isEmpty())
            return ""
        val typeToken = object : TypeToken<Map<String, Any>>() {}.type
        val artifact: Map<String, Any> = gson.fromJson(json, typeToken)
        val artifacts: List<Map<String, Any>> = artifact["artifacts"] as List<Map<String, Any>>
        if (artifacts.isEmpty())
            return ""
        val artifactUrl = artifacts[0]["archive_download_url"] as String
        return artifactUrl
        // 狗日的,这他妈怎么解析，解析你老母
    }
}