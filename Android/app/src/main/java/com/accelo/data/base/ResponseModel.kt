package com.accelo.data.base

import com.accelo.data.model.Meta
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by dmytro on 12/2/19
 */
data class ResponseModel<T>(
    @SerializedName("response")
    @Expose
    var response: T? = null,

    @SerializedName("meta")
    @Expose
    var meta: Meta? = null
)