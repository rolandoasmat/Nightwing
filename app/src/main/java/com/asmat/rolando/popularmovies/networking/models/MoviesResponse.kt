package com.asmat.rolando.popularmovies.networking.models

data class MoviesResponse(val page: Int,
                          val results: Array<MovieResponse>)