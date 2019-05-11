package com.asmat.rolando.popularmovies.di.components

import com.asmat.rolando.popularmovies.di.modules.DatabaseModule
import com.asmat.rolando.popularmovies.ui.activities.BaseActivity
import com.asmat.rolando.popularmovies.ui.fragments.BaseGridFragment
import dagger.Component

@Component(modules = [DatabaseModule::class])
interface ApplicationComponent {

    fun inject(app: BaseActivity)
    fun inject(fragment: BaseGridFragment)

}