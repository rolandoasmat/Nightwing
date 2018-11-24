package com.asmat.rolando.popularmovies.database.entities

import android.arch.persistence.room.*
import com.asmat.rolando.popularmovies.networking.the.movie.db.models.MovieDetailsResponse

@Entity(tableName = "watch_later_movies")
data class WatchLaterMovie(@PrimaryKey var id: Int,
                         var poster_path: String?) {

    constructor(data: MovieDetailsResponse): this(
            data.id,
            data.poster_path)
}