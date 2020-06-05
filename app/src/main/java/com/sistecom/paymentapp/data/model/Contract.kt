package com.sistecom.paymentapp.data.model

import com.google.gson.annotations.SerializedName


/**
 * Created by: cristianramirez
 * On: 4/06/20 at: 05:11 PM
 *
 */

data class Contract (
        @SerializedName("id") val id : Int,
        @SerializedName("alternId") val alternId : Int,
        @SerializedName("status") val status : String,
        @SerializedName("notes") val notes : String,
        @SerializedName("customer_id") val customerId : Int,
        @SerializedName("customerName") val customerName : String,
        @SerializedName("address") val address : String,
        @SerializedName("requestdate") val requestDate : String
)