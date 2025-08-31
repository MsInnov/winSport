package com.mscode.data.availableleagues.datasource

import com.mscode.domain.availableleagues.model.League

class AvailableLeaguesLocalDataSource {

    private var _availableLeagues: List<League> = emptyList()

    val availableLeagues: List<League>
        get() = _availableLeagues

    fun saveAvailableLeagues(league: List<League>) {
        _availableLeagues = league
    }
}