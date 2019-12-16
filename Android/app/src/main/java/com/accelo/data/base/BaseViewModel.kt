package com.accelo.data.base

import androidx.annotation.CallSuper
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * Created by dmytro on 12/2/19
 */
abstract class BaseViewModel : ViewModel() {

    private val subscriptions by lazy { CompositeDisposable() }

    protected fun addSubscription(vararg disposable: Disposable) {
        subscriptions.addAll(*disposable)
    }

    protected fun clearSubscriptions() = subscriptions.clear()

    @CallSuper
    override fun onCleared() {
        super.onCleared()
        clearSubscriptions()
    }
}