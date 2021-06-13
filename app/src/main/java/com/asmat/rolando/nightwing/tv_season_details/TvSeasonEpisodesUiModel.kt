package com.asmat.rolando.nightwing.tv_season_details

data class TvSeasonEpisodesUiModel(
    val episodes: List<Item>
) {
    data class Item(
        val title: String,
        val body: String
    )
}