package com.asmat.rolando.popularmovies.networking.models

data class VideoResponse (
        val id: String,
        val iso_639_1: String,
        val iso_3166_1: String,
        val key: String,
        val name: String,
        val site: String,
        val size: Int, // 360, 480, 720, 1080
        val type: String) // Trailer, Teaser, Clip, Featurette
