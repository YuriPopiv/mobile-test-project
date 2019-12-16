package com.accelo.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Yuri Popiv on 12/2/2019.
 */
data class CreatePostData(
    @SerializedName("response")
    @Expose
    var subject: String? = null,

    @SerializedName("confidential")
    @Expose
    var confidential: Int? = null,

    @SerializedName("id")
    @Expose
    var id: String? = null
)