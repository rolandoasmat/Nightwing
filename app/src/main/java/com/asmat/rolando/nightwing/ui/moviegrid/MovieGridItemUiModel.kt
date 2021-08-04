package com.asmat.rolando.nightwing.ui.moviegrid

import com.asmat.rolando.nightwing.database.entities.PopularMovie
import com.asmat.rolando.nightwing.utilities.URLUtils

data class MovieGridItemUiModel(
    val id: Int,
    val title: String,
    val posterURL: String?
)

fun PopularMovie.toMovieGridItemUiModel(): MovieGridItemUiModel {
    val posterURL = this.posterPath?.let { path ->
        URLUtils.getImageURL342(path)
    }
    return MovieGridItemUiModel(
        id = this.id,
        title = this.title ?: "Unknown title",
        posterURL = posterURL
    )
}