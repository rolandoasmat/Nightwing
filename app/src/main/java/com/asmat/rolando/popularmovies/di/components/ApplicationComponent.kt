package com.asmat.rolando.popularmovies.di.components

import com.asmat.rolando.popularmovies.di.modules.DatabaseModule
import dagger.Component

@Component(modules = [DatabaseModule::class])
interface ApplicationComponent {



}