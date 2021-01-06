package com.asmat.rolando.nightwing.networking.models

data class TvShowDetailsResponse(
        val backdrop_path: String?,
        val id: Int,
        val name: String?,
        val number_of_episodes: Int?,
        val number_of_seasons: Int?,
        val overview: String?,
        val poster_path: String?,
        val seasons: List<Season>,
        val status: String?,
        val tagline: String?) {
    data class Season(
            val episode_count: Int?,
            val id: Int,
            val name: String?,
            val overview: String?,
            val poster_path: String?,
            val season_number: Int?)
}