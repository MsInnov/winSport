package com.mscode.data.remoteconfig.model

const val url_the_sports_db = "url_thesportsdb"

const val key_api = "api_key"

const val path_all_leagues = "path_all_leagues"
const val path_search_all_teams = "path_search_all_teams"


val datasUrl = listOf(url_the_sports_db, key_api)
val paths = listOf(path_all_leagues, path_search_all_teams)

data class Url (
    val keyApi: String,
    val value: String,
)

data class Path (
    val name: String,
    val value: String,
)