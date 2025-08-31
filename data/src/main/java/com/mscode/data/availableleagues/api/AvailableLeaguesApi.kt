package com.mscode.data.availableleagues.api

import com.mscode.data.availableleagues.model.LeaguesEntity
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface AvailableLeaguesApi {
    @GET("api/v1/json/{apiKey}/{path}")
    suspend fun getAllLeagues(
        @Path("apiKey") apiKey: String,
        @Path("path") path: String
    ): Response<LeaguesEntity>
}