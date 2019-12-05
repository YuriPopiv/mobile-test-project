package com.accelo.ui.create

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.accelo.data.AcceloRepository
import com.accelo.data.model.CreatePostData
import com.accelo.util.Event
import com.accelo.util.NetworkUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.net.SocketTimeoutException
import javax.inject.Inject

/**
 * Created by Yuri Popiv on 11/26/2019.
 */
class CreateViewModel @Inject constructor(
    private val repo: AcceloRepository,
    private val networkUtils: NetworkUtils
) : ViewModel() {

    private val subscription: CompositeDisposable = CompositeDisposable()

    val activityData: LiveData<Event<CreatePostData>> get() = _activityData
    private val _activityData = MutableLiveData<Event<CreatePostData>>()

    val errorMessage: LiveData<Event<String>> get() = _errorMessage
    private val _errorMessage = MutableLiveData<Event<String>>()

    private val _navigateToNetworkErrorDialogAction = MutableLiveData<Event<Unit>>()
    val navigateToNetworkErrorDialogAction: LiveData<Event<Unit>>
        get() = _navigateToNetworkErrorDialogAction

    val isLoading: LiveData<Boolean> get() = _isLoading
    private val _isLoading = MutableLiveData(true)


    fun createActivity(body: String, subject: String) {
        //TODO replace ! (was added for faster reproducing connectivity issue)
        if (!networkUtils.hasNetworkConnection()) {

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
                        _errorMessage.postValue(Event(t.localizedMessage))

                    })
            )
        }else{
            Timber.e("No network connection")
            _navigateToNetworkErrorDialogAction.value = Event(Unit)
        }

    }

    fun saveNotDeliveredActivitiesToDB(body: String, subject: String){
        repo.saveActivityForFutureDelivery(body, subject)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {  }
    }


    override fun onCleared() {
        super.onCleared()
        subscription.clear()
    }
}