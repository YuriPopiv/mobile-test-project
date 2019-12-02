package com.accelo.util

import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by dmytro on 12/2/19
 */
object DateUtil {

    fun getTimeFromTimeStamp(time: Long): String {
        val date = Date(time * 1000)
        val format = SimpleDateFormat("dd MMM yyy", Locale.getDefault())
        return format.format(date)
    }

}