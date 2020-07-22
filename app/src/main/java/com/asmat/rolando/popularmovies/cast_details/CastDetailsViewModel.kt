package com.asmat.rolando.popularmovies.cast_details

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.asmat.rolando.popularmovies.cast_details.CastDetailsUiModel
import com.asmat.rolando.popularmovies.repositories.PeopleRepository
import com.asmat.rolando.popularmovies.cast_details.PersonInfoUiModel
import com.asmat.rolando.popularmovies.utilities.URLUtils
import io.reactivex.Scheduler
import io.reactivex.disposables.Disposable

class CastDetailsViewModel(private val peopleRepository: PeopleRepository,
                           private val mainThreadScheduler: Scheduler): ViewModel() {

    private var disposable: Disposable? = null

    val loading = MutableLiveData<Boolean>()
    val error = MutableLiveData<Throwable>()
    val name = MutableLiveData<String>()

    val uiModel = MutableLiveData<CastDetailsUiModel>()

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
                .observeOn(mainThreadScheduler)
                .subscribe({
                    loading.value = false
                    name.value = it.name
                    val infoUiModel = PersonInfoUiModel(
                            URLUtils.getImageURL342(it.profile_path ?: ""),
                            it.place_of_birth ?: "--",
                            it.birthday ?: "--",
                            it.deathday ?: "--",
                            it.biography)

                    uiModel.value = CastDetailsUiModel(infoUiModel, personID)
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