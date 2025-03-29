package com.example.githubrepoviewerapp

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Repo(
    val id: Long,
    val name: String,
    @Json(name = "full_name") val fullName: String,
    val private: Boolean,
    val owner: Owner,
    @Json(name = "html_url") val htmlUrl: String,
    val description: String?,
    val fork: Boolean,
    val url: String,
    @Json(name = "stargazers_count") val stars: Int,
    @Json(name = "watchers_count") val watchers: Int,
    val language: String?,
    @Json(name = "forks_count") val forks: Int,
    @Json(name = "open_issues_count") val openIssues: Int,
    @Json(name = "default_branch") val defaultBranch: String
)

@JsonClass(generateAdapter = true)
data class Owner(
    val login: String,
    val id: Long,
    @Json(name = "avatar_url") val avatarUrl: String
)