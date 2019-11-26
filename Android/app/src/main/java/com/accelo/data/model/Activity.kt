package com.accelo.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


/**
 * Created by Yuri Popiv on 11/25/2019.
 */
data class Activity(
    @SerializedName("preview_body")
    @Expose
    var previewBody: String? = null,
    @SerializedName("date_logged")
    @Expose
    var dateLogged: String? = null,
    @SerializedName("confidential")
    @Expose
    var confidential: Int? = null,
    @SerializedName("subject")
    @Expose
    var subject: String? = null,
    @SerializedName("interacts")
    @Expose
    var interacts: List<Owner>? = null,
    @SerializedName("id")
    @Expose
    var id: String? = null,
    @SerializedName("event_text")
    @Expose
    var eventText: String? = null

)