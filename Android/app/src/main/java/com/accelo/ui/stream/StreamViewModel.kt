package com.accelo.ui.stream

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.accelo.data.AcceloRepository
import com.accelo.data.base.BaseViewModel
import com.accelo.data.model.ActivityData
import com.accelo.util.Event
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.processors.PublishProcessor
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Created by Yuri Popiv on 11/26/2019.
 */
class StreamViewModel @Inject constructor(
    private val repository: AcceloRepository
) : BaseViewModel() {

    val activityData: LiveData<Event<ActivityData>> get() = _activityData
    private val _activityData = MutableLiveData<Event<ActivityData>>()

    val refreshData: LiveData<Event<ActivityData>> get() = _refreshData
    private val _refreshData = MutableLiveData<Event<ActivityData>>()

    val searchData: LiveData<Event<ActivityData>> get() = _searchData
    private val _searchData = MutableLiveData<Event<ActivityData>>()

    val snackbarMessage: LiveData<Event<String>> get() = _snackbarMessage
    private val _snackbarMessage = MutableLiveData<Event<String>>()

    val isLoading: LiveData<Boolean> get() = _isLoading
    private val _isLoading = MutableLiveData(true)

    val isRefreshing: LiveData<Boolean> get() = _isRefreshing
    private val _isRefreshing = MutableLiveData(false)

    private val _isEmpty = MediatorLiveData<Boolean>()
    val isEmpty: LiveData<Boolean> = _isEmpty

    private val requestProcessor: PublishProcessor<String> = PublishProcessor.create()
    private lateinit var debounceDisposable: Disposable

    fun getActivities(page: Int) {
        if (page == PAGE_START) {
            addSubscription(repository.getListActivity(page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { _isLoading.postValue(true) }
                .doFinally { _isLoading.postValue(false) }
                .subscribe(this::onSuccessGetActivities, this::onErrorGetActivities)
            )
        } else {
            addSubscription(
                repository.getListActivity(page)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::onSuccessGetActivities, this::onErrorGetActivities)
            )
        }
    }

    private fun onSuccessGetActivities(activityData: ActivityData?) {
        Timber.d("OnSuccessGetActivity")
        _activityData.postValue(Event(activityData!!))
    }

    private fun onErrorGetActivities(throwable: Throwable) {
        Timber.e(throwable)
        _snackbarMessage.postValue(Event(throwable.localizedMessage))
    }

    fun delete(id: String) {

        addSubscription(repository.delete(id)
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

    fun refresh() {
        addSubscription(repository.getListActivity(PAGE_START)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { _isRefreshing.postValue(true) }
            .doFinally { _isRefreshing.postValue(false) }
            .subscribe(this::onSuccessSwipeRefresh, this::onErrorSwipeRefresh)
        )
    }

    private fun onSuccessSwipeRefresh(activityData: ActivityData?) {
        Timber.d("onSuccessSwipeRefresh")
        if (activityData == null || activityData.threads.isNullOrEmpty()) {
            _isEmpty.postValue(true)
        } else {
            _isEmpty.postValue(false)
            _refreshData.postValue(Event(activityData))
        }
    }

    private fun onErrorSwipeRefresh(throwable: Throwable) {
        Timber.e(throwable)
        _snackbarMessage.postValue(Event(throwable.localizedMessage))
    }

    fun search(query: String) {
        if (!::debounceDisposable.isInitialized) {
            debounceDisposable = requestProcessor
                .throttleLast(REQUEST_DELAY, TimeUnit.MILLISECONDS)
                .flatMap {
                    repository.search(it)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .toFlowable()
                }
                .subscribe(this::onSuccessSearch, this::onErrorSearch)
                .apply {
                    addSubscription(this)
                }
        }

        requestProcessor.onNext(query)
    }

    private fun onSuccessSearch(activityData: ActivityData?) {
        Timber.d("onSuccessSearch")
        if (activityData == null || activityData.threads.isNullOrEmpty()) {
            _isEmpty.postValue(true)
        } else {
            _isEmpty.postValue(false)
            _searchData.postValue(Event(activityData))
        }
    }

    private fun onErrorSearch(throwable: Throwable) {
        Timber.e(throwable)
        _snackbarMessage.postValue(Event(throwable.localizedMessage))
    }

    companion object {
        private const val REQUEST_DELAY = 500L
    }
}