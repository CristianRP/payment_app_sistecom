package com.sistecom.paymentapp.data.model.user

import com.google.gson.annotations.SerializedName

/**
 * Created by: cristianramirez
 * On: 5/06/20 at: 11:17 PM
 *
 */

data class Customer (
    @SerializedName("id") val id: Int? = 0,
    @SerializedName("alternId") val alternId: String? = "",
    @SerializedName("fisrtName") val firstName: String? = "",
    @SerializedName("lastName") val lastName: String? = "",
    @SerializedName("birthDate") val birthDate: String? = "",
    @SerializedName("documentTypeId") val documentTypeId: String? = "",
    @SerializedName("documentID") val documentId: String? = "",
    @SerializedName("taxId") val taxId: String? = "",
    @SerializedName("phoneNumer") val phoneNumber: String? = "",
    @SerializedName("userId") val userId: String? = "",
    @SerializedName("email") val email:String? = "",
    @SerializedName("status") val status: String? = ""

)