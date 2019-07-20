package com.asmat.rolando.popularmovies.ui.castdetails

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.asmat.rolando.popularmovies.repositories.PeopleRepository
import com.asmat.rolando.popularmovies.ui.castdetails.personinfo.PersonInfoUiModel
import com.asmat.rolando.popularmovies.utilities.URLUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class CastDetailsViewModel(private val peopleRepository: PeopleRepository): ViewModel() {

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
                .observeOn(AndroidSchedulers.mainThread())
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