package com.accelo.workers

import android.content.Context
import android.util.Log
import androidx.work.ListenableWorker
import androidx.work.RxWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.accelo.data.AcceloAuthInterceptor
import com.accelo.data.AcceloRepository
import com.accelo.data.LocalDataSource
import com.accelo.data.api.AcceloService
import com.accelo.data.database.ActivityDatabase
import com.accelo.data.database.PendingActivity
import com.accelo.data.model.CreatePostData
import com.accelo.di.PREFS
import com.accelo.util.Event
import com.google.gson.GsonBuilder
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.intellij.lang.annotations.Flow
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.util.concurrent.Callable

/**
 * Created by Yuri Popiv on 12/3/2019.
 */

class DeliveryWorker(
    private val context: Context,
    workerParameters: WorkerParameters,
    private val repository: AcceloRepository
) : RxWorker(context, workerParameters) {


    override fun createWork(): Single<Result> {

        val ac2 = PendingActivity(
            1, 1,
            AcceloService.AGAINST_TYPE,
            AcceloService.MEDIUM,
            AcceloService.OWNER_TYPE,
            AcceloService.VISIBILITY, "subject1111", "body"
        )

        val ac1 = PendingActivity(
            1, 1,
            AcceloService.AGAINST_TYPE,
            AcceloService.MEDIUM,
            AcceloService.OWNER_TYPE,
            AcceloService.VISIBILITY, "subject2222", "body"
        )

        val list = Observable.just(listOf(ac1, ac2))
        val ids = mutableListOf<String?>()
        return repository.getNotDeviveredActivities()
            .flatMap { t: List<PendingActivity> -> Flowable.fromIterable(t) }
            .flatMap {t : PendingActivity -> repository.createActivity( t.subject, t.body)
                .doOnSuccess {Timber.e("SUCCESS")}
                .doOnError { Timber.e("ERROR") }
                .toFlowable() }

            .doOnComplete {
            }.collect({ mutableListOf<Result>()}, { t1, t2: CreatePostData? ->
                ids.add(t2?.id)
                Timber.e("${t2?.id} COMPLETED")
                t1.add(Result.success())
            }).map {
                repository.deleteActivity()
                Timber.e("ALL SENT")

                Result.success()
            }

        //return Single.just(Result.success())



//        Observable.just(1)
//            .flatMap {
//                return@flatMap Observable.just(it * 10)
//            }.flatMap {
//                return@flatMap Observable.just(it * 20)
//            }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
//            .subscribe({
//                //OnNext
//                Log.d("result", "" + it)
//            }, {
//                it.printStackTrace()
//                //on error
//            }, {
//                //on complete
//            })

//        repository.getNotDeviveredActivities()
//            .flatMap { return@flatMap Observable.just("") }


//        return repository.createActivity( "test offline", "")
//            .doOnSuccess { response ->
//            if (response != null) {
//                Timber.e(response.toString())
//            }
//            Result.success()
//        }.doOnError { t ->
//            Timber.e(t)
//            Result.failure()
//        }.map {
//            Result.success()
//        }
    }
}




