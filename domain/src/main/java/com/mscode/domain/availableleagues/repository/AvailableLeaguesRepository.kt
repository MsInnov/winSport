package com.mscode.domain.availableleagues.repository

import com.mscode.domain.common.WrapperResults
import com.mscode.domain.availableleagues.model.League

interface AvailableLeaguesRepository {

    suspend fun getAvailableLeagues(): WrapperResults<List<League>>

}