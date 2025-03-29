package com.example.githubrepoviewerapp

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Repo(
    val name: String,
    val private: Boolean,
    val owner: Owner,
    @Json(name = "html_url") val htmlURL: String,
    val description: String?,
    val fork: Boolean,
    val url: String,
    @Json(name = "stargazers_count") val stars: Int,
    @Json(name = "watchers_count") val watchers: Int,
    @Json(name = "forks_count") val forks: Int,
    @Json(name = "open_issues_count") val issues: Int,
)

@JsonClass(generateAdapter = true)
data class Owner(
    @Json(name = "avatar_url") val avatarURL: String
)