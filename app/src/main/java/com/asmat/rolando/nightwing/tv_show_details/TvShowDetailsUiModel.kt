package com.asmat.rolando.nightwing.tv_show_details

import com.asmat.rolando.nightwing.networking.models.TvShowDetailsResponse
import com.asmat.rolando.nightwing.utilities.URLUtils

data class TvShowDetailsUiModel(
        val backdropUrl: String?,
        val name: String,
        val overview: String,
        val posterUrl: String?,
        val numberOfEpisodes: String,
        val numberOfSeasons: String,
        val tagline: String,
        val seasons: List<Season>) {

    data class Season(
            val name: String,
            val posterUrl: String?,
            val seasonNumber: String,
            val episodeCount: String)

    companion object {
        fun from(data: TvShowDetailsResponse): TvShowDetailsUiModel {
            val backdropUrl = data.backdrop_path?.let { URLUtils.getImageURL780(it) }
            val name = data.name ?: "--"
            val overview = data.overview ?: "--"
            val posterUrl = data.poster_path?.let { URLUtils.getImageURL342(it) }
            val numberOfEpisodes = data.number_of_episodes?.toString() ?: "--"
            val numberOfSeasons = data.number_of_seasons?.toString() ?: "--"
            val tagline = data.tagline ?: "--"
            val seasons = data.seasons.map {
                val seasonName = it.name ?: ""
                val url = it.poster_path?.let { posterPath ->
                    URLUtils.getImageURL342(posterPath)
                }
                val seasonNumber = it.season_number?.toString() ?: "--"
                val episodeCount = it.episode_count?.toString() ?: "--"
                Season(seasonName, url, seasonNumber, episodeCount)
            }
            return TvShowDetailsUiModel(backdropUrl, name, overview, posterUrl, numberOfEpisodes, numberOfSeasons, tagline, seasons)
        }
    }
}