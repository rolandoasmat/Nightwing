package com.asmat.rolando.nightwing.model

import com.asmat.rolando.nightwing.networking.models.PersonMovieCredits as NetworkPersonMovieCredits

data class PersonMovieCredits(
    val cast: List<CastCredit>?,
    val crew: List<CrewCredit>?) {
    data class CastCredit(
        val character: String?,
        val creditID: String?,
        val releaseDate: String?,
        val voteCount: Int?,
        val video: Boolean?,
        val adult: Boolean?,
        val voteAverage: Double?,
        val title: String?,
        val genreIDs: List<Int>?,
        val originalLanguage: String?,
        val originalTitle: String?,
        val popularity: Double?,
        val id: Int?, // TODO is this nullable??
        val backdropPath: String?,
        val overview: String,
        val posterPath: String?)
    data class CrewCredit(
        val id: Int?,
        val department: String?,
        val originalLanguage: String?,
        val originalTitle: String?,
        val job: String?,
        val overview: String?,
        val voteCount: Int?,
        val video: Boolean?,
        val posterPath: String?,
        val backdropPath: String?,
        val title: String?,
        val popularity: Double?,
        val genreIDs: List<Int>?,
        val voteAverage: Double?,
        val adult: Boolean?,
        val releaseDate: String?,
        val creditID: String?
    )
}

fun NetworkPersonMovieCredits.toDataModel(): PersonMovieCredits {
    return PersonMovieCredits(
        cast = this.cast?.map {
            PersonMovieCredits.CastCredit (
                character = it.character,
                creditID = it.credit_id,
                releaseDate = it.release_date,
                voteCount = it.vote_count,
                video = it.video,
                adult = it.adult,
                voteAverage = it.vote_average,
                title = it.title,
                genreIDs = it.genre_ids,
                originalLanguage = it.original_language,
                originalTitle = it.original_title,
                popularity = it.popularity,
                id = it.id,
                backdropPath = it.backdrop_path,
                overview = it.overview,
                posterPath = it.poster_path
            )
        },
        crew = this.crew?.map {
            PersonMovieCredits.CrewCredit (
                id = it.id,
                department = it.department,
                originalLanguage = it.original_language,
                originalTitle = it.original_title,
                job = it.job,
                overview = it.overview,
                voteCount = it.vote_count,
                video = it.video,
                posterPath = it.poster_path,
                backdropPath = it.backdrop_path,
                title = it.title,
                popularity = it.popularity,
                genreIDs = it.genre_ids,
                voteAverage = it.vote_average,
                adult = it.adult,
                releaseDate = it.release_date,
                creditID = it.credit_id
            )
        }
    )
}