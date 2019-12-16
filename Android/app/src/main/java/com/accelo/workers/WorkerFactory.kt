package com.accelo.workers

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.accelo.data.AcceloRepository
import com.accelo.workers.DeliveryWorker
import dagger.MapKey
import javax.inject.Inject
import kotlin.reflect.KClass

/**
 * Created by Yuri Popiv on 12/4/2019.
 *
 * Factory for the [DeliveryWorker]
 */
class DeliveryWorkerFactory @Inject constructor(
    private val repository: AcceloRepository
) : WorkerFactory() {
    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? {
        return DeliveryWorker(appContext, workerParameters, repository)
    }


}

@MapKey
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class WorkerKey(val value: KClass<out ListenableWorker>)