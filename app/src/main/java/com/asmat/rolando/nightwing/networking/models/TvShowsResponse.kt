package com.asmat.rolando.nightwing.networking.models

data class TvShowsResponse(val results: List<Item>) {
    data class Item(
            val id: Int,
            val poster_path: String?,
            val name: String?)
}