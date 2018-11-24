package com.asmat.rolando.popularmovies.utilities

object ImageURLUtils {

    /**
     * Sample widths "w92", "w154", "w185", "w342", "w500", "w780"
     * i.e. http://image.tmdb.org/t/p/w342
     */
    private const val TMDB_BASE_URL = "http://image.tmdb.org/t/p/"

    private const val YOUTUBE_BASE_URL = "https://www.youtube.com/watch?v="
    private const val YOUTUBE_THUMBNAIL_BASE_URL = "http://img.youtube.com/vi/"
    private const val YOUTUBE_THUMBNAIL_POST = "/0.jpg"

    private val w92 = "w92"
    private val w154 = "w154"
    private val w185 = "w185"
    private val w342 = "w342"
    private val w500 = "w500"
    private val w780 = "w780"

    //region API

    fun getImageURL342(relativeURL: String): String {
        return tmdbBuilder()
                .append(w342)
                .append(relativeURL)
                .toString()
    }

    fun getImageURL780(relativeURL: String): String {
        return tmdbBuilder()
                .append(w780)
                .append(relativeURL)
                .toString()
    }

    fun getYoutubeURL(key: String): String {
        return youtubeBuilder()
                .append(key)
                .toString()
    }

    fun getYoutubeThumbnailURL(key: String): String {
        return youtubeThumbnailBuilder()
                .append(key)
                .append(YOUTUBE_THUMBNAIL_POST)
                .toString()
    }

    //endregion

    // Helper method to create TMDB urls
    private fun tmdbBuilder(): StringBuilder {
        return StringBuilder(TMDB_BASE_URL)
    }

    private fun youtubeBuilder(): StringBuilder {
        return StringBuilder(YOUTUBE_BASE_URL)
    }

    private fun youtubeThumbnailBuilder(): StringBuilder {
        return StringBuilder(YOUTUBE_THUMBNAIL_BASE_URL)
    }


}
