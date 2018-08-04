package com.asmat.rolando.popularmovies.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "watch_later_movies",
        foreignKeys = @ForeignKey(
                entity = Movie.class,
                parentColumns = "id",
                childColumns = "id",
                onDelete = CASCADE))

public class WatchLaterMovie {
    @PrimaryKey
    int id;

    public WatchLaterMovie(int id) {
        this.id = id;
    }
}