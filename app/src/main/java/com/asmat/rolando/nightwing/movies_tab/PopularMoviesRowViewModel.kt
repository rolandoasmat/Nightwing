package com.asmat.rolando.nightwing.movies_tab

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asmat.rolando.nightwing.database.entities.PopularMovie
import com.asmat.rolando.nightwing.model.Resource
import com.asmat.rolando.nightwing.model.mappers.UiModelMapper
import com.asmat.rolando.nightwing.repositories.MoviesRepository
import com.asmat.rolando.nightwing.ui.row_view.RowViewUiModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class PopularMoviesRowViewModel(
    private val moviesRepository: MoviesRepository,
    private val uiModelMapper: UiModelMapper
): ViewModel() {

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean>
        get() = _loading

    private val _rowViewUiModel = MutableLiveData<RowViewUiModel>()
    val rowViewUiModel: LiveData<RowViewUiModel>
        get() = _rowViewUiModel

    fun load() {
        viewModelScope.launch {
            moviesRepository.popularMoviesSinglePage().collect {
                handleResource(it)
            }
        }
    }

    private fun handleResource(resource: Resource<List<PopularMovie>>) {
        _loading.value = resource is Resource.Loading
        resource.data?.let { popularMovies ->
            val uiModel = uiModelMapper.mapPopularMoviesToRowViewUiModel(popularMovies)
            _rowViewUiModel.value = uiModel
        }
    }
}