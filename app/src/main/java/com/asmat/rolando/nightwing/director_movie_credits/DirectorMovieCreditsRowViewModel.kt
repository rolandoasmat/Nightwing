package com.asmat.rolando.nightwing.director_movie_credits

import com.asmat.rolando.nightwing.model.MovieSummary
import com.asmat.rolando.nightwing.model.Resource
import com.asmat.rolando.nightwing.model.mappers.UiModelMapper
import com.asmat.rolando.nightwing.movies_tab.MoviesRowViewModel
import com.asmat.rolando.nightwing.repositories.MoviesRepository
import com.asmat.rolando.nightwing.repositories.PeopleRepository
import kotlinx.coroutines.flow.Flow

class DirectorMovieCreditsRowViewModel (
    private val peopleRepository: PeopleRepository,
    uiModelMapper: UiModelMapper,
    private val directorId: Int
): MoviesRowViewModel(uiModelMapper) {

    override fun moviesFlow(): Flow<Resource<List<MovieSummary>>> {
        return peopleRepository.get(movieId)
    }

}