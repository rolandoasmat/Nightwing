package com.asmat.rolando.popularmovies.cast_details

data class PersonMovieCreditsUiModel(
        val backdropURL: String?,
        val movies: List<MovieCreditUiModel>)