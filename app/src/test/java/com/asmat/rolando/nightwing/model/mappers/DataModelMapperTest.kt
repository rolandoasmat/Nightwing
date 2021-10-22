package com.asmat.rolando.nightwing.model.mappers

import com.asmat.rolando.nightwing.TestObjectsFactory
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class DataModelMapperTest {

    @Mock
    lateinit var mockMovieMapper: MovieMapper

    lateinit var mapper: DataModelMapper

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        mapper = DataModelMapper(mockMovieMapper)
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