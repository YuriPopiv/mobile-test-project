package com.accelo.di

import android.content.Context
import com.accelo.MainApplication
import com.accelo.workers.WorkerModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton

/**
 * Created by Yuri Popiv on 11/26/2019.
 *
 * Main component of the app, each new module should be added to the list of modules
 */

@Singleton
@Component(
    modules = [
        AndroidInjectionModule::class,
        ActivityBindingModule::class,
        ViewModelModule::class,
        WorkerModule::class,
        AppModule::class
    ]
)
interface AppComponent : AndroidInjector<MainApplication> {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance applicationContext: Context): AppComponent
    }
}