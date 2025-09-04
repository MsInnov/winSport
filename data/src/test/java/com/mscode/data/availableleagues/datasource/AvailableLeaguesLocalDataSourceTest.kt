package com.mscode.data.availableleagues.datasource

import com.mscode.domain.availableleagues.model.League
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test


class AvailableLeaguesLocalDataSourceTest {

    private lateinit var dataSource: AvailableLeaguesLocalDataSource

    @BeforeEach
    fun setUp() {
        dataSource = AvailableLeaguesLocalDataSource()
    }

    @Test
    fun `availableLeagues should be empty initially`() {
        assertTrue(dataSource.availableLeagues.isEmpty())
    }

    @Test
    fun `saveAvailableLeagues should update leagues list`() {
        // Given
        val league1 = League(
            id = 1,
            league = "Ligue1",
            sport = "Foot"
        )

        val league2 = League(
            id = 2,
            league = "Ligue2",
            sport = "Foot"
        )

        val leagues = listOf(league1, league2)

        // When
        dataSource.saveAvailableLeagues(leagues)

        // Then
        val savedAvailableLeagues = dataSource.availableLeagues
        assertEquals(2, savedAvailableLeagues.size)
        assertEquals("Ligue1", savedAvailableLeagues[0].league)
        assertEquals("Foot", savedAvailableLeagues[1].sport)
    }
}