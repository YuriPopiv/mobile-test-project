package com.accelo.di

import androidx.lifecycle.ViewModel
import com.accelo.ui.auth.TokenViewModel
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
    abstract fun bindTokenViewModel(viewModel: TokenViewModel) : ViewModel
}