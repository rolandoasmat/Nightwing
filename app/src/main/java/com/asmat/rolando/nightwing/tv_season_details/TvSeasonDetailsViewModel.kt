package com.asmat.rolando.nightwing.tv_season_details

import androidx.lifecycle.*
import com.asmat.rolando.nightwing.model.Resource
import com.asmat.rolando.nightwing.model.mappers.UiModelMapper
import com.asmat.rolando.nightwing.repositories.TvShowsRepository
import com.asmat.rolando.nightwing.tv_season_details.domain.TvShowSeason

class TvSeasonDetailsViewModel(
    private val tvShowsRepository: TvShowsRepository,
    private val uiModelMapper: UiModelMapper
): ViewModel() {

    private val _request = MutableLiveData<Request>()
    private val _response = _request.switchMap { request ->
        tvShowsRepository
            .getTvShowSeasonDetails(request.tvShowId, request.seasonNumber)
            .asLiveData()
    }

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean>
        get() = _loading

    private val _episodes = MediatorLiveData<TvSeasonEpisodesUiModel>().apply {
        addSource(_response) { resource ->
            handleResponse(resource)
        }
    }
    val episodes: LiveData<TvSeasonEpisodesUiModel>
        get() = _episodes

    fun fetchEpisodes(
        tvShowId: Int,
        seasonNumber: Int) {
        _request.value = Request(tvShowId, seasonNumber)
    }

    private fun handleResponse(resource: Resource<TvShowSeason>) {
        _loading.value = resource is Resource.Loading
        resource.data?.let { data ->
            val uiModel = uiModelMapper.mapTvShowSeason(data)
            _episodes.value = uiModel
        }
    }

    private class Request(
        val tvShowId: Int,
        val seasonNumber: Int)
}