package com.asmat.rolando.nightwing.repositories

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class SchedulersProvider @Inject constructor() {

    open val mainScheduler = AndroidSchedulers.mainThread()

    open val ioScheduler = Schedulers.io()
}