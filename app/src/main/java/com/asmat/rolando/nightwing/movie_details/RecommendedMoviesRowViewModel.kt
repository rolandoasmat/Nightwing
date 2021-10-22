package com.asmat.rolando.nightwing.movie_details

import com.asmat.rolando.nightwing.model.MovieSummary
import com.asmat.rolando.nightwing.model.Resource
import com.asmat.rolando.nightwing.model.mappers.UiModelMapper
import com.asmat.rolando.nightwing.movies_tab.RowViewModel
import com.asmat.rolando.nightwing.repositories.MoviesRepository
import com.asmat.rolando.nightwing.ui.row_view.RowViewUiModel
import kotlinx.coroutines.flow.Flow

class RecommendedMoviesRowViewModel(
    private val moviesRepository: MoviesRepository,
    private val uiModelMapper: UiModelMapper,
    private val movieID: Int
): RowViewModel<MovieSummary>() {

    override fun dataFlow(): Flow<Resource<List<MovieSummary>>> {
        return moviesRepository.getMovieRecommendations(movieID)
    }

    override fun transformDataToUiModel(data: List<MovieSummary>): RowViewUiModel {
        return uiModelMapper.mapMoviesToRowViewUiModel(data)
    }

}