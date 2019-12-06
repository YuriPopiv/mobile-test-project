package com.accelo.di

import android.content.Context
import android.content.SharedPreferences
import com.accelo.data.interceptors.AcceloAuthInterceptor
import com.accelo.data.LocalDataSource
import com.accelo.data.interceptors.UnauthorizedInterceptor
import com.accelo.data.api.AcceloService
import com.accelo.data.database.ActivityDatabase
import com.accelo.data.database.ActivityDao
import com.accelo.data.interceptors.DynamicUrlInterceptor
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
        authInterceptor: AcceloAuthInterceptor,
        dynamicUrlInterceptor: DynamicUrlInterceptor
    ): AcceloService {
        val client = OkHttpClient.Builder()
            .addInterceptor(
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            )
            .addInterceptor(unauthorizedInterceptor)
            .addInterceptor(authInterceptor)
            .addInterceptor(dynamicUrlInterceptor)
            .build()
        return Retrofit.Builder()
            .baseUrl("https://api.accelo.com/")
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

    @Singleton
    @Provides
    fun provideLocalDataSource(sharedPreferences: SharedPreferences): LocalDataSource {
        return LocalDataSource(sharedPreferences)
    }

    @Singleton
    @Provides
    fun provideDynamicUrlInterceptor(localDataSource: LocalDataSource): DynamicUrlInterceptor {
        return DynamicUrlInterceptor(localDataSource)
    }
}

const val PREFS = "ACCELO_PREFS"