package com.asmat.rolando.nightwing.director_movie_credits

import com.asmat.rolando.nightwing.model.MovieSummary
import com.asmat.rolando.nightwing.model.Resource
import com.asmat.rolando.nightwing.model.mappers.UiModelMapper
import com.asmat.rolando.nightwing.movie_details.MovieDetailsViewModel
import com.asmat.rolando.nightwing.movies_tab.MoviesRowViewModel
import com.asmat.rolando.nightwing.repositories.MoviesRepository
import com.asmat.rolando.nightwing.repositories.PeopleRepository
import com.asmat.rolando.nightwing.ui.row_view.RowViewItemUiModel
import com.asmat.rolando.nightwing.utilities.URLUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DirectorMovieCreditsRowViewModel (
    private val peopleRepository: PeopleRepository,
    uiModelMapper: UiModelMapper,
    private val directorId: Int
): MoviesRowViewModel(uiModelMapper) {

    override fun moviesFlow(): Flow<Resource<List<MovieSummary>>> {
        return peopleRepository.getPersonMovieCredits(directorId)
    }



    private fun getDirectorCredits(personID: Int) {
        peopleRepository
            .getPersonMovieCredits(personID)
            .observeOn(mainThreadScheduler)
            .subscribe({ result ->
                val directorCredits = result.crew?.filter { it.job == MovieDetailsViewModel.DIRECTOR }
                val movies = directorCredits?.map {
                    val posterURL = it.poster_path?.let { url -> URLUtils.getImageURL342(url)}
                    RowViewItemUiModel(it.id ?: 0,posterURL ?: "", it.title ?: "")
                }
                _directorMovies.value = movies
            }, { error ->
                // TODO show error
            })
    }

}