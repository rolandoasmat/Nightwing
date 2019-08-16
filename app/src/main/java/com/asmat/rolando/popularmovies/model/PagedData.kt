package com.asmat.rolando.popularmovies.model

import androidx.lifecycle.MutableLiveData
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers

/**
 * Encapsulates the states and data of a paginated
 * data request.
 */
abstract class PagedData<T> {

    private var pageToLoad = 1

    val data = MutableLiveData<List<T>>()
    val loading = MutableLiveData<Boolean>()
    val loadingMore = MutableLiveData<Boolean>()
    val error = MutableLiveData<Throwable>()
    val errorLoadingMore = MutableLiveData<Throwable>()

    /**
     * What data to fetch
     */
    abstract fun fetchData(pageToLoad: Int): Single<List<T>>

    /**
     * Load first page of data
     */
    fun load() {
        pageToLoad = 1
        error.value = null
        loading.value = true
        fetchData(pageToLoad)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    loading.value = false
                    data.value = response
                }, { loadError ->
                    loading.value = false
                    error.value = loadError
                })
    }

    /**
     * Load next page of data and add it to the current list of data
     */
    fun loadMore() {
        pageToLoad++
        errorLoadingMore.value = null
        loadingMore.value = true
        fetchData(pageToLoad)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    val currentItems = data.value ?: emptyList()
                    val newList = currentItems + response
                    loadingMore.value = false
                    data.value = newList
                }, { loadError ->
                    loadingMore.value = false
                    errorLoadingMore.value = loadError
                    pageToLoad--
                })
    }

    /**
     * Gets the item at a given position, null otherwise
     */
    fun getItem(index: Int): T? {
        return data.value?.getOrNull(index)
    }

}