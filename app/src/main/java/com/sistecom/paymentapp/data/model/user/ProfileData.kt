package com.sistecom.paymentapp.data.model.user

import com.google.gson.annotations.SerializedName

/**
 * Created by: cristianramirez
 * On: 5/06/20 at: 11:24 PM
 *
 */

data class ProfileData (
        @SerializedName("customer") val customer: Customer,
        @SerializedName("contract") val contract: List<CustomerContract>
)