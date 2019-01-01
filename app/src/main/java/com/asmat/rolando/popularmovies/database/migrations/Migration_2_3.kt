package com.asmat.rolando.popularmovies.database.migrations

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.migration.Migration

/**
 * Deleted central movies table
 */
class Migration_2_3: Migration(2, 3) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // Create new favorite_movies table
        database.execSQL("""CREATE TABLE favorite_movies_new
                    (id INTEGER PRIMARY KEY NOT NULL,
                    poster_path TEXT,
                    overview TEXT NOT NULL,
                    release_date TEXT NOT NULL,
                    title TEXT NOT NULL,
                    backdrop_path TEXT,
                    vote_average REAL NOT NULL)""")
        // Populate new favorite_movies table
        database.execSQL("""
                    INSERT INTO favorite_movies_new
                    SELECT movies.id, poster_path, overview, release_date, title, backdrop_path, vote_average
                    FROM favorite_movies_deprecated
                    INNER JOIN movies
                    ON favorite_movies_deprecated.id = movies.id""")


        // Create watch_later_movies table again
        database.execSQL("""CREATE TABLE watch_later_movies_new
                    (id INTEGER PRIMARY KEY NOT NULL,
                    poster_path TEXT,
                    overview TEXT NOT NULL,
                    release_date TEXT NOT NULL,
                    title TEXT NOT NULL,
                    backdrop_path TEXT,
                    vote_average REAL NOT NULL)""")
        // Populate new watch_later_movies table
        database.execSQL("""
                    INSERT INTO watch_later_movies_new
                    SELECT movies.id, poster_path, overview, release_date, title, backdrop_path, vote_average
                    FROM watch_later_movies_deprecated
                    INNER JOIN movies
                    ON watch_later_movies_deprecated.id = movies.id""")


        // Delete old tables
        database.execSQL("DROP TABLE IF EXISTS favorite_movies")
        database.execSQL("DROP TABLE IF EXISTS watch_later_movies")
        database.execSQL("DROP TABLE IF EXISTS movies")

        // Rename new tables
        database.execSQL("ALTER TABLE favorite_movies_new RENAME TO favorite_movies")
        database.execSQL("ALTER TABLE watch_later_movies_new RENAME TO watch_later_movies")
    }
}