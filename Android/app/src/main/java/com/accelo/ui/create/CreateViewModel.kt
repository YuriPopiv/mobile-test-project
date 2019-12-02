package com.accelo.ui.create

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.accelo.data.AcceloRepository
import com.accelo.data.response.ActivityResponse
import com.accelo.data.response.CreateActivityResponse
import com.accelo.util.Event
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Yuri Popiv on 11/26/2019.
 */
class CreateViewModel @Inject constructor(
    private val repo: AcceloRepository
) : ViewModel() {

    private val subscription: CompositeDisposable = CompositeDisposable()

    val activityData: LiveData<Event<CreateActivityResponse>> get() = _activityData
    private val _activityData = MutableLiveData<Event<CreateActivityResponse>>()

    val snackbarMessage: LiveData<Event<String>> get() = _snackbarMessage
    private val _snackbarMessage = MutableLiveData<Event<String>>()

    val isLoading: LiveData<Boolean> get() = _isLoading
    private val _isLoading = MutableLiveData(true)


    fun createActivity(body: String, subject: String) {

        subscription.add(repo.createActivity(body, subject)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { _isLoading.postValue(true) }
            .doFinally { _isLoading.postValue(false) }
            .subscribe(
                { response ->
                    if (response != null) {
                        _activityData.postValue(Event(response))
                    } else {
                        Timber.e("Error creating activity")
                    }
                }, { t ->
                    Timber.e(t)
                    _snackbarMessage.postValue(Event(t.localizedMessage))
                })
        )

    }


    override fun onCleared() {
        super.onCleared()
        subscription.clear()
    }
}