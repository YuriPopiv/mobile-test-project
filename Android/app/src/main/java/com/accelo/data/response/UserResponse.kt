package com.accelo.data.response

import com.accelo.data.model.AccountDetails
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


/**
 * Created by Yuri Popiv on 11/25/2019.
 */
data class UserResponse(
    @SerializedName("account_id")
    @Expose
    var accountId: String? = null,
    @SerializedName("account_details")
    @Expose
    var accountDetails: AccountDetails? = null,
    @SerializedName("access_token")
    @Expose
    var accessToken: String? = null,
    @SerializedName("deployment")
    @Expose
    var deployment: String? = null,
    @SerializedName("refresh_token")
    @Expose
    var refreshToken: String? = null,
    @SerializedName("deployment_name")
    @Expose
    var deploymentName: String? = null,
    @SerializedName("deployment_uri")
    @Expose
    var deploymentUri: String? = null,
    @SerializedName("expires_in")
    @Expose
    var expiresIn: Int? = null,
    @SerializedName("token_type")
    @Expose
    var tokenType: String? = null
)