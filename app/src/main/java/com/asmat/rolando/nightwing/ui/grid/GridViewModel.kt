package com.asmat.rolando.nightwing.ui.grid

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable

abstract class GridViewModel : ViewModel() {

    private val compositeDisposable = CompositeDisposable()
    private var page = 1

    private val _items = MutableLiveData<List<GridItemUiModel>>()
    val items: LiveData<List<GridItemUiModel>>
        get() = _items

    abstract fun dataSource(page: Int): Single<List<GridItemUiModel>>

    fun load() {
        val request = dataSource(page).subscribe({ newItems ->
            val currentItems = _items.value ?: emptyList()
            _items.postValue(currentItems + newItems)
        }, {

        })
        compositeDisposable.add(request)
    }

    fun loadMore() {
        page += 1
        load()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}