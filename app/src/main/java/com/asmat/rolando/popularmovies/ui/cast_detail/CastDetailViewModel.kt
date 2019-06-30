package com.asmat.rolando.popularmovies.ui.cast_detail

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.asmat.rolando.popularmovies.repositories.MoviesRepository
import com.asmat.rolando.popularmovies.utilities.URLUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable

class CastDetailViewModel(private val moviesRepository: MoviesRepository): ViewModel() {

    private var disposable: Disposable? = null

    val loading = MutableLiveData<Boolean>()
    val error = MutableLiveData<Throwable>()
    val name = MutableLiveData<String>()
    val photoURL = MutableLiveData<String>()
    val biography = MutableLiveData<String>()
    val hometown = MutableLiveData<String>()
    val birthday = MutableLiveData<String>()
    val deathday = MutableLiveData<String>()

    fun init(personID: Int) {
        fetchPersonDetails(personID)
    }

    fun retry(personID: Int) {
        fetchPersonDetails(personID)
    }

    private fun fetchPersonDetails(personID: Int) {
        loading.value = true
        disposable = moviesRepository
                .getPersonDetails(personID)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    loading.value = false
                    name.value = it.name
                    photoURL.value = URLUtils.getImageURL342(it.profile_path ?: "")
                    biography.value = it.biography
                    hometown.value = it.place_of_birth ?: "--"
                    birthday.value = it.birthday ?: "--"
                    deathday.value = it.deathday ?: "--"
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