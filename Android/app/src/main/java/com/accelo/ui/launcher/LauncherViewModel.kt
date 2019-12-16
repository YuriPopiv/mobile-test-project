package com.accelo.ui.launcher

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.accelo.data.LocalDataSource
import com.accelo.util.Event
import com.accelo.util.Result
import javax.inject.Inject

/**
 * Created by Yuri Popiv on 11/26/2019.
 */
class LauncherViewModel @Inject constructor(
    private val localDataSource: LocalDataSource
) : ViewModel() {

    private val sessionActive = MutableLiveData<Result<Boolean>>()

    val launchDestination: LiveData<Event<Destination>>

    init {
        sessionActive.postValue(Result.Success(localDataSource.getToken().isNotEmpty()))

        launchDestination = Transformations.map(sessionActive) {
            if ((it as? Result.Success)?.data == false) {
                Event(Destination.AUTH_ACTIVITY)
            } else
                Event(Destination.STREAM)
        }

    }
}

enum class Destination {
    AUTH_ACTIVITY,
    STREAM
}