package com.asmat.rolando.nightwing.di.modules

import androidx.room.Room
import android.content.Context
import com.asmat.rolando.nightwing.database.NightwingDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

    companion object {
        private const val DATABASE_NAME = "nightwing-database"
    }

    @Singleton
    @Provides
    fun provideAppDatabase(context: Context): NightwingDatabase {
        return Room.databaseBuilder(context,
                NightwingDatabase::class.java, DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .build()
    }

}