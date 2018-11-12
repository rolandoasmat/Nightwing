package com.asmat.rolando.popularmovies.networking.models

data class VideosResponse(val id: Int,
                          val results: Array<VideoResponse>)