package com.accelo.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by dmytro on 12/2/19
 */
data class FullActivity(
    @SerializedName("id")
    @Expose
    var activityId: String,
    @SerializedName("subject")
    @Expose
    var subject: String,

    @SerializedName("confidential")
    @Expose
    var confidential: Int,

    @SerializedName("body")
    @Expose
    var body: String,

    @SerializedName("html_body")
    @Expose
    var htmlBody: String,

    @SerializedName("date_logged")
    @Expose
    var dateLogged: String,

    @SerializedName("interacts")
    @Expose
    var interacts: List<Interact>

)