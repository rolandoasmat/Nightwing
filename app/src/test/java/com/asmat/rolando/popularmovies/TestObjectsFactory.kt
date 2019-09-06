package com.asmat.rolando.popularmovies

import com.asmat.rolando.popularmovies.database.entities.FavoriteMovie
import com.asmat.rolando.popularmovies.database.entities.WatchLaterMovie
import com.asmat.rolando.popularmovies.model.Movie
import com.asmat.rolando.popularmovies.networking.the.movie.db.models.VideosResponse

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
}