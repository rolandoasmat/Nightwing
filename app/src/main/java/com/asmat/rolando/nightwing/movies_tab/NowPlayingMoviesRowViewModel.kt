package com.asmat.rolando.nightwing.movies_tab

import com.asmat.rolando.nightwing.model.MovieSummary
import com.asmat.rolando.nightwing.model.Resource
import com.asmat.rolando.nightwing.model.mappers.UiModelMapper
import com.asmat.rolando.nightwing.repositories.MoviesRepository
import kotlinx.coroutines.flow.Flow

class NowPlayingMoviesRowViewModel(
    moviesRepository: MoviesRepository,
    uiModelMapper: UiModelMapper
): MoviesRowViewModel(moviesRepository, uiModelMapper) {

    override fun moviesFlow(): Flow<Resource<List<MovieSummary>>> {
        return moviesRepository.nowPlayingMoviesSinglePage()
    }

}