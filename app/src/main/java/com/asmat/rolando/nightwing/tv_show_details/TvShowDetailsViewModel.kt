package com.asmat.rolando.nightwing.tv_show_details

import androidx.lifecycle.*
import com.asmat.rolando.nightwing.database.entities.SavedTvShow
import com.asmat.rolando.nightwing.deep_links.DeepLinksUtils
import com.asmat.rolando.nightwing.model.mappers.UiModelMapper
import com.asmat.rolando.nightwing.repositories.TvShowsRepository
import com.asmat.rolando.nightwing.share.ShareData
import com.asmat.rolando.nightwing.ui.row_view.RowViewItemUiModel
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.launch

class TvShowDetailsViewModel(
        private val tvShowsRepository: TvShowsRepository,
        private val uiModelMapper: UiModelMapper,
        private val deepLinksUtils: DeepLinksUtils): ViewModel() {

    private val _uiModel = MutableLiveData<TvShowDetailsUiModel>()
    val uiModel: LiveData<TvShowDetailsUiModel>
        get() = _uiModel

    private val _seasons = MutableLiveData<List<RowViewItemUiModel>>()
    val seasons: LiveData<List<RowViewItemUiModel>>
        get() = _seasons

    private val _isSaved = MediatorLiveData<Boolean>()
    val isSaved: LiveData<Boolean>
        get() = _isSaved

    private val _share = MutableLiveData<ShareData>()
    val share: LiveData<ShareData>
        get() = _share

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
        _isSaved.addSource(tvShowsRepository.isSavedTvShow(tvShowId).asLiveData()) {
            _isSaved.postValue(it == true)
        }
    }

    fun heartIconTapped() {
        val uiModel = _uiModel.value ?: return
        if (_isSaved.value == true) {
            viewModelScope.launch {
                tvShowsRepository.deleteSavedTvShow(uiModel.id)
            }
        } else {
            viewModelScope.launch {
                val savedTvShow = SavedTvShow(uiModel.id, uiModel.posterUrl, uiModel.name)
                tvShowsRepository.insertSavedTvShow(savedTvShow)
            }
        }
    }

    fun shareIconTapped() {
        _uiModel.value?.let {
            val title = "Share TV Show"
            val message = deepLinksUtils.shareTvShowDetailsDeepLink(it.id)
            val shareData = ShareData(title, message)
            _share.value = shareData
            _share.value = null
        }
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}