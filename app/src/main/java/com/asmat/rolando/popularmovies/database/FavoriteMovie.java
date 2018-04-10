package com.asmat.rolando.popularmovies.database;

import android.arch.persistence.room.*;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "favorite_movies",
        foreignKeys = @ForeignKey(
                entity = Movie.class,
                parentColumns = "id",
                childColumns = "id",
                onDelete = CASCADE))

public class FavoriteMovie {
    @PrimaryKey
    public int id;

}
