package com.asmat.rolando.nightwing.movies_tab

import com.asmat.rolando.nightwing.model.MovieSummary
import com.asmat.rolando.nightwing.model.Resource
import com.asmat.rolando.nightwing.model.mappers.UiModelMapper
import com.asmat.rolando.nightwing.repositories.MoviesRepository
import kotlinx.coroutines.flow.Flow

class TopRatedMoviesRowViewModel(
    private val moviesRepository: MoviesRepository,
    uiModelMapper: UiModelMapper
): MoviesRowViewModel(uiModelMapper) {

    override fun moviesFlow(): Flow<Resource<List<MovieSummary>>> {
        return moviesRepository.topRatedMoviesSinglePage()
    }

}