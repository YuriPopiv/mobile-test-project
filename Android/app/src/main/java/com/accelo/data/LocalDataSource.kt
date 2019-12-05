package com.accelo.data

import android.content.SharedPreferences
import androidx.core.content.edit
import javax.inject.Inject

/**
 * Created by Yuri Popiv on 11/26/2019.
 */
class LocalDataSource @Inject constructor(private val prefs: SharedPreferences) {

    fun saveToken(token: String) = prefs.edit { putString(KEY_USER_TOKEN, token) }

    fun getToken(): String = prefs.getString(KEY_USER_TOKEN, "") ?: ""

    fun saveDeploymentName(deploymentName: String) = prefs.edit { putString(KEY_DEPLOYMENT, deploymentName) }

    fun getDeploymentName(): String = prefs.getString(KEY_DEPLOYMENT, "") ?: ""

    fun saveNotDeliveredActivitiesState(state: Boolean) = prefs.edit { putBoolean(KEY_ACTIVITIES, false) }

    fun hasNotDeliveredActivities(): Boolean = prefs.getBoolean(KEY_ACTIVITIES, false)

    fun clearData() {
        prefs.edit { KEY_USER_TOKEN to "" }
        prefs.edit { KEY_DEPLOYMENT to "" }
        prefs.edit { KEY_ACTIVITIES to false }
    }

    companion object {
        const val KEY_USER_TOKEN = "KEY_USER_TOKEN"
        const val KEY_DEPLOYMENT = "KEY_DEPLOYMENT"
        const val KEY_ACTIVITIES = "KEY_ACTIVITIES"
    }
}