package com.asmat.rolando.nightwing.tv_shows_tab.on_the_air

import com.asmat.rolando.nightwing.model.mappers.UiModelMapper
import com.asmat.rolando.nightwing.repositories.TvShowsRepository
import com.asmat.rolando.nightwing.ui.grid.GridItemUiModel
import com.asmat.rolando.nightwing.ui.grid.GridViewModel
import io.reactivex.Single

class OnTheAirTvShowsViewModel(
        private val tvShowsRepository: TvShowsRepository,
        private val uiModelMapper: UiModelMapper): GridViewModel() {

    override fun dataSource(page: Int): Single<List<GridItemUiModel>> {
        return tvShowsRepository.getOnTheAirTvShows(page).map { uiModelMapper.mapToGridItems(it) }
    }
}