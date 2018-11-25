package com.asmat.rolando.popularmovies.database.entities

import android.arch.persistence.room.*
import com.asmat.rolando.popularmovies.networking.the.movie.db.models.MoviesResponse

@Entity(tableName = "watch_later_movies")
data class WatchLaterMovie(@PrimaryKey override var id: Int,
                           override var poster_path: String?,
                           override var adult: Boolean,
                           override var overview: String,
                           override var release_date: String,
                           override var genre_ids: List<Int>,
                           override var original_title: String,
                           override var original_language: String,
                           override var title: String,
                           override var backdrop_path: String?,
                           override var popularity: Double,
                           override var vote_count: Int,
                           override var video: Boolean,
                           override var vote_average: Double): MoviesResponse.Movie(poster_path, adult, overview, release_date, genre_ids, id, original_title, original_language, title, backdrop_path, popularity, vote_count, video, vote_average) {

    constructor(data: MoviesResponse.Movie): this(
            data.id,
            data.poster_path,
            data.adult,
            data.overview,
            data.release_date,
            data.genre_ids,
            data.original_title,
            data.original_language,
            data.title,
            data.backdrop_path,
            data.popularity,
            data.vote_count,
            data.video,
            data.vote_average)
}