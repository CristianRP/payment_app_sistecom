package com.sistecom.paymentapp.data.repository

import com.sistecom.paymentapp.data.api.SistecomApiHelper

/**
 * Created by: cristianramirez
 * On: 4/06/20 at: 06:11 PM
 *
 */

class MainRepository(private val apiHelper: SistecomApiHelper) {
    suspend fun getContractsByCustomer() = apiHelper.getContractsByCustomer()
    suspend fun getOrdersByContract() = apiHelper.getOrdersByContract()
    suspend fun getReceiptsByCustomer() = apiHelper.getReceiptsByCustomer()
    suspend fun getCustomerProfile() = apiHelper.getCustomerProfile()
    suspend fun getCustomerProfileByUser() = apiHelper.getCustomerProfileByUser()
    suspend fun addCognitoReferenceToCustomer() = apiHelper.addCognitoReferenceToCustomer()
    suspend fun getPendingOrdersByCustomer() = apiHelper.getPendingOrdersByCustomer()
}