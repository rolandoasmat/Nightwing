package com.asmat.rolando.nightwing.tv_shows_tab

import com.asmat.rolando.nightwing.model.Resource
import com.asmat.rolando.nightwing.model.TvShowSummary
import com.asmat.rolando.nightwing.model.mappers.UiModelMapper
import com.asmat.rolando.nightwing.movies_tab.RowViewModel
import com.asmat.rolando.nightwing.repositories.TvShowsRepository
import com.asmat.rolando.nightwing.ui.row_view.RowViewUiModel
import kotlinx.coroutines.flow.Flow

class OnTheAirTvShowsRowViewModel(
    private val tvShowsRepository: TvShowsRepository,
    private val uiModelMapper: UiModelMapper
): RowViewModel<TvShowSummary>() {

    override fun dataFlow(): Flow<Resource<List<TvShowSummary>>> {
        return tvShowsRepository.popularTvShowsSinglePage()
    }

    override fun transformDataToUiModel(data: List<TvShowSummary>): RowViewUiModel {
        return uiModelMapper.mapTvShowsToRowViewUiModel(data)
    }

}