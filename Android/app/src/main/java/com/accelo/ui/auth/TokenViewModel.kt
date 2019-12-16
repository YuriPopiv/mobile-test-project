package com.accelo.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.accelo.data.AcceloRepository
import com.accelo.data.base.BaseViewModel
import com.accelo.data.response.UserResponse
import com.accelo.util.Event
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Yuri Popiv on 11/26/2019.
 */
class TokenViewModel @Inject constructor(
    private val repository: AcceloRepository
) : BaseViewModel() {

    val userResponse: LiveData<Event<UserResponse>> get() = _userResponse
    private val _userResponse = MutableLiveData<Event<UserResponse>>()

    val snackbarMessage: LiveData<Event<String>> get() = _snackbarMessage
    private val _snackbarMessage = MutableLiveData<Event<String>>()

    fun getToken(code: String) {
        addSubscription(
            repository.getToken(code)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onSuccessGetToken, this::onErrorGetToken)
        )
    }

    private fun onSuccessGetToken(userResponse: UserResponse) {
        Timber.d("Token received")
        _userResponse.postValue(Event(userResponse))
    }

    private fun onErrorGetToken(throwable: Throwable) {
        Timber.e(throwable)
        _snackbarMessage.postValue(Event(throwable.localizedMessage))
    }
}