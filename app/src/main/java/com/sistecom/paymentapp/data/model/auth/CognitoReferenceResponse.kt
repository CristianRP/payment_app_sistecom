package com.sistecom.paymentapp.data.model.auth

import com.google.gson.annotations.SerializedName

/**
 * Created by: cristianramirez
 * On: 6/06/20 at: 10:32 AM
 *
 */

data class CognitoReferenceResponse (
        @SerializedName("success") val success: Boolean,
        @SerializedName("data") val reference: CognitoReference
)