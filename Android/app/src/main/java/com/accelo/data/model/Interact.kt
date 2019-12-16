package com.accelo.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by dmytro on 12/2/19
 */
data class Interact(
    @SerializedName("id")
    @Expose
    var interactId: String,

    @SerializedName("type")
    @Expose
    var type: String,

    @SerializedName("email")
    @Expose
    var email: String,

    @SerializedName("owner_name")
    @Expose
    var ownerName: String,

    @SerializedName("date_actioned")
    @Expose
    var dateActioned: String,

    @SerializedName("owner_id")
    @Expose
    var ownerId: String,

    @SerializedName("owner_type")
    @Expose
    var ownerType: String
)