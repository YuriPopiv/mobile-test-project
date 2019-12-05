package com.accelo.di

import android.content.Context
import android.content.SharedPreferences
import com.accelo.data.AcceloAuthInterceptor
import com.accelo.data.LocalDataSource
import com.accelo.data.UnauthorizedInterceptor
import com.accelo.data.api.AcceloService
import com.accelo.data.database.ActivityDatabase
import com.accelo.data.database.ActivityDao
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * Created by Yuri Popiv on 11/26/2019.
 */
@Module
class AppModule {
    @Singleton
    @Provides
    fun provideService(
        unauthorizedInterceptor: UnauthorizedInterceptor,
        authInterceptor: AcceloAuthInterceptor
    ): AcceloService {
        val client = OkHttpClient.Builder()
            .addInterceptor(
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            )
            .addInterceptor(unauthorizedInterceptor)
            .addInterceptor(authInterceptor)
            .build()
        return Retrofit.Builder()
            //TODO should be dynamic
            .baseUrl("https://nk-company-2019.api.accelo.com/")
            .client(client)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder()
                        .setLenient()
                        .create()
                )
            )
            .build()
            .create(AcceloService::class.java)
    }

    @Singleton
    @Provides
    fun provideSharedPreferences(context: Context): SharedPreferences {
        return context.applicationContext.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
    }

    @Singleton
    @Provides
    fun provideActivityDao(context: Context): ActivityDao {
        return ActivityDatabase.getInstance(context).activityDao()
    }
}

const val PREFS = "ACCELO_PREFS"