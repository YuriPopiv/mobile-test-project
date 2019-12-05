package com.accelo.data

import com.accelo.data.api.AcceloService
import com.accelo.data.api.AcceloService.Companion.AGAINST_ID
import com.accelo.data.api.AcceloService.Companion.AGAINST_TYPE
import com.accelo.data.api.AcceloService.Companion.MEDIUM
import com.accelo.data.api.AcceloService.Companion.OWNER_TYPE
import com.accelo.data.api.AcceloService.Companion.VISIBILITY
import com.accelo.data.database.ActivityDao
import com.accelo.data.database.PendingActivity
import com.accelo.data.model.ActivityData
import com.accelo.data.model.CreatePostData
import com.accelo.data.model.FullActivity
import com.accelo.data.response.UserResponse
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import javax.inject.Inject

/**
 * Created by Yuri Popiv on 11/26/2019.
 */
class AcceloRepository @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val dataSource: ActivityDao,
    private val service: AcceloService
) {

    private val url = "https://${localDataSource.deployment}.api.accelo.com/"

    fun saveDeploymentName(deployment: String): Completable {
        return Completable.fromAction {
            localDataSource.deployment = deployment
        }
    }

    fun getToken(code: String): Single<UserResponse> {
        return service.getToken(code)
            .doOnSuccess {
                localDataSource.accessToken = it.accessToken
            }
    }

    fun createActivity(subject: String, body: String): Single<CreatePostData> {
        return service.createActivity(
            AGAINST_ID, AGAINST_TYPE, MEDIUM, OWNER_TYPE, VISIBILITY, subject, body
        )
            .map { it.response }
    }

    fun getListActivity(
        fields: String,
        limit: Int
    ): Single<ActivityData> {
        return service.getListActivity(fields, limit)
            .map { it.response }
    }

    fun search(
        fields: String,
        query: String
    ): Single<ActivityData> {
        return service.search(fields, query)
            .map { it.response }
    }

    fun getFullActivity(activityId: String): Single<FullActivity> {
        val fields = "id,html_body,interacts, date_logged"
        return service.getFullActivity(activityId, fields)
            .map { it.response }
    }

    fun delete(activityId: String): Single<ResponseBody> {
        val fields = "id,html_body,interacts, date_logged"
        return service.deleteActivity(activityId)
    }

    fun getNotDeliveredActivities(): Flowable<List<PendingActivity>>{

        return Flowable.fromCallable { dataSource.getActivities() }


    }

    fun saveActivityForFutureDelivery(subject: String, body: String): Completable{
        val activity = PendingActivity(
            System.currentTimeMillis() ,AGAINST_ID, AGAINST_TYPE, MEDIUM, OWNER_TYPE, VISIBILITY, subject, body
        )
        return dataSource.insertActivity(activity)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnComplete {
                localDataSource.hasNotDeliveredActivities = true

            }


    }

    fun deleteActivity(activity: PendingActivity){
        dataSource.deleteActivity(activity)
    }

    fun deleteActivities(){
        localDataSource.hasNotDeliveredActivities = false
        dataSource.deleteAllActivities()
    }

}