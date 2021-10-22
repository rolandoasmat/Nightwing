package com.asmat.rolando.nightwing.repositories

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class SchedulersProvider @Inject constructor() {

    val mainScheduler = AndroidSchedulers.mainThread()

    val ioScheduler = Schedulers.io()
}