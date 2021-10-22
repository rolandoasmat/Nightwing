package com.asmat.rolando.nightwing.cast_details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asmat.rolando.nightwing.model.MovieSummary
import com.asmat.rolando.nightwing.model.PersonMovieCredits
import com.asmat.rolando.nightwing.model.Resource
import com.asmat.rolando.nightwing.model.mappers.UiModelMapper
import com.asmat.rolando.nightwing.repositories.PeopleRepository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class PersonMovieCreditsViewModel(
    private val peopleRepository: PeopleRepository,
    private val uiModelMapper: UiModelMapper,
    private val personID: Int
    ): ViewModel() {

    private val _uiModel = MutableLiveData<PersonMovieCreditsUiModel>()
    val uiModel: LiveData<PersonMovieCreditsUiModel>
        get() = _uiModel

    init {
        viewModelScope.launch {
            peopleRepository.getActorMovieCredits(personID).collect { resource ->
                handleMovieCreditsResponse(resource)
            }
        }
    }

    private fun handleMovieCreditsResponse(resource: Resource<PersonMovieCredits>) {
        when (resource) {
            is Resource.Loading -> {
                // TODO
            }
            is Resource.Success -> {
                resource.data?.let { credits ->
                    _uiModel.value = uiModelMapper.mapPersonMovieCredits(credits)
                }
            }
            is Resource.Error -> {
                // TODO
            }
        }
    }
}