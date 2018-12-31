package com.asmat.rolando.popularmovies.database.entities

import android.arch.persistence.room.*

@Entity(tableName = "favorite_movies")
data class FavoriteMovie(val posterPath: String?,
                         val overview: String,
                         val releaseDate: String,
                         @PrimaryKey val id: Int,
                         val title: String,
                         val backdropPath: String?,
                         val popularity: Double)