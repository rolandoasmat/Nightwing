package com.asmat.rolando.popularmovies.networking.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MoviesResponse(val page: Int?,
                          val results: List<Movie>?,
                          val total_results: Int?,
                          val total_pages: Int?):Parcelable {

    @Parcelize
    open class Movie(open val poster_path: String?,
                         open val adult: Boolean?,
                         open val overview: String?,
                         open val release_date: String?,
                         open val genre_ids: List<Int>?,
                         open val id: Int?,
                         open val original_title: String?,
                         open val original_language: String?,
                         open val title: String?,
                         open val backdrop_path: String?,
                         open val popularity: Double?,
                         open val vote_count: Int?,
                         open val video: Boolean?,
                         open val vote_average: Double?): Parcelable
}