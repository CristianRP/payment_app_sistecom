package com.sistecom.paymentapp.data.model

import com.google.gson.annotations.SerializedName

/**
 * Created by: cristianramirez
 * On: 4/06/20 at: 05:27 PM
 *
 */

data class DataNode (
        @SerializedName("contracts") val contract : Contract,
        @SerializedName("detail") val detail : List<DetailNode>
)