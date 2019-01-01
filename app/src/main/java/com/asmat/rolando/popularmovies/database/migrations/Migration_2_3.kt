package com.asmat.rolando.popularmovies.database.migrations

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.migration.Migration

/**
 * Deleted central movies table
 */
class Migration_2_3: Migration(2, 3) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // Deprecate old favorite_movies table
        database.execSQL("ALTER TABLE favorite_movies RENAME TO favorite_movies_deprecated")
        // Create favorite_movies table again
        database.execSQL("""CREATE TABLE favorite_movies
                    (id INTEGER PRIMARY KEY NOT NULL,
                    poster_path TEXT,
                    overview TEXT NOT NULL,
                    release_date TEXT NOT NULL,
                    title TEXT NOT NULL,
                    backdrop_path TEXT,
                    vote_average REAL NOT NULL)""")
        // Populate new favorite_movies table
        database.execSQL("""
                    INSERT INTO favorite_movies
                    SELECT movies.id, poster_path, overview, release_date, title, backdrop_path, vote_average
                    FROM favorite_movies_deprecated
                    INNER JOIN movies
                    ON favorite_movies_deprecated.id = movies.id""")
        // Delete old favorite_movies table
        database.execSQL("DROP TABLE favorite_movies_deprecated")

        // Deprecate old favorite_movies table
        database.execSQL("ALTER TABLE watch_later_movies RENAME TO watch_later_movies_deprecated")
        // Create favorite_movies table again
        database.execSQL("""CREATE TABLE watch_later_movies
                    (id INTEGER PRIMARY KEY NOT NULL,
                    poster_path TEXT,
                    overview TEXT NOT NULL,
                    release_date TEXT NOT NULL,
                    title TEXT NOT NULL,
                    backdrop_path TEXT,
                    vote_average REAL NOT NULL)""")
        // Populate new favorite_movies table
        database.execSQL("""
                    INSERT INTO watch_later_movies
                    SELECT movies.id, poster_path, overview, release_date, title, backdrop_path, vote_average
                    FROM watch_later_movies_deprecated
                    INNER JOIN movies
                    ON watch_later_movies_deprecated.id = movies.id""")
        // Delete old favorite_movies table
        database.execSQL("DROP TABLE watch_later_movies_deprecated")

        // Delete movies table
        database.execSQL("DROP TABLE movies")
    }
}