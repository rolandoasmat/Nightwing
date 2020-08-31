package com.asmat.rolando.nightwing.movie_details

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MovieDetailsUIModel(
        val posterURL: String?,
        val overview: String,
        val releaseDate: String,
        val id: Int,
        val title: String,
        val backdropPath: String?,
        val voteAverage: String?,
        val runtime: String?,
        val tagline: String?) : Parcelable