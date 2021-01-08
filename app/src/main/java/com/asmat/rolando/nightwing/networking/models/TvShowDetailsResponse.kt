package com.asmat.rolando.nightwing.networking.models

data class TvShowDetailsResponse(
        val backdrop_path: String?,
        val created_by: List<CreatedBy>,
        val first_air_date: String?,
        val id: Int,
        val last_air_date: String?,
        val name: String?,
        val networks: List<Network>,
        val number_of_episodes: Int?,
        val number_of_seasons: Int?,
        val overview: String?,
        val poster_path: String?,
        val seasons: List<Season>,
        val status: String?,
        val tagline: String?) {

    data class CreatedBy(val name: String?)
    data class Network(val name: String?)
    data class Season(
            val air_date: String?,
            val episode_count: Int?,
            val id: Int,
            val name: String?,
            val overview: String?,
            val poster_path: String?,
            val season_number: Int?)
}