package com.asmat.rolando.nightwing.model

import androidx.lifecycle.MutableLiveData
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.disposables.Disposable

/**
 * Encapsulates the states and data of a paginated
 * data request.
 */
@Deprecated(message = "Replace with Paging3 library")
abstract class PaginatedRequest<T>(open val mainThreadScheduler: Scheduler) {

    private var pageToLoad = 1
    private var totalNumOfPages: Int? = null

    val data = MutableLiveData<List<T>>()
    val loading = MutableLiveData<Boolean>()
    val loadingMore = MutableLiveData<Boolean>()
    val error = MutableLiveData<Throwable>()
    val errorLoadingMore = MutableLiveData<Throwable>()

    private var loadSubscription: Disposable? = null
    private var loadMoreSubscription: Disposable? = null

    open val shouldLoad = true

    /**
     * What data to fetch
     */
    abstract fun fetchData(pageToLoad: Int): Single<PagedData<T>>

    /**
     * Load first page of data
     */
    fun load() {
        loadSubscription?.dispose()
        if(!shouldLoad) { return }
        pageToLoad = 1
        error.value = null
        loading.value = true
        loadSubscription = fetchData(pageToLoad)
                .observeOn(mainThreadScheduler)
                .subscribe({ pagedData ->
                    loading.value = false
                    data.value = pagedData.items
                    totalNumOfPages = pagedData.totalNumOfPages
                    pageToLoad++
                }, { loadError ->
                    loading.value = false
                    error.value = loadError
                })
    }

    /**
     * Load next page of data and add it to the current list of data
     */
    fun loadMore() {
        val loadingValue = this.loading.value ?: false
        val loadingMoreValue = this.loadingMore.value ?: false
        if (loadingValue || loadingMoreValue) {
            // Ignore if any request is loading
            return
        }
        totalNumOfPages?.let { maxPage ->
            if (pageToLoad > maxPage) {
                // Ignore if there's no more pages to load
                return@let
            }
        }
        errorLoadingMore.value = null
        loadingMore.value = true
        loadMoreSubscription = fetchData(pageToLoad)
                .observeOn(mainThreadScheduler)
                .subscribe({ pagedData ->
                    val currentItems = data.value ?: emptyList()
                    val newList = currentItems + pagedData.items
                    loadingMore.value = false
                    data.value = newList
                    pageToLoad++
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

    /**
     * Resets the paginated request to its original state
     */
    open fun reset() {
        pageToLoad = 1
        totalNumOfPages = null
        data.value = null
        loading.value = null
        loadingMore.value = null
        error.value = null
        errorLoadingMore.value = null
        loadSubscription?.dispose()
        loadSubscription = null
        loadMoreSubscription?.dispose()
        loadMoreSubscription = null
    }

}