package com.asmat.rolando.nightwing.movies_tab

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asmat.rolando.nightwing.model.MovieSummary
import com.asmat.rolando.nightwing.model.Resource
import com.asmat.rolando.nightwing.model.mappers.UiModelMapper
import com.asmat.rolando.nightwing.ui.row_view.RowViewUiModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

// TODO rename to more generic naming since reused for TV rows
abstract class MoviesRowViewModel(
    protected val uiModelMapper: UiModelMapper
): ViewModel() {

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean>
        get() = _loading

    private val _error = MutableLiveData<Boolean>()
    val error: LiveData<Boolean>
        get() = _error

    private val _rowViewUiModel = MutableLiveData<RowViewUiModel>()
    val rowViewUiModel: LiveData<RowViewUiModel>
        get() = _rowViewUiModel

    abstract fun moviesFlow(): Flow<Resource<List<MovieSummary>>>

    fun load() {
        viewModelScope.launch {
            moviesFlow().collect {
                handleResource(it)
            }
        }
    }

    private fun handleResource(resource: Resource<List<MovieSummary>>) {
        _loading.value = resource is Resource.Loading
        _error.value = resource is Resource.Error
        resource.data?.let { movies ->
            val uiModel = uiModelMapper.mapMoviesToRowViewUiModel(movies)
            _rowViewUiModel.value = uiModel
        }
    }
}