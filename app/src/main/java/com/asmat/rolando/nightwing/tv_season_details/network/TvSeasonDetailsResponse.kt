package com.asmat.rolando.nightwing.tv_season_details.network

class TvSeasonDetailsResponse(
    val name: String,
    val overview: String,
    val episodes: List<Episode>) {

    data class Episode(
        val episode_number: Int,
        val name: String,
        val overview: String,
        val still_path: String?)
}