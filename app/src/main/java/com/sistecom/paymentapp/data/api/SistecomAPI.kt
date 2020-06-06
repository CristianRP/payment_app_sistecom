package com.sistecom.paymentapp.data.api

import com.sistecom.paymentapp.data.model.ContractByCustomerResponse
import com.sistecom.paymentapp.data.model.auth.CognitoReferenceResponse
import com.sistecom.paymentapp.data.model.order.OrderByContractResponse
import com.sistecom.paymentapp.data.model.receipt.ReceiptByCustomerResponse
import com.sistecom.paymentapp.data.model.user.ProfileCustomerResponse
import retrofit2.http.GET
import retrofit2.http.POST
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

    @GET("api/receipt/index")
    suspend fun getReceiptsByCustomer(@Query("pCustomerId") customerId: Int):
            ReceiptByCustomerResponse

    @GET("api/customer/profile")
    suspend fun getCustomerProfile(@Query("pCustomerId") customerId: Int):
            ProfileCustomerResponse

    @POST("api/customer/setup")
    suspend fun addCognitoReferenceToCustomer(@Query("alternId") alternId: String,
                                              @Query("userId") userId: String):
            CognitoReferenceResponse

    @GET("api/order/pendenting?pCustomerId=1")
    suspend fun getPendingOrders(@Query("pCustomerId") customerId: Int ):
        OrderByContractResponse

    @GET("api/customer/profile")
    suspend fun getCustomerProfileByUser(@Query("pUserId") userId: String):
            ProfileCustomerResponse

}