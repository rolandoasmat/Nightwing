package com.asmat.rolando.popularmovies.di.modules

import com.asmat.rolando.popularmovies.database.DatabaseManager
import com.asmat.rolando.popularmovies.repositories.MoviesRepository
import com.asmat.rolando.popularmovies.networking.the.movie.db.TheMovieDBClient
import dagger.Module
import dagger.Provides
import io.reactivex.schedulers.Schedulers
import javax.inject.Singleton

@Module
class RepositoriesModule {

    @Provides
    @Singleton
    fun provideMoviesRepository(databaseManager: DatabaseManager, client: TheMovieDBClient): MoviesRepository {
        return MoviesRepository(databaseManager, client, Schedulers.computation())
    }
}