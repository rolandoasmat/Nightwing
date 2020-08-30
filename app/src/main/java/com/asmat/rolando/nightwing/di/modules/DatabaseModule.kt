package com.asmat.rolando.nightwing.di.modules

import androidx.room.Room
import android.content.Context
import com.asmat.rolando.nightwing.database.AppDatabase
import com.asmat.rolando.nightwing.database.DatabaseManager
import com.asmat.rolando.nightwing.database.MoviesDAO
import dagger.Module
import dagger.Provides
import io.reactivex.schedulers.Schedulers

@Module
class DatabaseModule {

    companion object {
        private const val DATABASE_NAME = "nightwing-database"
    }

    @Provides
    fun provideDAO(context: Context): MoviesDAO {
        return Room.databaseBuilder(context,
                AppDatabase::class.java, DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .build()
                .moviesDAO()
    }

    @Provides
    fun provideDatabaseManager(dao: MoviesDAO): DatabaseManager {
        return DatabaseManager(dao, Schedulers.io())
    }

}