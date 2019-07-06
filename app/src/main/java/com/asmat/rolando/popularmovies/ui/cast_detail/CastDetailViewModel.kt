package com.asmat.rolando.popularmovies.ui.cast_detail

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import com.asmat.rolando.popularmovies.repositories.MoviesRepository
import com.asmat.rolando.popularmovies.repositories.PeopleRepository
import com.asmat.rolando.popularmovies.utilities.URLUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class CastDetailViewModel(private val peopleRepository: PeopleRepository): ViewModel() {

    private var disposable: Disposable? = null

    val loading = MutableLiveData<Boolean>()
    val error = MutableLiveData<Throwable>()
    val name = MutableLiveData<String>()
    val photoURL = MutableLiveData<String>()
    val biography = MutableLiveData<String>()
    val hometown = MutableLiveData<String>()
    val birthday = MutableLiveData<String>()
    val deathday = MutableLiveData<String>()

    val uiModel = MutableLiveData<CastDetailScreenUiModel>()

    fun init(personID: Int) {
        fetchPersonDetails(personID)
    }

    fun retry(personID: Int) {
        fetchPersonDetails(personID)
    }

    private fun fetchPersonDetails(personID: Int) {
        loading.value = true
        disposable = peopleRepository
                .getPersonDetails(personID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    loading.value = false
                    name.value = it.name
                    val infoUiModel = PersonDetailsUiModel(
                            URLUtils.getImageURL342(it.profile_path ?: ""),
                            it.place_of_birth ?: "--",
                            it.birthday ?: "--",
                            it.deathday ?: "--",
                            it.biography)

                    uiModel.value = CastDetailScreenUiModel(infoUiModel)
                }, {
                    error.value = it
                    loading.value = false
                })
        peopleRepository
                .getPersonMovieCredits(personID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    it.cast.forEach {
                        // TODO populate UI with list of movies

                    }
                }, {
                    error.value = it
                    loading.value = false
                })
    }

    override fun onCleared() {
        super.onCleared()
        disposable?.dispose()
        disposable = null
    }

}