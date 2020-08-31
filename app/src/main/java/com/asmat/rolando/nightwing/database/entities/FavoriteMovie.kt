package com.asmat.rolando.nightwing.database.entities

import androidx.room.*

@Entity(tableName = "favorite_movies")
data class FavoriteMovie(
        @PrimaryKey val id: Int,
        @ColumnInfo(name = "poster_url")
        val posterURL: String?,
        @ColumnInfo(name = "release_date")
        val releaseDate: String,
        val title: String)