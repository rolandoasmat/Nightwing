package com.asmat.rolando.popularmovies.models

import com.asmat.rolando.popularmovies.networking.models.VideoResponse

data class Video(val key: String, val name: String) {
    constructor(videoResponse: VideoResponse) : this(videoResponse.key, videoResponse.name)

    val youTubeURL: String
        get() = YOUTUBE_URL + key

    val youTubeThumbnailURL: String
        get() = YOUTUBE_THUMBNAIL_PRE + key + YOUTUBE_THUMBNAIL_POST

    companion object {
        private val YOUTUBE_URL = "https://www.youtube.com/watch?v="
        private val YOUTUBE_THUMBNAIL_PRE = "http://img.youtube.com/vi/"
        private val YOUTUBE_THUMBNAIL_POST = "/0.jpg"
    }
}