package com.asmat.rolando.nightwing.networking.models

data class ReviewsResponse(val id: Int,
                           val page: Int,
                           val results: List<Review>,
                           val total_pages: Int,
                           val total_results: Int) {

    data class Review(val id: String,
                      val author: String,
                      val content: String,
                      val url: String)
}