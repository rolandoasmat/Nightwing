package com.asmat.rolando.nightwing.tv_show_details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.asmat.rolando.nightwing.model.mappers.UiModelMapper
import com.asmat.rolando.nightwing.repositories.TvShowsRepository
import io.reactivex.disposables.CompositeDisposable

class TvShowDetailsViewModel(
        private val tvShowsRepository: TvShowsRepository,
        private val uiModelMapper: UiModelMapper): ViewModel() {

    private val _uiModel = MutableLiveData<TvShowDetailsUiModel>()
    val uiModel: LiveData<TvShowDetailsUiModel>
        get() = _uiModel

    private val compositeDisposable = CompositeDisposable()


    fun load(tvShowId: Int) {
        val disposable = tvShowsRepository
                .getTvShowDetails(tvShowId)
                .subscribe({
                    val mapped = TvShowDetailsUiModel.from(it)
                    _uiModel.postValue(mapped)
                }, {})
        compositeDisposable.add(disposable)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}