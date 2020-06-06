package com.sistecom.paymentapp.ui.base

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sistecom.paymentapp.data.api.SistecomApiHelper
import com.sistecom.paymentapp.data.repository.MainRepository
import com.sistecom.paymentapp.ui.viewmodel.*
import java.lang.IllegalArgumentException

/**
 * Created by: cristianramirez
 * On: 4/06/20 at: 06:18 PM
 *
 */

class ViewModelFactory(private val apiHelper: SistecomApiHelper) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ContractByCustomerViewModel::class.java)) {
            return ContractByCustomerViewModel(MainRepository(apiHelper)) as T
        }
        if (modelClass.isAssignableFrom(OrdersByContractViewModel::class.java)) {
            return OrdersByContractViewModel(MainRepository(apiHelper)) as T
        }
        if (modelClass.isAssignableFrom(PendingOrdersViewModel::class.java)) {
            return PendingOrdersViewModel(MainRepository(apiHelper)) as T
        }
        if (modelClass.isAssignableFrom(ReceiptsViewModel::class.java)) {
            return ReceiptsViewModel(MainRepository(apiHelper)) as T
        }
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            return ProfileViewModel(MainRepository(apiHelper)) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}