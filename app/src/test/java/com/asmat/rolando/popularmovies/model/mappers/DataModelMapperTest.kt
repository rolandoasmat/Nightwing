package com.asmat.rolando.popularmovies.model.mappers

import com.asmat.rolando.popularmovies.TestObjectsFactory
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class DataModelMapperTest {

    @Mock
    lateinit var mockMovieMapper: MovieMapper
    @Mock
    lateinit var mockFavoriteMovieMapper: FavoriteMovieMapper
    @Mock
    lateinit var mockWatchLaterMovieMapper: WatchLaterMovieMapper

    lateinit var mapper: DataModelMapper

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        mapper = DataModelMapper(mockMovieMapper, mockFavoriteMovieMapper, mockWatchLaterMovieMapper)
    }

    @Test
    fun map_listOfMoviesResponse() {
        // Arrange
        val moviesData = TestObjectsFactory.moviesResponse()
        val data = listOf(moviesData, moviesData, moviesData)

        // Act
        mapper.map(data)

        // Assert
        verify(mockMovieMapper, times(3)).from(moviesData)
    }

}