package com.asmat.rolando.nightwing.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
open class Movie(open val posterPath: String?,
                 open val overview: String,
                 open val releaseDate: String,
                 open val id: Int,
                 open val title: String,
                 open val backdropPath: String?,
                 open val voteAverage: Double): Parcelable
