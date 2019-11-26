package com.accelo.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Yuri Popiv on 11/25/2019.
 */
data class Owner(
    @SerializedName("email")
    @Expose
    var email: String? = null,
    @SerializedName("owner_id")
    @Expose
    var ownerId: String? = null,
    @SerializedName("owner_name")
    @Expose
    var ownerName: String? = null,
    @SerializedName("date_actioned")
    @Expose
    var dateActioned: String? = null,
    @SerializedName("type")
    @Expose
    var type: String? = null,
    @SerializedName("owner_type")
    @Expose
    var ownerType: String? = null,
    @SerializedName("id")
    @Expose
    var id: String? = null
)