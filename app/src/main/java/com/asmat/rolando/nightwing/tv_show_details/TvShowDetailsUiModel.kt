package com.asmat.rolando.nightwing.tv_show_details

import com.asmat.rolando.nightwing.networking.models.TvShowDetailsResponse
import com.asmat.rolando.nightwing.utilities.DateUtils
import com.asmat.rolando.nightwing.utilities.URLUtils

data class TvShowDetailsUiModel(
        val id: Int,
        val backdropUrl: String?,
        val createdBy: String,
        val firstAirDateText: String,
        val firstAirDate: Long?,
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
            val id: Int,
            val name: String,
            val posterUrl: String?,
            val seasonNumber: Int?,
            val episodeCount: String)

    companion object {
        fun from(data: TvShowDetailsResponse): TvShowDetailsUiModel {
            val id = data.id
            val backdropUrl = data.backdrop_path?.let { URLUtils.getImageURL780(it) }
            val createdByItems = data.created_by.mapNotNull { it.name }
            val createdBy = if(createdByItems.isNotEmpty()) {
                createdByItems.reduce { acc, createdBy ->
                    "$acc, $createdBy"
                }
            } else "--"
            val firstAirDateText = data.first_air_date?.let { DateUtils.formatDate(it) } ?: ""
            val firstAirDate = DateUtils.transform(data.first_air_date)?.time
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
                val seasonNumber = it.season_number
                val episodeCount = it.episode_count?.toString() ?: "--"
                Season(it.id, seasonName, url, seasonNumber, episodeCount)
            }
            return TvShowDetailsUiModel(id, backdropUrl, createdBy, firstAirDateText, firstAirDate, lastAirDate, name,
                    networks, overview, posterUrl, numberOfEpisodes, numberOfSeasons, status,
                    tagline, seasons)
        }
    }
}