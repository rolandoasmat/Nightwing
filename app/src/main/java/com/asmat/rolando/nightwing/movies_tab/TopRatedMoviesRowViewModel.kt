package com.asmat.rolando.nightwing.movies_tab

import com.asmat.rolando.nightwing.model.MovieSummary
import com.asmat.rolando.nightwing.model.Resource
import com.asmat.rolando.nightwing.model.mappers.UiModelMapper
import com.asmat.rolando.nightwing.repositories.MoviesRepository
import com.asmat.rolando.nightwing.ui.row_view.RowViewUiModel
import kotlinx.coroutines.flow.Flow

class TopRatedMoviesRowViewModel(
    private val moviesRepository: MoviesRepository,
    private val uiModelMapper: UiModelMapper
): RowViewModel<MovieSummary>() {

    override fun dataFlow(): Flow<Resource<List<MovieSummary>>> {
        return moviesRepository.topRatedMoviesSinglePage()
    }

    override fun transformDataToUiModel(data: List<MovieSummary>): RowViewUiModel {
        TODO("Not yet implemented")
    }

}