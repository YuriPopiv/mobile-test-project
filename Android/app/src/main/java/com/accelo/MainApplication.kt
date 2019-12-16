package com.accelo

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.work.Configuration
import androidx.work.WorkManager
import com.accelo.di.DaggerAppComponent
import com.accelo.workers.DeliveryWorkerFactory
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Yuri Popiv on 11/26/2019.
 */
class MainApplication : DaggerApplication(), LifecycleObserver {

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.factory().create(applicationContext)
    }

    @Inject
    lateinit var workerFactory: DeliveryWorkerFactory

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
        ProcessLifecycleOwner.get().lifecycle
            .addObserver(this)

        WorkManager.initialize(
            this,
            Configuration.Builder().setWorkerFactory(workerFactory).build()
        )
    }
}