package com.accelo.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


/**
 * Created by Yuri Popiv on 11/25/2019.
 */
data class Staff(
    @SerializedName("id")
    @Expose
    var id: String? = null,
    @SerializedName("firstname")
    @Expose
    var firstname: String? = null,
    @SerializedName("surname")
    @Expose
    var surname: String? = null
)