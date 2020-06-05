package com.sistecom.paymentapp.data.model

import com.google.gson.annotations.SerializedName

/**
 * Created by: cristianramirez
 * On: 4/06/20 at: 05:26 PM
 *
 */

data class DetailNode(
        @SerializedName("contract_detail") val contractDetail : ContractDetail,
        @SerializedName("product_related") val listProductRelated : ArrayList<ProductRelated>
)