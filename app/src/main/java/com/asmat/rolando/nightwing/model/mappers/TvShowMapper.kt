package com.asmat.rolando.nightwing.model.mappers

import com.asmat.rolando.nightwing.model.TvShowSummary
import com.asmat.rolando.nightwing.networking.models.TvShowsResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TvShowMapper @Inject constructor() {

    fun map(data: TvShowsResponse): List<TvShowSummary> {
        return data.results.map { item ->
            map(item)
        }
    }

    private fun map(data: TvShowsResponse.Item): TvShowSummary {
        return TvShowSummary(
            id = data.id,
            title = data.name,
            posterPath = data.poster_path
        )
    }
}