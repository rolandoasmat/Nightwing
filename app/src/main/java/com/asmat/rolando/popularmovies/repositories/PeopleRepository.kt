package com.asmat.rolando.popularmovies.repositories

import com.asmat.rolando.popularmovies.networking.the.movie.db.TheMovieDBClient
import com.asmat.rolando.popularmovies.networking.the.movie.db.models.PersonDetailsResponse
import com.asmat.rolando.popularmovies.networking.the.movie.db.models.PersonMovieCredits
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


class PeopleRepository @Inject constructor(private val tmdbClient: TheMovieDBClient) {

    fun getPersonDetails(id: Int): Single<PersonDetailsResponse> {
        return tmdbClient.getPersonDetails(id).subscribeOn(Schedulers.io())
    }

    fun getPersonMovieCredits(id: Int) : Single<PersonMovieCredits> {
        return tmdbClient.getPersonMovieCredits(id)
    }

}