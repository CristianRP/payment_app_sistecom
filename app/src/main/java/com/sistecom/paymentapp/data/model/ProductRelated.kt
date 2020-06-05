package com.sistecom.paymentapp.data.model

import com.google.gson.annotations.SerializedName

/**
 * Created by: cristianramirez
 * On: 4/06/20 at: 05:25 PM
 *
 */

data class ProductRelated (
        @SerializedName("id") val id : Int,
        @SerializedName("product_id") val productId : Int,
        @SerializedName("description") val description : String
)