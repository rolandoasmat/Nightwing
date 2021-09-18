package com.asmat.rolando.nightwing.director_movie_credits

import com.asmat.rolando.nightwing.model.MovieSummary
import com.asmat.rolando.nightwing.model.PersonMovieCredits
import com.asmat.rolando.nightwing.model.Resource
import com.asmat.rolando.nightwing.model.mappers.UiModelMapper
import com.asmat.rolando.nightwing.movies_tab.MoviesRowViewModel
import com.asmat.rolando.nightwing.repositories.PeopleRepository
import com.asmat.rolando.nightwing.utilities.URLUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DirectorMovieCreditsRowViewModel (
    private val peopleRepository: PeopleRepository,
    uiModelMapper: UiModelMapper,
    private val directorId: Int
): MoviesRowViewModel(uiModelMapper) {

    override fun moviesFlow(): Flow<Resource<List<MovieSummary>>> {
        return peopleRepository
            .getPersonMovieCredits(directorId)
            .map {
                transformResource(it)
            }
    }

    private fun transformResource(resource: Resource<PersonMovieCredits>): Resource<List<MovieSummary>> {
        return resource.transform { credits ->
            credits.crew?.filter {
                it.job == DIRECTOR
            }?.map {
                val posterURL = it.posterPath?.let { url -> URLUtils.getImageURL342(url)}
                MovieSummary(it.id ?: 0,posterURL ?: "", it.title ?: "")
            } ?: emptyList()
        }
    }

    companion object {
        private const val DIRECTOR = "Director"
    }
}