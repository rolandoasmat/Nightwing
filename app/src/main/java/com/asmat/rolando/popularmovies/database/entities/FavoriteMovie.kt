package com.asmat.rolando.popularmovies.database.entities

import android.arch.persistence.room.*

@Entity(tableName = "favorite_movies")
data class FavoriteMovie(@PrimaryKey
                         val id: Int,
                         @ColumnInfo(name = "poster_path")
                         val posterPath: String?,
                         val overview: String,
                         @ColumnInfo(name = "release_date")
                         val releaseDate: String,
                         val title: String,
                         @ColumnInfo(name = "backdrop_path")
                         val backdropPath: String?,
                         @ColumnInfo(name = "vote_average")
                         val voteAverage: Double)