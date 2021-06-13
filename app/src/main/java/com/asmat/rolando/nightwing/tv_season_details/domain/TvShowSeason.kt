package com.asmat.rolando.nightwing.tv_season_details.domain

import com.asmat.rolando.nightwing.tv_season_details.network.TvSeasonDetailsResponse

data class TvShowSeason(
    val name: String,
    val overview: String,
    val episodes: List<Episode>) {

    data class Episode(
        val episodeNumber: Int,
        val name: String,
        val overview: String,
        val imageURL: String?)
}

fun TvSeasonDetailsResponse.toTvShowSeason(): TvShowSeason {
    return TvShowSeason(
        name = this.name,
        overview = this.overview,
        episodes = this.episodes.map { episode ->
            TvShowSeason.Episode(
                episodeNumber = episode.episode_number,
                name = episode.name,
                overview = episode.overview,
                imageURL = episode.still_path // TODO update
            )
        }
    )
}