package com.asmat.rolando.popularmovies.ui.moviedetails

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MovieDetailsUIModel(
        val posterPath: String?,
        val overview: String,
        val releaseDate: String,
        val id: Int,
        val title: String,
        val backdropPath: String?,
        val voteAverage: String) : Parcelable