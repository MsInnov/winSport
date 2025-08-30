package com.mscode.data.remoteconfig.repository

import com.mscode.data.remoteconfig.datasource.LocalConfigDataSource
import com.mscode.data.remoteconfig.datasource.RemoteConfigDataSource
import com.mscode.data.remoteconfig.model.Path
import com.mscode.domain.common.WrapperResults
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertIs
import com.mscode.data.remoteconfig.model.Url
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue


class RemoteConfigRepositoryImplTest {

    private lateinit var remoteConfigDataSource: RemoteConfigDataSource
    private lateinit var localConfigDataSource: LocalConfigDataSource
    private lateinit var repository: RemoteConfigRepositoryImpl

    @BeforeEach
    fun setup() {
        remoteConfigDataSource = mockk()
        localConfigDataSource = mockk(relaxed = true) // relaxed: pas besoin de définir saveUrl
        repository = RemoteConfigRepositoryImpl(remoteConfigDataSource, localConfigDataSource)
    }

    @Test
    fun `updateRemoteConfig should save all urls and paths and return Success when data is available`() = runBlocking {
        val urls = mutableMapOf(
            "api_key" to "12345678", "url_thesportsdb" to "www.google.com", "path_all_leagues" to "all_leagues"
        )
        coEvery { remoteConfigDataSource.getRemoteConfig() } returns urls

        val result = repository.updateRemoteConfig()

        assertIs<WrapperResults.Success<Unit>>(result)
        coVerify { localConfigDataSource.saveUrl(
                Url(
                    "12345678",
                    "www.google.com"
                )
            )
        }
        coVerify { localConfigDataSource.savePath(
            Path(
                "path_all_leagues",
                "all_leagues"
            )
        )
        }
        coVerify(exactly = 1) { remoteConfigDataSource.getRemoteConfig() }
    }

    @Test
    fun `updateRemoteConfig should return Error when data is null`() = runBlocking {
        coEvery { remoteConfigDataSource.getRemoteConfig() } returns null

        val result = repository.updateRemoteConfig()

        assertTrue(result is WrapperResults.Error)
        verify(exactly = 0) { localConfigDataSource.saveUrl(any()) }
        verify(exactly = 0) { localConfigDataSource.savePath(any()) }
        coVerify(exactly = 1) { remoteConfigDataSource.getRemoteConfig() }
    }
}
