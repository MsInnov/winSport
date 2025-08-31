package com.mscode.presentation.home.di

import com.mscode.presentation.home.mapper.LeaguesUiMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object MapperModule {

    @Provides
    fun provideLeaguesUiMapper(): LeaguesUiMapper = LeaguesUiMapper()

}