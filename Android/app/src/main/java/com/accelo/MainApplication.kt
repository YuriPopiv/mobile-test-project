package com.accelo

import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.core.content.getSystemService
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.work.*
import com.accelo.data.AcceloRepository
import com.accelo.data.LocalDataSource
import com.accelo.di.DaggerAppComponent
import com.accelo.di.DeliveryWorkerFactory
import com.accelo.util.NetworkUtils
import com.accelo.workers.DeliveryWorker
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Yuri Popiv on 11/26/2019.
 */
class MainApplication : DaggerApplication(), LifecycleObserver {

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.factory().create(applicationContext)
    }

//    private var connectivityMonitor : ConnectivityManager? = null
//
//    private val connectivityCallback = object : ConnectivityManager.NetworkCallback(){
//        override fun onAvailable(network: Network) {
//            super.onAvailable(network)
//        }
//
//        override fun onLost(network: Network) {
//            super.onLost(network)
//        }
//    }
//
//    private var monitoringConnectivity = false

    @Inject
    lateinit var localDataSource: LocalDataSource

    @Inject
    lateinit var networkUtils: NetworkUtils

    @Inject
    lateinit var workerFactory: DeliveryWorkerFactory

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
        ProcessLifecycleOwner.get().lifecycle
            .addObserver(this)

        WorkManager.initialize(this, Configuration.Builder().setWorkerFactory(workerFactory).build())

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun checkForNotDeliveredActivities() {

        if (localDataSource.hasNotDeliveredActivities) {
            if (networkUtils.hasNetworkConnection()) {
                val request = OneTimeWorkRequestBuilder<DeliveryWorker>().build()
                WorkManager.getInstance(this).enqueue(request)
            } else {

                //shedule re-attemt when network will be available
                val constraints =
                    Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
                val request =
                    OneTimeWorkRequestBuilder<DeliveryWorker>().setConstraints(constraints).build()
                WorkManager.getInstance(this).enqueue(request)
            }
        }
    }
}

//    fun startMonitoringConnectivity(){
//        connectivityMonitor = getSystemService<ConnectivityManager>()
//        connectivityMonitor?.let {
//            it.registerNetworkCallback(
//                NetworkRequest.Builder()
//                    .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
//                    .build(),
//                connectivityCallback
//            )
//            monitoringConnectivity = true
//            Timber.d("Start monitoring connectivity")
//
//        }
//
//    }
//
//    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
//    fun stopMonitoringConnectivity(){
//        if (monitoringConnectivity){
//            connectivityMonitor?.let {
//                it.unregisterNetworkCallback(connectivityCallback)
//                monitoringConnectivity  = false
//            }
//            Timber.d("Stop monitoring connectivity")
//        }
//    }
//}