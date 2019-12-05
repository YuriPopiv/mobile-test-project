package com.accelo.data

import android.content.SharedPreferences
import android.util.Base64
import com.accelo.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

/**
 * Created by Yuri Popiv on 11/26/2019.
 */
class AcceloAuthInterceptor @Inject constructor(
    private val localData: LocalDataSource
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
        val endpoint = chain.request().url().encodedPath()

        if (endpoint.contains("oauth2")){
            val credentials = "${BuildConfig.CLIENT_ID}:${BuildConfig.CLIENT_SECRET}"


            val base64 = Base64.encodeToString(credentials.toByteArray(), Base64.NO_WRAP)

            request.addHeader("Authorization", "Basic $base64")
        }else{
            request.addHeader("Authorization", "Bearer ${localData.getToken()}")

        }

        return chain.proceed(request.build())
    }
}