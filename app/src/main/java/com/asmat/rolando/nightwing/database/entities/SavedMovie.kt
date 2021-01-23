package com.asmat.rolando.nightwing.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_movies")
data class SavedMovie(
        @PrimaryKey
        val id: Int,
        val posterURL: String?,
        val title: String,
        val releaseDate: Long?)