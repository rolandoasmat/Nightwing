package com.asmat.rolando.nightwing.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_movies")
data class SavedMovie(
        @PrimaryKey
        val id: Int,
        @ColumnInfo(name = "poster_url")
        val posterURL: String?,
        val title: String)