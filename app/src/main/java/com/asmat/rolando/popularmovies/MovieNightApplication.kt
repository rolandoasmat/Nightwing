package com.asmat.rolando.popularmovies

import android.app.Application
import com.asmat.rolando.popularmovies.di.components.ApplicationComponent
import com.asmat.rolando.popularmovies.di.components.DaggerApplicationComponent
import com.asmat.rolando.popularmovies.di.modules.ContextModule

class MovieNightApplication: Application() {

    private lateinit var applicationComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()
        applicationComponent = DaggerApplicationComponent
                .builder()
                .contextModule(ContextModule(applicationContext))
                .build()
    }

    fun component(): ApplicationComponent {
        return applicationComponent
    }

}