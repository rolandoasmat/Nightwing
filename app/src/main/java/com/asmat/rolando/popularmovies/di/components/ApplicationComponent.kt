package com.asmat.rolando.popularmovies.di.components

import com.asmat.rolando.popularmovies.di.modules.ContextModule
import com.asmat.rolando.popularmovies.di.modules.DatabaseModule
import com.asmat.rolando.popularmovies.di.modules.NetworkModule
import com.asmat.rolando.popularmovies.di.modules.RepositoriesModule
import com.asmat.rolando.popularmovies.ui.activities.BaseActivity
import com.asmat.rolando.popularmovies.ui.activities.MovieDetailActivity
import com.asmat.rolando.popularmovies.ui.fragments.BaseGridFragment
import dagger.Component

@Component(modules = [ContextModule::class, DatabaseModule::class, NetworkModule::class, RepositoriesModule::class])
interface ApplicationComponent {

    fun inject(app: BaseActivity)
    fun inject(fragment: BaseGridFragment)

    fun inject(movieDetailActivity: MovieDetailActivity)

}