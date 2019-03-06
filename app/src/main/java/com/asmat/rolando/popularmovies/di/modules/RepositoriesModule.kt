package com.asmat.rolando.popularmovies.di.modules

import com.asmat.rolando.popularmovies.database.DatabaseManager
import com.asmat.rolando.popularmovies.model.MoviesRepository
import com.asmat.rolando.popularmovies.networking.the.movie.db.TheMovieDBClient
import dagger.Module
import dagger.Provides

@Module
class RepositoriesModule {

    @Provides
    fun provideMoviesRepository(databaseManager: DatabaseManager, client: TheMovieDBClient ): MoviesRepository {
        return MoviesRepository(databaseManager, client)
    }
}