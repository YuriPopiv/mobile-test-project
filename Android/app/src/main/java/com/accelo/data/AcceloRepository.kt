package com.accelo.data

import com.accelo.data.LocalDataSource
import com.accelo.data.api.AcceloService
import com.accelo.data.api.AcceloService.Companion.AGAINST_ID
import com.accelo.data.api.AcceloService.Companion.AGAINST_TYPE
import com.accelo.data.api.AcceloService.Companion.MEDIUM
import com.accelo.data.api.AcceloService.Companion.OWNER_TYPE
import com.accelo.data.api.AcceloService.Companion.VISIBILITY
import com.accelo.data.response.ActivityResponse
import com.accelo.data.response.CreateActivityResponse
import com.accelo.data.response.UserResponse
import io.reactivex.Single
import javax.inject.Inject

/**
 * Created by Yuri Popiv on 11/26/2019.
 */
class AcceloRepository  @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val service: AcceloService
){

    private val url = "https://${localDataSource.deployment}.api.accelo.com/"

    fun saveDeploymentName(deployment: String){
        localDataSource.deployment = deployment

    }

    fun getToken(code: String) : Single<UserResponse> {
        return service.getToken(code)
            .doOnSuccess {
            localDataSource.accessToken = it.accessToken
        }
    }

    fun createActivity(subject: String, body: String): Single<CreateActivityResponse>{
        return service.createActivity(
            AGAINST_ID, AGAINST_TYPE, MEDIUM, OWNER_TYPE, VISIBILITY, subject, body)
    }

    fun getListActivity(
        fields: String,
        limit: Int
    ): Single<ActivityResponse> {
        return service.getListActivity(fields, limit)
    }

    fun search(
        fields: String,
        query: String
    ): Single<ActivityResponse> {
        return service.search(fields, query)
    }

}