package com.asmat.rolando.nightwing.model.mappers

import com.asmat.rolando.nightwing.networking.models.MoviesResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataModelMapper @Inject constructor(private val movieMapper: MovieMapper) {

    fun map(data: List<MoviesResponse.Movie>?) = data?.map { movieMapper.from(it) }

}