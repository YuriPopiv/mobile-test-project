package com.accelo.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


/**
 * Created by Yuri Popiv on 11/25/2019.
 */
data class Meta(
    @SerializedName("message")
    @Expose
    var message: String? = null,
    @SerializedName("more_info")
    @Expose
    var moreInfo: String? = null,
    @SerializedName("status")
    @Expose
    var status: String? = null
)