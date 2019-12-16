package com.accelo.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.accelo.data.AcceloRepository
import com.accelo.data.base.BaseViewModel
import com.accelo.util.Event
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Yuri Popiv on 11/26/2019.
 */
class AuthViewModel @Inject constructor(
    private val repository: AcceloRepository
) : BaseViewModel() {

    val snackbarMessage: LiveData<String> get() = _snackbarMessage
    private val _snackbarMessage = MutableLiveData<String>()

    fun saveDeploymentName(deployment: String) {
        addSubscription(
            repository.saveDeploymentName(deployment)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onSuccessSaveDeploymentName, this::onErrorSaveDeploymentName)
        )
    }

    private fun onSuccessSaveDeploymentName() {
        Timber.i("Successfully saved deployment name")
    }

    private fun onErrorSaveDeploymentName(throwable: Throwable) {
        Timber.e(throwable)
        _snackbarMessage.postValue(throwable.localizedMessage)
    }

}