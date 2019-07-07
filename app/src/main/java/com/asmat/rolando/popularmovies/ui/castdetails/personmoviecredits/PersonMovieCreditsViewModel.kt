package com.asmat.rolando.popularmovies.ui.castdetails.personmoviecredits

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.asmat.rolando.popularmovies.repositories.PeopleRepository
import com.asmat.rolando.popularmovies.utilities.URLUtils
import io.reactivex.android.schedulers.AndroidSchedulers

class PersonMovieCreditsViewModel(private val peopleRepository: PeopleRepository): ViewModel() {

    val uiModel = MutableLiveData<PersonMovieCreditsUiModel>()

    fun init(personID: Int) {
        peopleRepository
                .getPersonMovieCredits(personID)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    val mapped = response.cast.map {
                        val posterURL = it.poster_path?.let { url -> URLUtils.getImageURL342(url) }
                        MovieCreditUiModel(posterURL, it.character)
                    }
                    uiModel.value = PersonMovieCreditsUiModel(mapped)
                }, { error ->
                    // TODO handle error
                })

    }

}