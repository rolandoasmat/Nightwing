package com.asmat.rolando.nightwing.repositories

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.asmat.rolando.nightwing.TestObjectsFactory
import com.asmat.rolando.nightwing.database.DatabaseRepository
import com.asmat.rolando.nightwing.database.NightwingDatabase
import com.asmat.rolando.nightwing.model.mappers.MovieMapper
import com.asmat.rolando.nightwing.networking.TheMovieDBClient
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class MoviesRepositoryTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    lateinit var databaseRepository: DatabaseRepository
    @Mock
    lateinit var mockTheMovieDBClient: TheMovieDBClient
    @Mock
    lateinit var schedulersProvider: SchedulersProvider
    @Mock
    lateinit var database: NightwingDatabase
    @Mock
    lateinit var movieMapper: MovieMapper

    lateinit var repository: MoviesRepository

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        whenever(schedulersProvider.ioScheduler).thenReturn(Schedulers.trampoline())
        whenever(schedulersProvider.mainScheduler).thenReturn(Schedulers.trampoline())
        repository = MoviesRepository(
            databaseRepository,
            mockTheMovieDBClient,
            schedulersProvider,
            database,
            movieMapper
        )
    }

    // DB tests

    // Favorite movies

    @Test
    fun getFavoriteMovie_databaseManagerInvoked() {
        // Arrange
        val id = 4444
//        val expected = MutableLiveData<FavoriteMovie>()
//        expected.value = TestObjectsFactory.favoriteMovie()
//        whenever(mockDatabaseManager.getFavoriteMovie(id)).thenReturn(expected)
//
//        // Act
//        val actual = repository.getFavoriteMovie(id)
//
//        // Assert
//        verify(mockDatabaseManager).getFavoriteMovie(id)
//        Assert.assertEquals(expected.value, actual.value)
    }

    @Test
    fun removeFavoriteMovie_databaseManagerInvoked() {
//        // Arrange
//        val id = 4444
//        whenever(mockDatabaseManager.deleteFavoriteMovie(id)).thenReturn(Single.just(1))
//
//        // Act
//        repository.removeFavoriteMovie(id)
//
//        // Assert
//        verify(mockDatabaseManager).deleteFavoriteMovie(id)
    }

    @Test
    fun insertFavoriteMovie_databaseManagerInvoked() {
        // Arrange
//        val movie = TestObjectsFactory.favoriteMovie()
//        whenever(mockDatabaseManager.addFavoriteMovie(movie)).thenReturn(Completable.complete())
//
//        // Act
//        repository.insertFavoriteMovie(movie)
//
//        // Assert
//        verify(mockDatabaseManager).addFavoriteMovie(movie)
    }

    @Test
    fun getAllFavoriteMovies_databaseManagerInvoked() {
        // Arrange
//        val expected = MutableLiveData<List<FavoriteMovie>>()
//        expected.value = TestObjectsFactory.favoriteMovies()
//        whenever(mockDatabaseManager.getAllFavoriteMovies()).thenReturn(expected)
//
//        // Act
//        val actual = repository.getAllFavoriteMovies()
//
//        // Assert
//        verify(mockDatabaseManager).getAllFavoriteMovies()
//        Assert.assertEquals(expected.value, actual.value)
    }

    // Watch later movies

    @Test
    fun getWatchLaterMovie_databaseManagerInvoked() {
        // Arrange
//        val id = 4444
//        val expected = MutableLiveData<WatchLaterMovie>()
//        expected.value = TestObjectsFactory.watchLaterMovie()
//        whenever(mockDatabaseManager.getWatchLaterMovie(id)).thenReturn(expected)
//
//        // Act
//        val actual = repository.getWatchLaterMovie(id)
//
//        // Assert
//        verify(mockDatabaseManager).getWatchLaterMovie(id)
//        Assert.assertEquals(expected.value, actual.value)
    }

    @Test
    fun removeWatchLaterMovie_databaseManagerInvoked() {
        // Arrange
//        val id = 4444
//        whenever(mockDatabaseManager.deleteWatchLaterMovie(id)).thenReturn(Single.just(1))
//
//        // Act
//        repository.removeWatchLaterMovie(id)
//
//        // Assert
//        verify(mockDatabaseManager).deleteWatchLaterMovie(id)
    }

    @Test
    fun insertWatchLaterMovie_databaseManagerInvoked() {
        // Arrange
//        val movie = TestObjectsFactory.watchLaterMovie()
//        whenever(mockDatabaseManager.addWatchLaterMovie(movie)).thenReturn(Completable.complete())
//
//        // Act
//        repository.insertWatchLaterMovie(movie)
//
//        // Assert
//        verify(mockDatabaseManager).addWatchLaterMovie(movie)
    }

    @Test
    fun getAllWatchLaterMovies_databaseManagerInvoked() {
        // Arrange
//        val expected = MutableLiveData<List<WatchLaterMovie>>()
//        expected.value = TestObjectsFactory.watchLaterMovies()
//        whenever(mockDatabaseManager.getAllWatchLaterMovies()).thenReturn(expected)
//
//        // Act
//        val actual = repository.getAllWatchLaterMovies()
//
//        // Assert
//        verify(mockDatabaseManager).getAllWatchLaterMovies()
//        Assert.assertEquals(expected.value, actual.value)
    }

    // Network

    @Test
    fun getMovieVideos_successfulResponse() {
        // Arrange
        val id = 4444
        val expected = TestObjectsFactory.videosResponse()
        whenever(mockTheMovieDBClient.getMovieVideos(id)).thenReturn(Single.just(expected))

        // Act
        val actual = repository.getMovieVideos(id).test()

        // Assert
        verify(mockTheMovieDBClient).getMovieVideos(id)
        actual.assertValue(expected)
    }

    @Test
    fun getMovieReviews_successfulResponse() {
        // Arrange
        val id = 4444
        val expected = TestObjectsFactory.movieReviews()
        whenever(mockTheMovieDBClient.getMovieReviews(id)).thenReturn(Single.just(expected))

        // Act
        val actual = repository.getMovieReviews(id).test()

        // Assert
        verify(mockTheMovieDBClient).getMovieReviews(id)
        actual.assertValue(expected)
    }

    @Test
    fun getMovieCredits_successfulResponse() {
        // Arrange
        val id = 4444
        val expected = TestObjectsFactory.movieCredits()
        whenever(mockTheMovieDBClient.getMovieCredits(id)).thenReturn(Single.just(expected))

        // Act
        val actual = repository.getMovieCredits(id).test()

        // Assert
        verify(mockTheMovieDBClient).getMovieCredits(id)
        actual.assertValue(expected)
    }

}