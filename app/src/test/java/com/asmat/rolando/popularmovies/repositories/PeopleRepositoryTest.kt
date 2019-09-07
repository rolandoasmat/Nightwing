package com.asmat.rolando.popularmovies.repositories

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.asmat.rolando.popularmovies.TestObjectsFactory
import com.asmat.rolando.popularmovies.networking.the.movie.db.TheMovieDBClient
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Single
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class PeopleRepositoryTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    lateinit var mockTheMovieDBClient: TheMovieDBClient

    lateinit var repository: PeopleRepository

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        repository = PeopleRepository(mockTheMovieDBClient)
    }

    // Network

    @Test
    fun getPersonDetails_successfulResponse() {
        // Arrange
        val id = 4444
        val expected = TestObjectsFactory.personDetailsResponse()
        whenever(mockTheMovieDBClient.getPersonDetails(id)).thenReturn(Single.just(expected))

        // Act
        val actual = repository.getPersonDetails(id).test()

        // Assert
        verify(mockTheMovieDBClient).getPersonDetails(id)
        actual.assertValue(expected)
    }

    @Test
    fun getPersonMovieCredits_successfulResponse() {
        // Arrange
        val id = 4444
        val expected = TestObjectsFactory.personMovieCredits()
        whenever(mockTheMovieDBClient.getPersonMovieCredits(id)).thenReturn(Single.just(expected))

        // Act
        val actual = repository.getPersonMovieCredits(id).test()

        // Assert
        verify(mockTheMovieDBClient).getPersonMovieCredits(id)
        actual.assertValue(expected)
    }

}