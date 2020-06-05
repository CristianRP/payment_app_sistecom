package com.sistecom.paymentapp.data.model

import com.google.gson.annotations.SerializedName

/**
 * Created by: cristianramirez
 * On: 4/06/20 at: 05:29 PM
 *
 */

data class ContractByCustomerResponse (
        @SerializedName("success")
        val success : Boolean,
        @SerializedName("data")
        val data : List<DataNode>,
        @SerializedName("status")
        val status : Int
)