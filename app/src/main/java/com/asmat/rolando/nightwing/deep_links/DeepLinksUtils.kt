package com.asmat.rolando.nightwing.deep_links

import javax.inject.Inject

class DeepLinksUtils @Inject constructor() {

    companion object {
        private const val URI = "nightwing.app"
        private const val MOVIE_DETAILS_DOMAIN = "movieDetails"
    }

    fun shareMovieDetailsDeepLink(movieID: Int): String {
        return "$URI/$MOVIE_DETAILS_DOMAIN/$movieID"
    }


}