package com.asmat.rolando.popularmovies.database.entities

import android.arch.persistence.room.*
import com.asmat.rolando.popularmovies.networking.the.movie.db.models.MoviesResponse

@Entity(tableName = "favorite_movies")
data class FavoriteMovie(@PrimaryKey val id: Int,
                         val poster_path: String?,
                         val adult: Boolean,
                         val overview: String,
                         val release_date: String,
                         val original_title: String,
                         val original_language: String,
                         val title: String,
                         val backdrop_path: String?,
                         val popularity: Double,
                         val vote_count: Int,
                         val video: Boolean,
                         val vote_average: Double) {

    constructor(data: MoviesResponse.Movie): this( // TODO store List of genre ids
            data.id,
            data.poster_path,
            data.adult,
            data.overview,
            data.release_date,
            data.original_title,
            data.original_language,
            data.title,
            data.backdrop_path,
            data.popularity,
            data.vote_count,
            data.video,
            data.vote_average)
}