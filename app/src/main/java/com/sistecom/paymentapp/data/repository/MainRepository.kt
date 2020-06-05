package com.sistecom.paymentapp.data.repository

import com.sistecom.paymentapp.data.api.SistecomApiHelper

/**
 * Created by: cristianramirez
 * On: 4/06/20 at: 06:11 PM
 *
 */

class MainRepository(private val apiHelper: SistecomApiHelper) {
    suspend fun getContractsByCustomer() = apiHelper.getContractsByCustomer()
}