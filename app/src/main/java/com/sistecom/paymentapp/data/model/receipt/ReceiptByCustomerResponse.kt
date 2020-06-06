package com.sistecom.paymentapp.data.model.receipt

import com.google.gson.annotations.SerializedName
import com.sistecom.paymentapp.data.model.order.Order

/**
 * Created by: cristianramirez
 * On: 5/06/20 at: 08:28 PM
 *
 */

data class ReceiptByCustomerResponse (
        @SerializedName("success") val success : Boolean,
        @SerializedName("data") val data : List<Receipt>,
        @SerializedName("status") val status : Int
)