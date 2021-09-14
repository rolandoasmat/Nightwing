package com.asmat.rolando.nightwing.similar_movies

import com.asmat.rolando.nightwing.model.MovieSummary
import com.asmat.rolando.nightwing.model.Resource
import com.asmat.rolando.nightwing.model.mappers.UiModelMapper
import com.asmat.rolando.nightwing.movies_tab.MoviesRowViewModel
import com.asmat.rolando.nightwing.repositories.MoviesRepository
import kotlinx.coroutines.flow.Flow

class SimilarMoviesRowViewModel (
    moviesRepository: MoviesRepository,
    uiModelMapper: UiModelMapper,
    movieId: Int
): MoviesRowViewModel(moviesRepository, uiModelMapper) {

    override fun moviesFlow(): Flow<Resource<List<MovieSummary>>> {
        return moviesRepository.getSimilarMoviesSuspend(movieId)
    }

}