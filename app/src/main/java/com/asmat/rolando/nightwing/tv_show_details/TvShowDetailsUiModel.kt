package com.asmat.rolando.nightwing.tv_show_details

import com.asmat.rolando.nightwing.networking.models.TvShowDetailsResponse
import com.asmat.rolando.nightwing.utilities.DateUtils
import com.asmat.rolando.nightwing.utilities.URLUtils

data class TvShowDetailsUiModel(
        val backdropUrl: String?,
        val createdBy: String,
        val firstAirDate: String,
        val lastAirDate: String,
        val name: String,
        val networks: String,
        val overview: String,
        val posterUrl: String?,
        val numberOfEpisodes: String,
        val numberOfSeasons: String,
        val status: String,
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
            val createdByItems = data.created_by.mapNotNull { it.name }
            val createdBy = if(createdByItems.isNotEmpty()) {
                createdByItems.reduce { acc, createdBy ->
                    "$acc, $createdBy"
                }
            } else "--"
            val firstAirDate = data.first_air_date?.let { DateUtils.formatDate(it) } ?: ""
            val lastAirDate = data.last_air_date?.let { DateUtils.formatDate(it) } ?: ""
            val name = data.name ?: "--"
            val networkItems = data.networks.mapNotNull { it.name }
            val networks = if (networkItems.isNotEmpty()) {
                networkItems.reduce { acc, network ->
                    "$acc, $network"
                }
            } else "--"
            val overview = data.overview ?: "--"
            val posterUrl = data.poster_path?.let { URLUtils.getImageURL342(it) }
            val numberOfEpisodes = data.number_of_episodes?.toString() ?: "--"
            val numberOfSeasons = data.number_of_seasons?.toString() ?: "--"
            val status = data.status ?: "--"
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
            return TvShowDetailsUiModel(backdropUrl, createdBy, firstAirDate, lastAirDate, name,
                    networks, overview, posterUrl, numberOfEpisodes, numberOfSeasons, status,
                    tagline, seasons)
        }
    }
}