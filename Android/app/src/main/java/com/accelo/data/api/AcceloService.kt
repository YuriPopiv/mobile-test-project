package com.accelo.data.api

import com.accelo.data.response.ActivityResponse
import com.accelo.data.response.UserResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import retrofit2.http.Url

/**
 * Created by Yuri Popiv on 11/26/2019.
 */
interface AcceloService {

    @POST("/oauth2/v0/token?")
    fun getToken(
        @Url url: String,
        @Query("code") code: String,
        @Query("grant_type") type: String = "authorization_code"

    ): Single<UserResponse>

    @GET("api/v0/activities/threads")
    fun getListActivity(
        @Url url: String,
        @Query("_fields") fields: String,
        @Query("_limit") limit: Int
    ): Single<ActivityResponse>


}