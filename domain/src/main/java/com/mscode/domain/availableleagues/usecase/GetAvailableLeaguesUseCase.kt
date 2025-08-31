package com.mscode.domain.availableleagues.usecase

import com.mscode.domain.availableleagues.repository.AvailableLeaguesRepository

class GetAvailableLeaguesUseCase(
    private val repository: AvailableLeaguesRepository
) {

    suspend operator fun invoke() = repository.getAvailableLeagues()

}