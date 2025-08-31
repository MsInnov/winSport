package com.mscode.data.availableleagues.datasource

import com.mscode.data.availableleagues.api.AvailableLeaguesApi
import com.mscode.data.availableleagues.model.LeaguesEntity
import com.mscode.domain.common.WrapperResults
import javax.inject.Inject

class AvailableLeaguesRemoteDataSource @Inject constructor(
    private val api: AvailableLeaguesApi
) {

    suspend fun getAvailableLeagues(
        apiKey: String,
        path: String
    ): WrapperResults<LeaguesEntity> = try {
        api.getAllLeagues(apiKey, path).body()?.let {
            WrapperResults.Success(it)
        } ?: run {
            WrapperResults.Error(Exception())
        }
    } catch (e: Exception) {
        WrapperResults.Error(e)
    }
}