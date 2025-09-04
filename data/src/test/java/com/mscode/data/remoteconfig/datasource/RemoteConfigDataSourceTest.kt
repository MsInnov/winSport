package com.mscode.data.remoteconfig.datasource

import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.mscode.data.remoteconfig.model.datasUrl
import com.mscode.data.remoteconfig.model.key_api
import com.mscode.data.remoteconfig.model.path_all_leagues
import com.mscode.data.remoteconfig.model.path_search_all_teams
import com.mscode.data.remoteconfig.model.paths
import com.mscode.data.remoteconfig.model.url_the_sports_db
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull

class RemoteConfigDataSourceTest {

    private lateinit var remoteConfig: FirebaseRemoteConfig
    private lateinit var dataSource: RemoteConfigDataSource

    @BeforeEach
    fun setUp() {
        remoteConfig = mockk(relaxed = true)
        dataSource = RemoteConfigDataSource(remoteConfig)
    }

    @Test
    fun `getRemoteConfig should return map when Fetch Is Successful`() = runTest {
        // Given
        val mockTask: Task<Boolean> = mockk(relaxed = true)
        every { mockTask.isSuccessful } returns true
        every { remoteConfig.fetchAndActivate() } returns mockTask
        // Mock URLs
        every { remoteConfig.getString(url_the_sports_db) } returns "https://example.com"
        every { remoteConfig.getString(key_api) } returns "12345678"
        every { remoteConfig.getString(path_all_leagues) } returns "path1"
        every { remoteConfig.getString(path_search_all_teams) } returns "path2"


        // Simuler appel au listener
        every {
            mockTask.addOnCompleteListener(any())
        } answers {
            val listener = arg<OnCompleteListener<Boolean>>(0)
            listener.onComplete(mockTask)
            mockTask
        }

        // When
        val result = dataSource.getRemoteConfig()

        // Then
        assertNotNull(result)
        assertEquals(datasUrl.size + paths.size, result!!.size)
        assertEquals(result.get(url_the_sports_db), "https://example.com")
        assertEquals(result.get(key_api), "12345678")
        assertEquals(result.get(path_all_leagues), "path1")
        assertEquals(result.get(path_search_all_teams), "path2")
    }

    @Test
    fun `getRemoteConfig should return null when Fetch fails`() = runTest {
        // Given
        val mockTask: Task<Boolean> = mockk(relaxed = true)
        every { mockTask.isSuccessful } returns false
        every { remoteConfig.fetchAndActivate() } returns mockTask

        every {
            mockTask.addOnCompleteListener(any())
        } answers {
            val listener = arg<OnCompleteListener<Boolean>>(0)
            listener.onComplete(mockTask)
            mockTask
        }

        // When
        val result = dataSource.getRemoteConfig()

        // Then
        assertNull(result)
    }
}
