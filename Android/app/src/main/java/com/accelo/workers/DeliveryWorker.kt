package com.accelo.workers

import android.content.Context
import androidx.work.RxWorker
import androidx.work.WorkerParameters
import com.accelo.data.AcceloRepository
import com.accelo.data.database.PendingActivity
import com.accelo.data.model.CreatePostData
import io.reactivex.*
import retrofit2.HttpException
import timber.log.Timber

/**
 * Created by Yuri Popiv on 12/3/2019.
 *
 * Delivery Worker creates the activities that weren't send to server due to Network issues
 */
class DeliveryWorker(
    context: Context,
    workerParameters: WorkerParameters,
    private val repository: AcceloRepository
) : RxWorker(context, workerParameters) {


    override fun createWork(): Single<Result> {
        return repository.getNotDeliveredActivities()
            .flatMap { t: List<PendingActivity> -> Flowable.fromIterable(t) }
            .flatMap {activity : PendingActivity ->
                return@flatMap repository.createActivity( activity.subject, activity.body)
                    .doOnSuccess {
                        Timber.e("Activity created $activity")
                        repository.deleteActivity(activity)
                    }
                    .onErrorReturn {
                        Timber.e("Error creating activity $activity")
                        //If we get back the 400, 500, activity will be deleted from the device
                        //It will be not created during future retry
                        if (it is HttpException){
                            repository.deleteActivity(activity)
                        }else{
                            Timber.e(it)
                        }
                        CreatePostData()
                    }
                    .toFlowable()
            }//Just For combining result
            .collect({ mutableListOf<String>()}, { _, _: CreatePostData? ->

            })
            .map {
                    Timber.e("All activities successfully created")
                    repository.deleteActivities()
                    Result.success()
            }

    }
}




