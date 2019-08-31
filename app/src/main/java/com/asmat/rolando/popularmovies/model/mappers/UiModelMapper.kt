package com.asmat.rolando.popularmovies.model.mappers

import com.asmat.rolando.popularmovies.model.Movie
import com.asmat.rolando.popularmovies.networking.the.movie.db.models.PersonMovieCredits
import com.asmat.rolando.popularmovies.ui.castdetails.personmoviecredits.MovieCreditUiModel
import com.asmat.rolando.popularmovies.ui.castdetails.personmoviecredits.PersonMovieCreditsUiModel
import com.asmat.rolando.popularmovies.ui.moviegrid.MovieGridItemUiModel
import com.asmat.rolando.popularmovies.utilities.URLUtils
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Maps data objects to UiModel classes.
 */
@Singleton
class UiModelMapper @Inject constructor() {

    fun map(movies: List<Movie>?): List<MovieGridItemUiModel>? {
        return movies?.map {
            val posterURL = it.posterPath?.let { url -> URLUtils.getImageURL342(url)}
            MovieGridItemUiModel(it.title, posterURL)
        }
    }

    fun map(data: PersonMovieCredits): PersonMovieCreditsUiModel {
        val mapped = data.cast?.map {
            val posterURL = it.poster_path?.let { url -> URLUtils.getImageURL342(url) }
            MovieCreditUiModel(posterURL, it.character)
        }
        val movieCreditsWithBackdropImage = data.cast?.filter { it.backdrop_path != null }

        val backdropURL = if (movieCreditsWithBackdropImage?.isEmpty() == true) {
            null
        } else {
            movieCreditsWithBackdropImage?.random()?.backdrop_path?.let { URLUtils.getImageURL780(it) }
        }
        return PersonMovieCreditsUiModel(backdropURL, mapped ?: emptyList())
    }

}