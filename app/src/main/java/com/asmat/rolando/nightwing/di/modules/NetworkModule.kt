package com.asmat.rolando.nightwing.di.modules

import com.asmat.rolando.nightwing.networking.TheMovieDBClient
import dagger.Module
import dagger.Provides

@Module
class NetworkModule {

    @Provides
    fun provideTheMovieDBClient(): TheMovieDBClient {
        return TheMovieDBClient()
    }
}