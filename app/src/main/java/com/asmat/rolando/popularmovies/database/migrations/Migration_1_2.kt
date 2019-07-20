package com.asmat.rolando.popularmovies.database.migrations

import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.room.migration.Migration

/**
 * Added watch_later_movies table
 */
class Migration_1_2: Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
                "CREATE TABLE `watch_later_movies` (`id` INTEGER NOT NULL, "
                        + " PRIMARY KEY(`id`),"
                        + " FOREIGN KEY(`id`) REFERENCES `movies`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE)")
    }
}