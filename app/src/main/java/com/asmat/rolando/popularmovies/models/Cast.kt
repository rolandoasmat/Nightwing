package com.asmat.rolando.popularmovies.models

import android.os.Parcelable
import com.asmat.rolando.popularmovies.utilities.ImageURLUtil
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Cast(val castID: Int,
                val character: String,
                val creditID: String,
                val gender: Int,
                val id: Int,
                val name: String,
                val order: Int,
                val profilePath: String) : Parcelable {

    val thumbnailURL: String
        get() = ImageURLUtil.getImageURL342(profilePath)


}
