package com.asmat.rolando.popularmovies.ui.castdetails.personmoviecredits

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.asmat.rolando.popularmovies.repositories.PeopleRepository
import com.asmat.rolando.popularmovies.utilities.URLUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable

class PersonMovieCreditsViewModel(private val peopleRepository: PeopleRepository): ViewModel() {
    private val compositeDisposable = CompositeDisposable()

    val uiModel = MutableLiveData<PersonMovieCreditsUiModel>()

    fun init(personID: Int) {
        val disposable = peopleRepository
                .getPersonMovieCredits(personID)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    val mapped = response.cast.map {
                        val posterURL = it.poster_path?.let { url -> URLUtils.getImageURL342(url) }
                        MovieCreditUiModel(posterURL, it.character)
                    }
                    val movieCreditsWithBackdropImage = response.cast
                            .filter { it.backdrop_path != null }

                    val backdropURL = if (movieCreditsWithBackdropImage.isEmpty()) {
                        null
                    } else {
                        movieCreditsWithBackdropImage
                                .random().backdrop_path
                                ?.let { URLUtils.getImageURL780(it) }
                    }
                    uiModel.value = PersonMovieCreditsUiModel(backdropURL, mapped)
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