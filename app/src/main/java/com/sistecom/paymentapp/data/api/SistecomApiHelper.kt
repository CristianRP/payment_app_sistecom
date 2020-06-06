package com.sistecom.paymentapp.data.api

import com.sistecom.paymentapp.data.api.RetrofitBuilder.apiService

/**
 * Created by: cristianramirez
 * On: 4/06/20 at: 06:09 PM
 *
 */

class SistecomApiHelper(private val apiService: SistecomAPI, private val contractId: Int? = 0,
                        private val customerId: Int? = 0, private val alternId: String? = "",
                        private var userId: String? = "") {
    suspend fun getContractsByCustomer() = apiService.getContractsByCustomer(1)
    suspend fun getOrdersByContract() = apiService.getOrdersByContract(1)
    suspend fun getReceiptsByCustomer() = apiService.getReceiptsByCustomer(1)
    suspend fun getCustomerProfile() = customerId?.let { apiService.getCustomerProfile(it) }
    suspend fun getCustomerProfileByUser() = userId?.let { apiService.getCustomerProfileByUser(it) }
    suspend fun addCognitoReferenceToCustomer() = alternId?.let { userId?.let { it1 -> apiService.addCognitoReferenceToCustomer(it, it1) } }
    suspend fun getPendingOrdersByCustomer() = customerId?.let { apiService.getPendingOrders(it) }
}