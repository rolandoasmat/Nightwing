package com.asmat.rolando.popularmovies.database;

import android.arch.persistence.room.Room;
import android.content.Context;

public enum DatabaseManager {

    INSTANCE;

    private AppDatabase db;
    private final String DATABASE_NAME = "movies-database";

    public AppDatabase getInstance() {
        return this.db;
    }

    public void setInstance(Context applicationContext) {
        AppDatabase db = Room.databaseBuilder(applicationContext,
                AppDatabase.class, DATABASE_NAME).build();
        this.db = db;
    }
}
