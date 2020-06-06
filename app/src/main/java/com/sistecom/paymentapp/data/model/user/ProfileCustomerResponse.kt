package com.sistecom.paymentapp.data.model.user

import com.google.gson.annotations.SerializedName
import com.sistecom.paymentapp.data.model.receipt.Receipt

/**
 * Created by: cristianramirez
 * On: 5/06/20 at: 11:16 PM
 *
 */

data class ProfileCustomerResponse (
        @SerializedName("success") val success : Boolean,
        @SerializedName("data") val data : List<ProfileData>,
        @SerializedName("status") val status : Int
)