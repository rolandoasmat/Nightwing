package com.asmat.rolando.popularmovies.di.modules

import com.asmat.rolando.popularmovies.networking.the.movie.db.TheMovieDBClient
import dagger.Module
import dagger.Provides

@Module
class NetworkModule {

    @Provides
    fun provideTheMovieDBClient(): TheMovieDBClient {
        return TheMovieDBClient()
    }
}