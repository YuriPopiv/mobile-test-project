package com.accelo.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.accelo.data.AcceloRepository
import javax.inject.Inject

/**
 * Created by Yuri Popiv on 11/26/2019.
 */
class AuthViewModel@Inject constructor(
    private val repository: AcceloRepository
) : ViewModel() {


    val snackbarMessage: LiveData<String> get() = _snackbarMessage
    private val _snackbarMessage = MutableLiveData<String>()

    fun saveDeploymentName(deployment: String) {

        repository.saveDeploymentName(deployment)

    }


}