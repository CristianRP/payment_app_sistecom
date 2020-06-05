package com.sistecom.paymentapp.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sistecom.paymentapp.data.api.SistecomApiHelper
import com.sistecom.paymentapp.data.repository.MainRepository
import com.sistecom.paymentapp.ui.viewmodel.ContractByCustomerViewModel
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
        throw IllegalArgumentException("Unknown class name")
    }
}