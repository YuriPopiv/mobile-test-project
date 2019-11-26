package com.accelo.di

import com.accelo.ui.auth.AuthActivity
import com.accelo.ui.auth.TokenActivity
import com.accelo.ui.stream.StreamActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by Yuri Popiv on 11/26/2019.
 */
@Module
internal abstract class ActivityBindingModule {

    @ContributesAndroidInjector(modules = [ViewModelModule::class])
    abstract fun contributeTokenActivity(): TokenActivity

    @ContributesAndroidInjector(modules = [ViewModelModule::class])
    abstract fun contributeAuthActivity(): AuthActivity

    @ContributesAndroidInjector(modules = [ViewModelModule::class])
    abstract fun contributeStreamActivity(): StreamActivity
}