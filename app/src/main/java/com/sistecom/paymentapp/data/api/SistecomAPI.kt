package com.sistecom.paymentapp.data.api

import com.sistecom.paymentapp.data.model.ContractByCustomerResponse
import com.sistecom.paymentapp.data.model.order.OrderByContractResponse
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by: cristianramirez
 * On: 4/06/20 at: 05:32 PM
 *
 */

interface SistecomAPI {

    @GET("api/contract/index")
    suspend fun getContractsByCustomer(@Query("pCustomerId") customerId: Int):
            ContractByCustomerResponse

    @GET("api/order/index")
    suspend fun getOrdersByContract(@Query("pContractId") contractId: Int):
            OrderByContractResponse


}