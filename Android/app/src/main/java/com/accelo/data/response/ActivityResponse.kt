package com.accelo.data.response

import com.accelo.data.model.ActivityData
import com.accelo.data.model.Meta
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


/**
 * Created by Yuri Popiv on 11/25/2019.
 */
data class ActivityResponse(
    @SerializedName("response")
    @Expose
    var response: ActivityData? = null,
    @SerializedName("meta")
    @Expose
    var meta: Meta? = null
)