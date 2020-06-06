package com.sistecom.paymentapp.data.api

import com.sistecom.paymentapp.data.api.RetrofitBuilder.apiService

/**
 * Created by: cristianramirez
 * On: 4/06/20 at: 06:09 PM
 *
 */

class SistecomApiHelper(private val apiService: SistecomAPI, private val contractId: Int? = 0) {
    suspend fun getContractsByCustomer() = apiService.getContractsByCustomer(1)
    suspend fun getOrdersByContract() = contractId?.let { apiService.getOrdersByContract(it) }
}