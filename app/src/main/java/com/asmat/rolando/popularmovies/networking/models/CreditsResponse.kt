package com.asmat.rolando.popularmovies.networking.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CreditsResponse(val id: Int,
                           val cast: List<Cast>,
                           val crew: List<Crew>) : Parcelable {

    @Parcelize
    data class Cast(val cast_id: Int,
                    val character: String,
                    val credit_id: String,
                    val gender: Int?,
                    val id: Int,
                    val name: String,
                    val order: Int,
                    val profile_path: String?) : Parcelable

    @Parcelize
    data class Crew(val credit_id: String,
                    val department: String,
                    val gender: Int?,
                    val id: Int,
                    val job: String,
                    val name: String,
                    val profile_path: String?) : Parcelable
}