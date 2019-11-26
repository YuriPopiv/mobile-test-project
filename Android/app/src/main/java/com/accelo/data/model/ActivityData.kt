package com.accelo.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.accelo.data.model.Staff
import com.accelo.data.model.Affiliation


/**
 * Created by Yuri Popiv on 11/25/2019.
 */

data class ActivityData(
    @SerializedName("affiliations")
    @Expose
    var affiliations: List<Affiliation>? = null,
    @SerializedName("staff")
    @Expose
    var staff: List<Staff>? = null,
    @SerializedName("threads")
    @Expose
    var threads: List<Thread>? = null
)

