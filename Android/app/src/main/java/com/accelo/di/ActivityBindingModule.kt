package com.accelo.di

import com.accelo.ui.auth.TokenActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by Yuri Popiv on 11/26/2019.
 */
@Module
internal abstract class ActivityBindingModule {

    @ContributesAndroidInjector(modules = [ViewModelModule::class])
    abstract fun contributeTokenActivity(): TokenActivity
}