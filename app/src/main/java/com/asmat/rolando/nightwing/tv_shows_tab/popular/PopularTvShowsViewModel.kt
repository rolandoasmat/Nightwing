package com.asmat.rolando.nightwing.tv_shows_tab.popular

import com.asmat.rolando.nightwing.model.mappers.UiModelMapper
import com.asmat.rolando.nightwing.repositories.TvShowsRepository
import com.asmat.rolando.nightwing.ui.grid.GridItemUiModel
import com.asmat.rolando.nightwing.ui.grid.GridViewModel
import io.reactivex.Single

class PopularTvShowsViewModel(
        private val tvShowsRepository: TvShowsRepository,
        private val uiModelMapper: UiModelMapper): GridViewModel() {

    override fun dataSource(page: Int): Single<List<GridItemUiModel>> {
        return tvShowsRepository.getPopularTvShows(page).map { uiModelMapper.mapToGridItems(it) }
    }
}