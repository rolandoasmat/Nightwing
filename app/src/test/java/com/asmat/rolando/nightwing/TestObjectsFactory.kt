package com.asmat.rolando.nightwing

import com.asmat.rolando.nightwing.database.entities.FavoriteMovie
import com.asmat.rolando.nightwing.database.entities.WatchLaterMovie
import com.asmat.rolando.nightwing.model.Movie
import com.asmat.rolando.nightwing.networking.models.*

object TestObjectsFactory {

    fun movie(): Movie {
        return Movie("test poster path",
                "test overview",
                "test release data",
                4444,
                "test title",
                "test backdrop path",
                4.4)
    }

    fun favoriteMovie(id: Int = 4444): FavoriteMovie {
        return FavoriteMovie(id,
                "$id test poster path",
                "$id test overview",
                "$id test release data",
                "$id test title",
                "$id test backdrop path",
                4.44)
    }

    fun favoriteMovies(count: Int = 4): List<FavoriteMovie> {
        val list = mutableListOf<FavoriteMovie>()
        for (i in 1..count) {
            list.add(favoriteMovie(i))
        }
        return list
    }

    fun watchLaterMovie(id: Int = 4444): WatchLaterMovie {
        return WatchLaterMovie(id,
                "$id test poster path",
                "$id test overview",
                "$id test release data",
                "$id test title",
                "$id test backdrop path",
                4.44)
    }

    fun watchLaterMovies(count: Int = 4): List<WatchLaterMovie> {
        val list = mutableListOf<WatchLaterMovie>()
        for (i in 1..count) {
            list.add(watchLaterMovie(i))
        }
        return list
    }

    fun videosResponse(): VideosResponse {
        val video = VideosResponse.Video("test id",
                "test iso 639",
                "test iso 3166",
                "test key",
                "test name",
                "test site",
                300,
                "test type")
        return VideosResponse(5555, listOf(video, video, video))
    }

    fun movieReviews(): ReviewsResponse {
        val review = ReviewsResponse.Review("1234",
                "test author",
                "test content",
                "test url")
        val reviews = listOf(review, review, review)
        return ReviewsResponse(6666, 1, reviews, 1, 4)
    }

    fun movieCredits(): CreditsResponse {
        val cast = CreditsResponse.Cast(1234,
                "test character",
                "test credit id",
                null,
                5678,
                "test name",
                1,
                "test profile path")
        val crew = CreditsResponse.Crew("test credit id",
                "test department",
                null,
                5678,
                "test job",
                "test name",
                "test profile path")
        return CreditsResponse(4444,
                listOf(cast, cast, cast),
                listOf(crew))
    }

    fun personDetailsResponse(): PersonDetailsResponse {
        return PersonDetailsResponse("test birthday",
                "test known for",
                "test deathday",
                1234,
                "test name",
                listOf("test name 1", "test name 2", "test name 3"),
                1,"test biography",
                44.44,
                "test place of birth",
                "test profile path",
                false,
                "test imdb id",
                "test homepage")
    }

    fun personMovieCredits(): PersonMovieCredits {
        val castCredit = PersonMovieCredits.CastCredit("test character",
                "test credit id",
                "test release date",
                15,
                true,
                false,
                44.44,
                "test title",
                listOf(1, 2, 3),
                "test original language",
                "test original title",
                4.4,
                1234,
                "test backdrop path",
                "test overview",
                "test poster path")
        return PersonMovieCredits(listOf(castCredit, castCredit, castCredit))
    }
    fun moviesResponse(): MoviesResponse.Movie {
        return MoviesResponse.Movie("test poster path",
                false,
                "test overview",
                "test release date",
                listOf(1, 2, 3),
                1234,
                "test original title",
                "test original language",
                "test title",
                "test backdrop path",
                4.4,
                44,
                false,
                44.44)
    }
}