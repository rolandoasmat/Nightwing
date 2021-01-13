package com.asmat.rolando.nightwing.tv_show_details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.asmat.rolando.nightwing.model.mappers.UiModelMapper
import com.asmat.rolando.nightwing.repositories.TvShowsRepository
import com.asmat.rolando.nightwing.ui.row_view.RowViewItemUiModel
import io.reactivex.disposables.CompositeDisposable

class TvShowDetailsViewModel(
        private val tvShowsRepository: TvShowsRepository,
        private val uiModelMapper: UiModelMapper): ViewModel() {

    private val _uiModel = MutableLiveData<TvShowDetailsUiModel>()
    val uiModel: LiveData<TvShowDetailsUiModel>
        get() = _uiModel

    private val _seasons = MutableLiveData<List<RowViewItemUiModel>>()
    val seasons: LiveData<List<RowViewItemUiModel>>
        get() = _seasons

    private val _isSaved = MutableLiveData<Boolean>()
    val isSaved: LiveData<Boolean>
        get() = _isSaved

    private val compositeDisposable = CompositeDisposable()


    fun load(tvShowId: Int) {
        val disposable = tvShowsRepository
                .getTvShowDetails(tvShowId)
                .subscribe({
                    val uiModel = TvShowDetailsUiModel.from(it)
                    _uiModel.postValue(uiModel)

                    val seasonsUiModels = uiModel.seasons.map { season ->
                        RowViewItemUiModel(season.id, season.posterUrl, season.name)
                    }
                    _seasons.postValue(seasonsUiModels)
                }, {})
        compositeDisposable.add(disposable)
    }

    fun heartIconTapped() {

    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}