package com.asmat.rolando.nightwing

import android.app.Application
import com.asmat.rolando.nightwing.di.components.ApplicationComponent
import com.asmat.rolando.nightwing.di.components.DaggerApplicationComponent
import com.asmat.rolando.nightwing.di.modules.ContextModule

class NightwingApplication: Application() {

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