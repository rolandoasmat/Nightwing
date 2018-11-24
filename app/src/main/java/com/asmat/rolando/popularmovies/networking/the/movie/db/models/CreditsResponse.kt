package com.asmat.rolando.popularmovies.networking.the.movie.db.models

data class CreditsResponse(val id: Int,
                           val cast: List<Cast>,
                           val crew: List<Crew>) {

    data class Cast(val cast_id: Int,
                    val character: String,
                    val credit_id: String,
                    val gender: Int?,
                    val id: Int,
                    val name: String,
                    val order: Int,
                    val profile_path: String?)

    data class Crew(val credit_id: String,
                    val department: String,
                    val gender: Int?,
                    val id: Int,
                    val job: String,
                    val name: String,
                    val profile_path: String?)
}