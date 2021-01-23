package com.asmat.rolando.nightwing.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_tv_shows")
data class SavedTvShow(
        @PrimaryKey
        val id: Int,
        val posterURL: String?,
        val title: String,
        val firstAirDate: Long?)