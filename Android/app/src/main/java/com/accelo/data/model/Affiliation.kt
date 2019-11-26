package com.accelo.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Yuri Popiv on 11/25/2019.
 */
data class Affiliation(
    @SerializedName("mobile")
    @Expose
    var mobile: String? = null,
    @SerializedName("id")
    @Expose
    var id: String? = null,
    @SerializedName("email")
    @Expose
    var email: String? = null
)