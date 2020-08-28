package com.asmat.rolando.nightwing.di.modules

import com.asmat.rolando.nightwing.database.DatabaseManager
import com.asmat.rolando.nightwing.repositories.MoviesRepository
import com.asmat.rolando.nightwing.networking.TheMovieDBClient
import com.asmat.rolando.nightwing.repositories.PeopleRepository
import dagger.Module
import dagger.Provides
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Singleton

@Module
class RepositoriesModule {

    @Provides
    @Singleton
    fun provideMoviesRepository(databaseManager: DatabaseManager, client: TheMovieDBClient): MoviesRepository {
        return MoviesRepository(databaseManager, client, Schedulers.io(), AndroidSchedulers.mainThread())
    }

    @Provides
    @Singleton
    fun providePeopleRepository(client: TheMovieDBClient): PeopleRepository {
        return PeopleRepository(client, Schedulers.io(), AndroidSchedulers.mainThread())
    }
}