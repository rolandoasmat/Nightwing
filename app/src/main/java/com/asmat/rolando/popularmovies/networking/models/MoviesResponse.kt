package com.asmat.rolando.popularmovies.networking.models

data class MoviesResponse(val page: Int,
                          val results: List<MovieResponse>,
                          val total_results: Int,
                          val total_pages: Int)