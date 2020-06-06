package com.sistecom.paymentapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.sistecom.paymentapp.data.repository.MainRepository
import com.sistecom.paymentapp.utils.Resource
import kotlinx.coroutines.Dispatchers
import java.lang.Exception

class ReceiptsViewModel(private val mainRepository: MainRepository) : ViewModel() {

    fun getReceiptsByCustomer() = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data= mainRepository.getReceiptsByCustomer()))
        } catch (exception: Exception) {
            emit(Resource.error(data= null, message = exception.message ?: "An error has ocurred"))
        }
    }

}