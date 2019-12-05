package com.accelo.data

import android.content.SharedPreferences
import androidx.core.content.edit
import javax.inject.Inject

/**
 * Created by Yuri Popiv on 11/26/2019.
 */
class LocalDataSource @Inject constructor(private val prefs: SharedPreferences) {

    private var _accessToken: String? = prefs.getString(KEY_USER_TOKEN, null)

    var accessToken: String? = _accessToken
        set(value) {
            prefs.edit { putString(KEY_USER_TOKEN, value) }
            field = value
        }

    private var _deployment: String? = prefs.getString(KEY_DEPLOYMENT, null)

    var deployment: String? = _deployment
        set(value) {
            prefs.edit { putString(KEY_DEPLOYMENT, value) }
            field = value
        }

    private var _hasNotDeliveredActivities: Boolean = prefs.getBoolean(KEY_ACTIVITIES, false)

    var hasNotDeliveredActivities: Boolean = _hasNotDeliveredActivities
        set(value) {
            prefs.edit { putBoolean(KEY_ACTIVITIES, value) }
            field = value
        }

    fun clearData() {
        prefs.edit { KEY_USER_TOKEN to null }
        prefs.edit { KEY_DEPLOYMENT to null }
        accessToken = null
    }

    companion object {
        const val KEY_USER_TOKEN = "KEY_USER_TOKEN"
        const val KEY_DEPLOYMENT = "KEY_DEPLOYMENT"
        const val KEY_ACTIVITIES = "KEY_ACTIVITIES"
    }
}