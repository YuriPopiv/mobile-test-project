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
 */

class DeliveryWorker(
    private val context: Context,
    workerParameters: WorkerParameters,
    private val repository: AcceloRepository
) : RxWorker(context, workerParameters) {


    override fun createWork(): Single<Result> {
        var errorsCount = 0
        var successCount = 0
        return repository.getNotDeliveredActivities()
            .flatMap { t: List<PendingActivity> -> Flowable.fromIterable(t) }
            .flatMap {activity : PendingActivity ->
                return@flatMap repository.createActivity( activity.subject, activity.body)
                    .doOnSuccess {
                        Timber.e("SUCCESS ${activity}")
                        successCount++
                        repository.deleteActivity(activity)
                    }
                    .onErrorReturn {
                        errorsCount++
                        Timber.e("ERROR ${activity}")
                        if (it is HttpException){
                            repository.deleteActivity(activity)
                        }else{
                            Timber.e(it)
                        }
                        CreatePostData()
                    }
//                    .doOnError {error ->
//                        Timber.e("ERROR")
//                        if (error is HttpException){
//                            when(error.code()){
//                                400 -> //Bad request
//                                401 ->
//                                    //NOT AUTHORIZED
//                                402 ->
//                                    //FORBIDDEN
//                                404 ->
//                                    //NOT FOUND
//
//                                500 ->
//                                //INTERNAL SERVER ERROR
//                            }
//                            //repository.deleteActivity(activity)
//                        }else{
//                            errorsCount++
//
//                        }
//                    }
                    .toFlowable()
            }//Just For combining result
            .collect({ mutableListOf<String>()}, { _, _: CreatePostData? ->

            })
            .map {
                Timber.e("${errorsCount} Errors")
                Timber.e("${successCount} Success")

                    Timber.e("DONE")
                    repository.deleteActivities()
                    Result.success()

//                }else{
//                    Result.retry()
//                }
            }

    }
}




