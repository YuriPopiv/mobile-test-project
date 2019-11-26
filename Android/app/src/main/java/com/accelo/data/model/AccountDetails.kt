package com.accelo.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.util.*


/**
 * Created by Yuri Popiv on 11/25/2019.
 */
data class AccountDetails (

    @SerializedName("phone")
    @Expose
    var phone: String? = null,
    @SerializedName("username")
    @Expose
    var username: String? = null,
//    @SerializedName("user_defined_titles")
//    @Expose
//    var userDefinedTitles: UserDefinedTitles? = null,
    @SerializedName("id")
    @Expose
    var id: String? = null,
    @SerializedName("mobile")
    @Expose
    var mobile: Any? = null,
//    @SerializedName("locale")
//    @Expose
//    var locale: Locale? = null,
    @SerializedName("fax")
    @Expose
    var fax: Any? = null,
    @SerializedName("surname")
    @Expose
    var surname: String? = null,
    @SerializedName("email")
    @Expose
    var email: String? = null,
    @SerializedName("firstname")
    @Expose
    var firstname: String? = null,
    @SerializedName("title")
    @Expose
    var title: Any? = null,
    @SerializedName("access_level")
    @Expose
    var accessLevel: String? = null,
//    @SerializedName("user_access")
//    @Expose
//    var userAccess: UserAccess? = null,
    @SerializedName("position")
    @Expose
    var position: String? = null,
    @SerializedName("initials")
    @Expose
    var initials: String? = null
)