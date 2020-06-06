package com.sistecom.paymentapp.data.model.auth

import com.google.gson.annotations.SerializedName

/**
 * Created by: cristianramirez
 * On: 6/06/20 at: 10:33 AM
 *
 */

data class CognitoReference (
        @SerializedName("type") val type: String,
        @SerializedName("message") val message: String
)