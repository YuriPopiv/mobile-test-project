package com.accelo.ui.create

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.accelo.data.AcceloRepository
import com.accelo.data.base.BaseViewModel
import com.accelo.data.model.ActivityData
import com.accelo.data.model.CreatePostData
import com.accelo.util.Event
import com.accelo.util.NetworkUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Yuri Popiv on 11/26/2019.
 */
class CreateViewModel @Inject constructor(
    private val repository: AcceloRepository,
    private val networkUtils: NetworkUtils
) : BaseViewModel() {

    val activityData: LiveData<Event<CreatePostData>> get() = _activityData
    private val _activityData = MutableLiveData<Event<CreatePostData>>()

    val snackbarMessage: LiveData<Event<String>> get() = _snackbarMessage
    private val _snackbarMessage = MutableLiveData<Event<String>>()

    private val _navigateToNetworkErrorDialogAction = MutableLiveData<Event<Unit>>()
    val navigateToNetworkErrorDialogAction: LiveData<Event<Unit>>
        get() = _navigateToNetworkErrorDialogAction

    val isLoading: LiveData<Boolean> get() = _isLoading
    private val _isLoading = MutableLiveData(false)


    fun createActivity(body: String, subject: String) {
        if (networkUtils.hasNetworkConnection()) {
            addSubscription(repository.createActivity(body, subject)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { _isLoading.postValue(true) }
                .doFinally { _isLoading.postValue(false) }
                .subscribe(this::onSuccessCreateActivity, this::onErrorCreateActivity)
            )
        } else {
            Timber.e("No network connection")
            _navigateToNetworkErrorDialogAction.value = Event(Unit)
        }
    }

    private fun onSuccessCreateActivity(response: CreatePostData) {
        Timber.d("onSuccessCreateActivity: all activities sent")
        _activityData.postValue(Event(response))
    }

    private fun onErrorCreateActivity(throwable: Throwable){
        Timber.e(throwable)
        _snackbarMessage.postValue(Event(throwable.localizedMessage))
    }

    fun saveNotDeliveredActivitiesToDB(body: String, subject: String) {
        addSubscription(repository.saveActivityForFutureDelivery(body, subject)
            .doOnComplete {
                Timber.d("Not delivered activities saved to the DB")
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::onSuccessSaveNotDeliveredActivitiesToDb, this::onErrorSaveNotDeliveredActivitiesToDb)
        )
    }

    private fun onSuccessSaveNotDeliveredActivitiesToDb() {
        Timber.d("onSuccessSaveNotDeliveredActivitiesToDb")
    }

    private fun onErrorSaveNotDeliveredActivitiesToDb(throwable: Throwable) {
        Timber.e(throwable)
        _snackbarMessage.postValue(Event(throwable.localizedMessage))
    }

}