package com.accelo.ui.stream

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.accelo.AcceloRepository
import com.accelo.data.response.ActivityResponse
import com.accelo.util.Event
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
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

    val snackbarMessage: LiveData<String> get() = _snackbarMessage
    private val _snackbarMessage = MutableLiveData<String>()

    fun getActivities() {

        subscription.add(repo.getListActivity("interacts,date_logged,preview_body", 20)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { data: ActivityResponse? ->
                    _activityData.postValue(Event(data!!))
                }, { t ->
                    _snackbarMessage.postValue(t.localizedMessage)
                    t.printStackTrace()
                })
        )

    }

    override fun onCleared() {
        super.onCleared()
        subscription.clear()
    }
}