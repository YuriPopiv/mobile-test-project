package com.accelo.ui.stream

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.accelo.data.AcceloRepository
import com.accelo.data.response.ActivityResponse
import com.accelo.util.Event
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Yuri Popiv on 11/26/2019.
 */
class StreamViewModel @Inject constructor(
    private val repo: AcceloRepository
) : ViewModel() {

    private val subscription: CompositeDisposable = CompositeDisposable()


    val activityData: LiveData<Event<ActivityResponse>> get() = _activityData
    private val _activityData = MutableLiveData<Event<ActivityResponse>>()

    val snackbarMessage: LiveData<Event<String>> get() = _snackbarMessage
    private val _snackbarMessage = MutableLiveData<Event<String>>()

    fun getActivities() {

        subscription.add(repo.getListActivity("interacts,date_logged,preview_body", 20)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { data: ActivityResponse? ->
                    _activityData.postValue(Event(data!!))
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