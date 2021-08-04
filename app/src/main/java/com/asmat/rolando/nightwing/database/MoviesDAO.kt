package com.asmat.rolando.nightwing.database

import androidx.paging.PagingSource
import androidx.room.*
import com.asmat.rolando.nightwing.database.entities.PopularMovie
import com.asmat.rolando.nightwing.database.entities.SavedMovie
import kotlinx.coroutines.flow.Flow

@Dao
interface MoviesDAO {

    //region Popular Movies
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllPopularMovies(movies: List<PopularMovie>)

    @Query("SELECT * FROM popular_movies order by id")
    fun popularMoviesPagingSource(): PagingSource<Int, PopularMovie>

    @Query("DELETE FROM popular_movies")
    suspend fun clearAllPopularMovies()
    //endregion

    @Query("SELECT * FROM saved_movies WHERE id LIKE :id")
    fun getSavedMovie(id: Int): Flow<SavedMovie?>

    @Query("SELECT * FROM saved_movies")
    fun getAllSavedMovies(): Flow<List<SavedMovie>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSavedMovie(movie: SavedMovie)

    @Query("DELETE FROM saved_movies WHERE id LIKE :id ")
    suspend fun deleteSavedMovie(id: Int)

}