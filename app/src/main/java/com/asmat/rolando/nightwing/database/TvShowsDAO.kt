package com.asmat.rolando.nightwing.database

import androidx.room.*
import com.asmat.rolando.nightwing.database.entities.SavedTvShow
import kotlinx.coroutines.flow.Flow

@Dao
interface TvShowsDAO {

    @Query("SELECT * FROM saved_tv_shows WHERE id LIKE :id")
    fun getSavedTvShow(id: Int): Flow<SavedTvShow?>

    @Query("SELECT * FROM saved_tv_shows")
    fun getAllSavedTvShows(): Flow<List<SavedTvShow>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSavedTvShow(tvShow: SavedTvShow)

    @Query("DELETE FROM saved_tv_shows WHERE id LIKE :id ")
    suspend fun deleteSavedTvShow(id: Int)

}