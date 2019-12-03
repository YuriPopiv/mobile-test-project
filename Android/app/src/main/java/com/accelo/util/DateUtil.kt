package com.accelo.util

import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by dmytro on 12/2/19
 */
object DateUtil {

    private const val TIME_HALF_AN_HOUR = 1800000L
    private const val TIME_TODAY = 86400000L

    private const val NOW = "Now"
    private const val TODAY = "Today"

    fun getTimeFromTimeStamp(time: Long): String {
        val loggedTime = time * 1000
        val currentTime = Calendar.getInstance().time.time
        return when {
            currentTime - loggedTime <= TIME_HALF_AN_HOUR -> NOW
            currentTime - loggedTime in (TIME_HALF_AN_HOUR + 1)..TIME_TODAY -> TODAY
            else -> {
                val format = SimpleDateFormat("dd MMM yyy", Locale.getDefault())
                format.format(Date(loggedTime))
            }
        }
    }
}