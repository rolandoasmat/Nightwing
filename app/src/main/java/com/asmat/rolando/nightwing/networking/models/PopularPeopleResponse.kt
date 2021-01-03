package com.asmat.rolando.nightwing.networking.models

data class PopularPeopleResponse(val page: Int, val results: List<Item>) {
    data class Item(val name: String, val profile_path: String?, val known_for: List<KnownForItem>)
    data class KnownForItem(val title: String?)
}