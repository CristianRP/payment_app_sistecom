package com.sistecom.paymentapp.data.model.order

import com.google.gson.annotations.SerializedName

/**
 * Created by: cristianramirez
 * On: 5/06/20 at: 12:59 PM
 *
 */

data class Order (
        @SerializedName("id") val id : Int,
        @SerializedName("alternId") val alternId : Int,
        @SerializedName("requestdate") val requestDate : String,
        @SerializedName("concept") val concept : String,
        @SerializedName("amount") val amount : Double,
        @SerializedName("status") val status : String
)