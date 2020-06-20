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
    suspend fun getContractsByCustomer() = alternId?.let { apiService.getContractsByCustomer(it) }
    suspend fun getOrdersByContract() = contractId?.let { apiService.getOrdersByContract(it) }
    suspend fun getReceiptsByCustomer() = alternId?.let { apiService.getReceiptsByCustomer(it) }
    suspend fun getCustomerProfile() = customerId?.let { apiService.getCustomerProfile(it) }
    suspend fun getCustomerProfileByUser() = userId?.let { apiService.getCustomerProfileByUser(it) }
    suspend fun addCognitoReferenceToCustomer() = alternId?.let { userId?.let { it1 -> apiService.addCognitoReferenceToCustomer(it, it1) } }
    suspend fun getPendingOrdersByCustomer() = alternId?.let { apiService.getPendingOrders(it) }
}