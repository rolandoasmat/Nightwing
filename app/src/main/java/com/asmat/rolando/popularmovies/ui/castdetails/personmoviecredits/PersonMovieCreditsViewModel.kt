package com.asmat.rolando.popularmovies.ui.castdetails.personmoviecredits

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.asmat.rolando.popularmovies.model.mappers.UiModelMapper
import com.asmat.rolando.popularmovies.repositories.PeopleRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable

class PersonMovieCreditsViewModel(private val peopleRepository: PeopleRepository,
                                  private val uiModelMapper: UiModelMapper): ViewModel() {
    private val compositeDisposable = CompositeDisposable()

    val uiModel = MutableLiveData<PersonMovieCreditsUiModel>()

    fun init(personID: Int) {
        val disposable = peopleRepository
                .getPersonMovieCredits(personID)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    uiModel.value = uiModelMapper.map(response)
                }, { error ->
                    // TODO handle error
                })
        compositeDisposable.add(disposable)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

}