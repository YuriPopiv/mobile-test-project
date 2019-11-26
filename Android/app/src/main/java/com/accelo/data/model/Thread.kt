package com.accelo.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


/**
 * Created by Yuri Popiv on 11/25/2019.
 */
data class Thread(
    @SerializedName("interacts")
    @Expose
    var interacts: List<Owner>? = null,
    @SerializedName("total_activities")
    @Expose
    var totalActivities: String? = null,
    @SerializedName("event_text")
    @Expose
    var eventText: String? = null,
    @SerializedName("activities")
    @Expose
    var activities: List<Activity>? = null,
    @SerializedName("id")
    @Expose
    var id: String? = null
)