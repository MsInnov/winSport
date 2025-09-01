package com.mscode.data.leagueteam.api

import com.mscode.data.leagueteam.model.LeagueTeamsEntity
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface LeagueTeamApi {
    @GET("api/v1/json/{apiKey}/{path}")
    suspend fun getLeagueTeam(
        @Path("apiKey") apiKey: String,
        @Path("path") path: String,
        @Query("l") leagueName: String
    ): Response<LeagueTeamsEntity>
}