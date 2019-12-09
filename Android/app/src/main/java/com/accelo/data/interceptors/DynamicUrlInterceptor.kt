package com.accelo.data.interceptors

import com.accelo.data.LocalDataSource
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by dmytro on 12/6/19
 */
@Singleton
class DynamicUrlInterceptor @Inject constructor(
    private val localDataSource: LocalDataSource
) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        var original = chain.request()
        val hardcodedUrl = HttpUrl.parse(chain.request().url().toString())
        val scheme = hardcodedUrl?.scheme()
        val host = "${localDataSource.getDeploymentName()}.${hardcodedUrl?.host()}"

        // If new Base URL is properly formatted than replace with old one
        if (scheme != null) {
            val newUrl = original.url().newBuilder()
                .scheme(scheme)
                .host(host)
                .build()

            original = original.newBuilder()
                .url(newUrl)
                .build()
        }
        return chain.proceed(original)
    }
}