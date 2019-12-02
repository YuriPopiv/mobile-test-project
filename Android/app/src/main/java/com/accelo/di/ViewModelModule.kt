package com.accelo.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.accelo.ui.auth.AuthViewModel
import com.accelo.ui.auth.TokenViewModel
import com.accelo.ui.create.CreateViewModel
import com.accelo.ui.launcher.LauncherViewModel
import com.accelo.ui.stream.StreamViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Created by Yuri Popiv on 11/26/2019.
 */
@Module
internal abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(TokenViewModel::class)
    abstract fun bindTokenViewModel(viewModel: TokenViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AuthViewModel::class)
    abstract fun bindAuthViewModel(viewModel: AuthViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(StreamViewModel::class)
    abstract fun bindStreamViewModell(viewModel: StreamViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LauncherViewModel::class)
    abstract fun bindLauncherViewModel(viewModel: LauncherViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CreateViewModel::class)
    abstract fun bindCreateViewModel(viewModel: CreateViewModel): ViewModel


    @Binds
    internal abstract fun bindViewModelFactory(
        factory: ViewModelFactory): ViewModelProvider.Factory

}