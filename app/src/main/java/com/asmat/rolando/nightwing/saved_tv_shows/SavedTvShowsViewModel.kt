package com.asmat.rolando.nightwing.saved_tv_shows

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.asmat.rolando.nightwing.model.mappers.UiModelMapper
import com.asmat.rolando.nightwing.repositories.TvShowsRepository
import kotlinx.coroutines.flow.map

class SavedTvShowsViewModel(
        tvShowsRepository: TvShowsRepository,
        uiModelMapper: UiModelMapper) : ViewModel() {

    val tvShows = tvShowsRepository
            .getAllSavedTvShows()
            .map {
                uiModelMapper.mapSavedTvShowsToGridItems(it)
            }.asLiveData()
}