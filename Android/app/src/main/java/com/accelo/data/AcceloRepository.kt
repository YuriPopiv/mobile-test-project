package com.accelo.data

import com.accelo.data.api.AcceloService
import com.accelo.data.api.AcceloService.Companion.AGAINST_ID
import com.accelo.data.api.AcceloService.Companion.AGAINST_TYPE
import com.accelo.data.api.AcceloService.Companion.MEDIUM
import com.accelo.data.api.AcceloService.Companion.OWNER_TYPE
import com.accelo.data.api.AcceloService.Companion.VISIBILITY
import com.accelo.data.model.ActivityData
import com.accelo.data.model.CreatePostData
import com.accelo.data.model.FullActivity
import com.accelo.data.response.UserResponse
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

/**
 * Created by Yuri Popiv on 11/26/2019.
 */
class AcceloRepository @Inject constructor(
    private val localDataSource: LocalDataSource,
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

    fun getListActivity(page: Int): Single<ActivityData> {
        val fields = "interacts,date_logged,preview_body"
        val pageLimit = 20
        return service.getListActivity(fields, pageLimit, page)
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

}