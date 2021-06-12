package com.asmat.rolando.nightwing.networking.models

class TvSeasonDetailsResponse(
    val name: String,
    val overview: String,
    val episodes: List<Episode>) {

    data class Episode(
        val episode_number: Int,
        val name: String,
        val overview: String,
        val still_path: String)
}