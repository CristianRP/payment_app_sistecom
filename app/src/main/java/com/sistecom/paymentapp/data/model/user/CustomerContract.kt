package com.sistecom.paymentapp.data.model.user

import com.google.gson.annotations.SerializedName

/**
 * Created by: cristianramirez
 * On: 5/06/20 at: 11:22 PM
 *
 */

data class CustomerContract (
        @SerializedName("id") val id: Int,
        @SerializedName("alternId") val alternId: String,
        @SerializedName("requestDate") val requestDate: String,
        @SerializedName("cancelDate") val cancelDate: String,
        @SerializedName("status") val status: String,
        @SerializedName("notes") val notes: String
)