package com.asmat.rolando.popularmovies.database.entities

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "movies")
class Movie(@PrimaryKey
            val id: Int,
            val title: String,
            @ColumnInfo(name = "poster_path")
            val posterPath: String?,
            @ColumnInfo(name = "backdrop_path")
            val backdropPath: String?,
            val overview: String,
            @ColumnInfo(name = "vote_average")
            val voteAverage: Double,
            @ColumnInfo(name = "release_date")
            val releaseDate: String)