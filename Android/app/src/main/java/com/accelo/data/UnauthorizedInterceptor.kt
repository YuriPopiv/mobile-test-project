package com.accelo.data

import android.content.Context
import android.content.Intent
import android.util.Base64
import com.accelo.BuildConfig
import com.accelo.MainApplication
import com.accelo.data.database.ActivityDao
import com.accelo.ui.launcher.LauncherActivity
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

/**
 * Created by Yuri Popiv on 12/5/2019.
 */
class UnauthorizedInterceptor @Inject constructor(
    private val context: Context,
    private val localDataSource: LocalDataSource,
    private val dao: ActivityDao
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)

        if (response.code() == 401){

            localDataSource.clearData()
            dao.deleteAllActivities()

            val intent = Intent(context, LauncherActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }

        return response
    }
}