package com.asmat.rolando.popularmovies.cast_details

import com.asmat.rolando.popularmovies.cast_details.MovieCreditUiModel

data class PersonMovieCreditsUiModel(
        val backdropURL: String?,
        val movies: List<MovieCreditUiModel>)