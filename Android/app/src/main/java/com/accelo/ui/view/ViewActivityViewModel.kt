package com.accelo.ui.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.accelo.data.AcceloRepository
import com.accelo.data.base.BaseViewModel
import com.accelo.data.model.FullActivity
import com.accelo.util.Event
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by dmytro on 12/2/19
 */
class ViewActivityViewModel @Inject constructor(private val repository: AcceloRepository) :
    BaseViewModel() {

    val fullActivityResponse: LiveData<Event<FullActivity>> get() = _fullActivityResponse
    private val _fullActivityResponse = MutableLiveData<Event<FullActivity>>()

    val snackbarMessage: LiveData<String> get() = _snackbarMessage
    private val _snackbarMessage = MutableLiveData<String>()

    val isLoading: LiveData<Boolean> get() = _isLoading
    private val _isLoading = MutableLiveData(true)

    fun getFullActivity(activityId: String) {
        addSubscription(
            repository.getFullActivity(activityId)
                .doOnSubscribe { _isLoading.postValue(true) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onSuccessGetFullActivity, this::onErrorGetFullActivity)
        )
    }

    private fun onSuccessGetFullActivity(fullActivityResponse: FullActivity) {
        Timber.d("Full activity data received ")
        _isLoading.postValue(false)
        _fullActivityResponse.postValue(Event(fullActivityResponse))
    }

    private fun onErrorGetFullActivity(throwable: Throwable) {
        Timber.e(throwable)
        _isLoading.postValue(false)
        _snackbarMessage.postValue(throwable.localizedMessage)
    }
}