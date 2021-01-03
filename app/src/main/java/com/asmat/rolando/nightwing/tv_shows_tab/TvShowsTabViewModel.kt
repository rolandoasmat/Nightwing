package com.asmat.rolando.nightwing.tv_shows_tab

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.asmat.rolando.nightwing.model.mappers.UiModelMapper
import com.asmat.rolando.nightwing.repositories.TvShowsRepository
import com.asmat.rolando.nightwing.ui.row_view.RowViewItemUiModel
import io.reactivex.disposables.CompositeDisposable

class TvShowsTabViewModel(
        tvShowsRepository: TvShowsRepository,
        uiModelMapper: UiModelMapper): ViewModel() {

    private val _popularTvShows = MutableLiveData<List<RowViewItemUiModel>>()
    val popularTvShows: LiveData<List<RowViewItemUiModel>>
        get() = _popularTvShows

    private val _topRatedTvShows = MutableLiveData<List<RowViewItemUiModel>>()
    val topRatedTvShows: LiveData<List<RowViewItemUiModel>>
        get() = _topRatedTvShows

    private val _onTheAirTvShows = MutableLiveData<List<RowViewItemUiModel>>()
    val onTheAirTvShows: LiveData<List<RowViewItemUiModel>>
        get() = _onTheAirTvShows

    private val compositeDisposable = CompositeDisposable()

    init {
        val disposable1 = tvShowsRepository
                .getPopularTvShows(1)
                .subscribe({
                    val mapped = uiModelMapper.map(it)
                    _popularTvShows.postValue(mapped)
                }, { error ->
                    Log.e("RAA", error.message)
                })
        compositeDisposable.add(disposable1)

        val disposable2 = tvShowsRepository
                .getTopRatedTvShows(1)
                .subscribe({
                    val mapped = uiModelMapper.map(it)
                    _topRatedTvShows.postValue(mapped)
                }, { error ->
                    Log.e("RAA", error.message)
                })
        compositeDisposable.add(disposable2)

        val disposable3 = tvShowsRepository
                .onTheAirTvShows(1)
                .subscribe({
                    val mapped = uiModelMapper.map(it)
                    _onTheAirTvShows.postValue(mapped)
                }, { error ->
                    Log.e("RAA", error.message)
                })
        compositeDisposable.add(disposable3)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}