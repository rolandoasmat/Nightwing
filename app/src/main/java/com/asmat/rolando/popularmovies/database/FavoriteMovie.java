package com.asmat.rolando.popularmovies.database;

import android.arch.persistence.room.*;

@Entity(tableName = "favorite_movies",
        foreignKeys = @ForeignKey(
                entity = Movie.class,
                parentColumns = "id",
                childColumns = "id"))

public class FavoriteMovie {
    @PrimaryKey
    public int id;

}
