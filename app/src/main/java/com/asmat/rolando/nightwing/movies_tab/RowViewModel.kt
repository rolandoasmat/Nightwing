package com.asmat.rolando.nightwing.movies_tab

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asmat.rolando.nightwing.model.Resource
import com.asmat.rolando.nightwing.ui.row_view.RowViewUiModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

abstract class RowViewModel<DomainDataModel>(): ViewModel() {

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean>
        get() = _loading

    private val _error = MutableLiveData<Boolean>()
    val error: LiveData<Boolean>
        get() = _error

    private val _rowViewUiModel = MutableLiveData<RowViewUiModel>()
    val rowViewUiModel: LiveData<RowViewUiModel>
        get() = _rowViewUiModel

    abstract fun dataFlow(): Flow<Resource<List<DomainDataModel>>>

    abstract fun transformDataToUiModel(data: List<DomainDataModel>): RowViewUiModel

    fun load() {
        viewModelScope.launch {
            dataFlow().collect {
                handleResource(it)
            }
        }
    }

    private fun handleResource(resource: Resource<List<DomainDataModel>>) {
        _loading.value = resource is Resource.Loading
        _error.value = resource is Resource.Error
        resource.data?.let { items ->
            _rowViewUiModel.value = transformDataToUiModel(items)
        }
    }
}