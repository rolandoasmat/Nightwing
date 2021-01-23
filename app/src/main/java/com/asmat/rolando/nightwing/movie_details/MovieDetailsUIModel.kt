package com.asmat.rolando.nightwing.movie_details

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MovieDetailsUIModel(
        val posterURL: String?,
        val overview: String,
        val releaseDateText: String,
        val releaseDate: Long?,
        val id: Int,
        val title: String,
        val backdropURL: String?,
        val voteAverage: String?,
        val runtime: String?,
        val tagline: String?) : Parcelable