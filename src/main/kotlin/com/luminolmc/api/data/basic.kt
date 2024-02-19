package com.luminolmc.api.data

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

object basic {
    data class Branches(
        @SerializedName("name") val name: String,
        @SerializedName("commit") val commit: Commit,
        @SerializedName("protected") val isProtected: Boolean
    )

    data class Commit(
        @SerializedName("sha") val sha: String,
        @SerializedName("url") val url: String
    )

    fun fetchUrl(url: String): String {
        val urlObj = URL(url)
        val connection = urlObj.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"

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

    fun getVersions(repo: String): List<String> {
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
}