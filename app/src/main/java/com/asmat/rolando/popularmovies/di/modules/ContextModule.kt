package com.asmat.rolando.popularmovies.di.modules

import android.content.Context
import dagger.Module
import dagger.Provides

@Module
class ContextModule(val context: Context) {

    @Provides
    fun provideContext(): Context {
        return context
    }

}