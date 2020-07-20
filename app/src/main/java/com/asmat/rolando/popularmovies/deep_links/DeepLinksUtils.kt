package com.asmat.rolando.popularmovies.deep_links

import javax.inject.Inject

class DeepLinksUtils @Inject constructor() {

    companion object {
        private const val URI = "movienight.app"
        private const val MOVIE_DETAILS_DOMAIN = "details"
    }

    fun shareMovieDetailsDeepLink(movieID: Int): String {
        return "$URI/$MOVIE_DETAILS_DOMAIN/$movieID"
    }


}