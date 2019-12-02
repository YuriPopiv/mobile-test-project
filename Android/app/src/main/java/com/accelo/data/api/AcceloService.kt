package com.accelo.data.api

import com.accelo.data.base.ResponseModel
import com.accelo.data.model.ActivityData
import com.accelo.data.model.CreatePostData
import com.accelo.data.model.FullActivity
import com.accelo.data.response.UserResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created by Yuri Popiv on 11/26/2019.
 */
interface AcceloService {

    @POST("/oauth2/v0/token?")
    fun getToken(
        @Query("code") code: String,
        @Query("grant_type") type: String = "authorization_code"
    ): Single<UserResponse>

    @POST("api/v0/activities?")
    fun createActivity(
        @Query("against_id") againstId: Int,
        @Query("against_type") againstType: String,
        @Query("medium") medium: String,
        @Query("owner_type") ownerType: String,
        @Query("visibility") visibility: String,
        @Query("subject") subject: String,
        @Query("body") body: String
    ): Single<ResponseModel<CreatePostData>>

    @GET("api/v0/activities/threads")
    fun getListActivity(
        @Query("_fields") fields: String,
        @Query("_limit") limit: Int
    ): Single<ResponseModel<ActivityData>>


    @GET("api/v0/activities/threads")
    fun search(
        @Query("_fields") fields: String,
        @Query("q") query: String
    ): Single<ResponseModel<ActivityData>>

    @GET("api/v0/activities/{activityId}")
    fun getFullActivity(@Path("activityId") activityId: String,
                        @Query("_fields") fields: String) : Single<ResponseModel<FullActivity>>

    companion object {
        //Mock data for createActivity()
        const val AGAINST_ID = 47
        const val AGAINST_TYPE = "task"
        const val MEDIUM = "note"
        const val OWNER_TYPE = "Staff"
        const val VISIBILITY = "all"
    }

}