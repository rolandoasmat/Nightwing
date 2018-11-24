package com.asmat.rolando.popularmovies.networking.the.movie.db.models

data class VideosResponse(val id: Int,
                          val results: List<Video>) {

    data class Video(val id: String,
                     val iso_639_1: String,
                     val iso_3166_1: String,
                     val key: String,
                     val name: String,
                     val site: String,
                     val size: Int, // 360, 480, 720, 1080
                     val type: String) // Trailer, Teaser, Clip, Featurette

}