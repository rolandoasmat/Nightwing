package com.asmat.rolando.nightwing.popular_people

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.asmat.rolando.nightwing.model.mappers.UiModelMapper
import com.asmat.rolando.nightwing.repositories.PeopleRepository
import io.reactivex.disposables.CompositeDisposable

class PopularPeopleViewModel(
        private val peopleRepository: PeopleRepository,
        private val uiModelMapper: UiModelMapper): ViewModel() {

    private var page = 1

    private val _data = MutableLiveData<List<PopularPersonUiModel>>()
    val data: LiveData<List<PopularPersonUiModel>>
        get() = _data

    private val compositeDisposable = CompositeDisposable()

    init {
        fetch()
    }

    fun loadMore() {
        page += 1
        fetch()
    }

    private fun fetch() {
        val disposable = peopleRepository
                .getPopularPeople(page)
                .subscribe({ result ->
                    val mapped = uiModelMapper.map(result)
                    val currentList = _data.value ?: emptyList()
                    _data.postValue(currentList + mapped)
                }, {})
        compositeDisposable.add(disposable)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}