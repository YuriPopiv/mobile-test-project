package com.accelo.ui.auth

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.accelo.data.AcceloRepository
import com.accelo.data.response.UserResponse
import com.accelo.util.Event
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Yuri Popiv on 11/26/2019.
 */
class TokenViewModel@Inject constructor(
    private val repository: AcceloRepository
) : ViewModel() {

    private val subscription: CompositeDisposable = CompositeDisposable()

    val userResponse: LiveData<Event<UserResponse>> get() = _tokenResponse
    private val _tokenResponse = MutableLiveData<Event<UserResponse>>()

    val snackbarMessage: LiveData<Event<String>> get() = _snackbarMessage
    private val _snackbarMessage = MutableLiveData<Event<String>>()

    fun getToken(code: String) {

        subscription.add(repository.getToken(code)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe( {response ->
                val token = response.accessToken
                _tokenResponse.postValue(Event(response))
            },{ t ->
                _snackbarMessage.postValue(Event(t.localizedMessage))
                Timber.e(t)
            }))

    }

    override fun onCleared() {
        super.onCleared()
        subscription.clear()
    }
}