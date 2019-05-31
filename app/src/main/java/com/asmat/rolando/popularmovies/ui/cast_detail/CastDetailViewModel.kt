package com.asmat.rolando.popularmovies.ui.cast_detail

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.asmat.rolando.popularmovies.repositories.MoviesRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable

class CastDetailViewModel(private val moviesRepository: MoviesRepository): ViewModel() {

    private var disposable: Disposable? = null

    val loading = MutableLiveData<Boolean>()
    val error = MutableLiveData<Throwable>()
    val name = MutableLiveData<String>()
    val biography = MutableLiveData<String>()
    val birthday = MutableLiveData<String>()
    val deathday = MutableLiveData<String>()

    fun init(personID: Int) {
        loading.value = true
        disposable = moviesRepository
                .getPersonDetails(personID)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    loading.value = false
                    name.value = it.name
                    biography.value = it.biography
                    birthday.value = it.birthday
                    deathday.value = it.deathday
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