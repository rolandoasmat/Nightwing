package com.asmat.rolando.nightwing.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.asmat.rolando.nightwing.networking.models.MoviesResponse

@Entity(tableName = "popular_movies")
class PopularMovie(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val movieId: Int?,
    val title: String?,
    val posterPath: String?
)

fun MoviesResponse.Movie.toPopularMovie(): PopularMovie {
    return PopularMovie(
        id = 0,
        movieId = this.id,
        title = this.original_title,
        posterPath = this.poster_path
    )
}