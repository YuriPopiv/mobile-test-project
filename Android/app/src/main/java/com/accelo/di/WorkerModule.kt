package com.accelo.di

import androidx.work.Worker
import androidx.work.WorkerFactory
import com.accelo.workers.DeliveryWorker
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Created by Yuri Popiv on 12/4/2019.
 */
@Module
internal abstract class WorkerModule {

    @Binds
    @IntoMap
    @WorkerKey(DeliveryWorker::class)
    internal abstract fun bindWorkerFactory( worker: DeliveryWorkerFactory) : WorkerFactory

}