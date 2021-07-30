package com.asmat.rolando.nightwing.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "popular_movies")
class PopularMovie(
    @PrimaryKey
    val id: Int,
    val title: String,
    val thumbnailURL: String?
)