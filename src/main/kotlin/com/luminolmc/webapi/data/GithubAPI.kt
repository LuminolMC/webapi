package com.luminolmc.webapi.data

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
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
        if (json.isEmpty())
            return listOf("")
        val branches = gson.fromJson(json, Array<Branches>::class.java)
        val versions: MutableList<String> = mutableListOf()

        branches.forEach {
            versions.add(it.name)
        }

        return versions
    }

    fun fetchActions(repo: String) {

    }
}