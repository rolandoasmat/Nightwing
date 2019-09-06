package com.asmat.rolando.popularmovies

import com.asmat.rolando.popularmovies.database.entities.FavoriteMovie
import com.asmat.rolando.popularmovies.model.Movie

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

    fun favoriteMovie(): FavoriteMovie {
        return FavoriteMovie(4444,
                "test poster path",
                "test overview",
                "test release data",
                "test title","test backdrop path",
                4.44)
    }
}