package com.sistecom.paymentapp.data.model.order

import com.google.gson.annotations.SerializedName

/**
 * Created by: cristianramirez
 * On: 5/06/20 at: 01:00 PM
 *
 */

data class OrderByContractResponse (
        @SerializedName("success") val success : Boolean,
        @SerializedName("data") val data : List<Order>,
        @SerializedName("status") val status : Int
)