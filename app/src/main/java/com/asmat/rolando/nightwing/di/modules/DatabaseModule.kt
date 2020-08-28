package com.asmat.rolando.nightwing.di.modules

import androidx.room.Room
import android.content.Context
import com.asmat.rolando.nightwing.database.AppDatabase
import com.asmat.rolando.nightwing.database.DatabaseManager
import com.asmat.rolando.nightwing.database.MoviesDAO
import com.asmat.rolando.nightwing.database.migrations.Migration_1_2
import com.asmat.rolando.nightwing.database.migrations.Migration_2_3
import dagger.Module
import dagger.Provides
import io.reactivex.schedulers.Schedulers

@Module
class DatabaseModule {

    companion object {
        private const val DATABASE_NAME = "movies-database"
    }

    @Provides
    fun provideDAO(context: Context): MoviesDAO {
        return Room.databaseBuilder(context,
                AppDatabase::class.java, DATABASE_NAME)
                .addMigrations(
                        Migration_1_2(),
                        Migration_2_3())
                .fallbackToDestructiveMigration()
                .build()
                .moviesDAO()
    }

    @Provides
    fun provideDatabaseManager(dao: MoviesDAO): DatabaseManager {
        return DatabaseManager(dao, Schedulers.io())
    }

}