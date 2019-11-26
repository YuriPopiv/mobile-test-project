package com.accelo.util

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * Created by Yuri Popiv on 11/26/2019.
 */

inline fun <reified VM: ViewModel> FragmentActivity.viewModelProvider(
    factory: ViewModelProvider.Factory
) = ViewModelProvider(this, factory).get(VM::class.java)