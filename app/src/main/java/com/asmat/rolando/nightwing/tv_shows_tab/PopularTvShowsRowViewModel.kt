package com.asmat.rolando.nightwing.tv_shows_tab

import com.asmat.rolando.nightwing.model.MovieSummary
import com.asmat.rolando.nightwing.model.Resource
import com.asmat.rolando.nightwing.model.mappers.UiModelMapper
import com.asmat.rolando.nightwing.movies_tab.MoviesRowViewModel
import com.asmat.rolando.nightwing.repositories.TvShowsRepository
import kotlinx.coroutines.flow.Flow

class PopularTvShowsRowViewModel(
    private val tvShowsRepository: TvShowsRepository,
    uiModelMapper: UiModelMapper
): MoviesRowViewModel(uiModelMapper) {

    override fun moviesFlow(): Flow<Resource<List<MovieSummary>>> {
        return tvShowsRepository.popularTvShowsSinglePage()
    }

}