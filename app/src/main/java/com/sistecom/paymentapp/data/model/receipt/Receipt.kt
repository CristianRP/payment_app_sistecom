package com.sistecom.paymentapp.data.model.receipt

import com.google.gson.annotations.SerializedName

/**
 * Created by: cristianramirez
 * On: 5/06/20 at: 08:29 PM
 *
 */

data class Receipt (
        @SerializedName("id") val id: Int,
        @SerializedName("systemId") val systemId: String,
        @SerializedName("transactiondate") val transactionDate: String,
        @SerializedName("transactiontime") val transactionTime: String,
        @SerializedName("transactionnumber") val transactionNumber: String,
        @SerializedName("cardtype") val cardType: String,
        @SerializedName("cardnumber") val cardNumber: Int,
        @SerializedName("amount") val amount: Double,
        @SerializedName("cardName") val cardName: String,
        @SerializedName("customer_id") val customerId: String,
        @SerializedName("customerName") val customerName: String
)