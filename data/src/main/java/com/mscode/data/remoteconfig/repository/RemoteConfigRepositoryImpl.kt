package com.mscode.data.remoteconfig.repository

import com.mscode.data.remoteconfig.datasource.LocalConfigDataSource
import com.mscode.data.remoteconfig.datasource.RemoteConfigDataSource
import com.mscode.data.remoteconfig.model.Path
import com.mscode.data.remoteconfig.model.Url
import com.mscode.data.remoteconfig.model.key_api
import com.mscode.data.remoteconfig.model.url_the_sports_db
import com.mscode.domain.common.WrapperResults
import com.mscode.domain.remoteconfig.repository.RemoteConfigRepository
import java.lang.Exception

class RemoteConfigRepositoryImpl(
    private val remoteConfigDataSource: RemoteConfigDataSource,
    private val localConfigDataSource: LocalConfigDataSource
) : RemoteConfigRepository {

    override suspend fun updateRemoteConfig(): WrapperResults<Unit> =
        remoteConfigDataSource.getRemoteConfig().let { configsOptional ->
            configsOptional?.let { configs ->
                localConfigDataSource.saveUrl(
                    Url(
                        keyApi = configs.get(key_api) ?: return WrapperResults.Error(Exception()),
                        value = configs.get(url_the_sports_db) ?: return WrapperResults.Error(Exception())
                    )
                )
                configs.filter { it.key.contains("path") }.map { configPath ->
                    localConfigDataSource.savePath(
                        Path(
                            name = configPath.key,
                            value = configPath.value
                        )
                    )
                }
                WrapperResults.Success(Unit)
            } ?: WrapperResults.Error(Exception())
        }
}