package com.asmat.rolando.nightwing.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.asmat.rolando.nightwing.networking.models.MoviesResponse

@Entity(tableName = "popular_movies")
class PopularMovie(
    @PrimaryKey
    val primaryKey: String,
    val id: Int,
    val title: String?,
    val posterPath: String?
)

fun MoviesResponse.Movie.toPopularMovie(): PopularMovie {
    val primaryKey = this.id!!
    return PopularMovie(
        primaryKey = primaryKey.toString(),
        id = id!!,
        title = this.original_title,
        posterPath = this.poster_path
    )
}