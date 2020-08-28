package com.asmat.rolando.nightwing.cast_details

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.asmat.rolando.nightwing.model.mappers.UiModelMapper
import com.asmat.rolando.nightwing.repositories.MoviesRepository
import com.asmat.rolando.nightwing.repositories.PeopleRepository
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable

class PersonMovieCreditsViewModel(private val peopleRepository: PeopleRepository,
                                  private val uiModelMapper: UiModelMapper,
                                  private val mainThreadScheduler: Scheduler,
                                  private val moviesRepository: MoviesRepository): ViewModel() {
    private val compositeDisposable = CompositeDisposable()

    val uiModel = MutableLiveData<PersonMovieCreditsUiModel>()

    fun init(personID: Int) {
        val disposable = peopleRepository
                .getPersonMovieCredits(personID)
                .observeOn(mainThreadScheduler)
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