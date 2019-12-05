package com.accelo.ui.stream

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.accelo.data.AcceloRepository
import com.accelo.data.model.ActivityData
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


    val activityData: LiveData<Event<ActivityData>> get() = _activityData
    private val _activityData = MutableLiveData<Event<ActivityData>>()

    val snackbarMessage: LiveData<Event<String>> get() = _snackbarMessage
    private val _snackbarMessage = MutableLiveData<Event<String>>()

    val isLoading: LiveData<Boolean> get() = _isLoading
    private val _isLoading = MutableLiveData(true)

    val isRefreshing: LiveData<Boolean> get() = _isRefreshing
    private val _isRefreshing = MutableLiveData(false)

    private val _isEmpty = MediatorLiveData<Boolean>()
    val isEmpty: LiveData<Boolean> = _isEmpty

    fun getActivities() {

        subscription.add(repo.getListActivity("interacts,date_logged,preview_body", 20)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { _isLoading.postValue(true) }
            .doFinally { _isLoading.postValue(false) }
            .subscribe(
                { data: ActivityData? ->
                    _activityData.postValue(Event(data!!))
                }, { t ->
                    Timber.e(t)
                    _snackbarMessage.postValue(Event(t.localizedMessage))
                })
        )

    }

    fun delete(id: String) {

        subscription.add(repo.delete(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { _isLoading.postValue(true) }
            .doFinally { _isLoading.postValue(false) }
            .subscribe(
                {
                    Timber.e(it.toString())
                }, { t ->
                })
        )

    }

    fun onSwipeRefresh(){
        subscription.add(repo.getListActivity("interacts,date_logged,preview_body", 20)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { _isRefreshing.postValue(true) }
            .doFinally { _isRefreshing.postValue(false) }
            .subscribe(
                { data: ActivityData? ->
                    if (data == null || data.threads.isNullOrEmpty()){
                        _isEmpty.postValue(true)
                    }else{
                        _isEmpty.postValue(false)
                        _activityData.postValue(Event(data))
                    }
                }, { t ->
                    Timber.e(t)
                    _snackbarMessage.postValue(Event(t.localizedMessage))
                })
        )
    }

    fun search(query: String){
        subscription.add(repo.search("interacts,date_logged,preview_body", query)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { _isLoading.postValue(true) }
            .doFinally { _isLoading.postValue(false) }
            .subscribe(
                { data: ActivityData? ->
                    if (data == null || data.threads.isNullOrEmpty()){
                        _isEmpty.postValue(true)
                    }else{
                        _isEmpty.postValue(false)
                        _activityData.postValue(Event(data))
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