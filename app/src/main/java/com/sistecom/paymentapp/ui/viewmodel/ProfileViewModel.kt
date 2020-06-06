package com.sistecom.paymentapp.ui.viewmodel

import android.view.View
import android.widget.Toast
import androidx.lifecycle.*
import com.sistecom.paymentapp.data.model.user.Customer
import com.sistecom.paymentapp.data.repository.MainRepository
import com.sistecom.paymentapp.utils.Resource
import com.sistecom.paymentapp.utils.Status
import kotlinx.coroutines.Dispatchers
import java.lang.Exception

class ProfileViewModel(private val mainRepository: MainRepository) : ViewModel() {

    //var customer: LiveData<Customer>? = LiveData<Customer>()
    val customer: MutableLiveData<Customer> by lazy {
        MutableLiveData<Customer>()
    }

    fun getCustomerProfile() = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data= mainRepository.getCustomerProfile()))
        } catch (exception: Exception) {
            emit(Resource.error(data= null, message = exception.message ?: "An error has ocurred"))
        }
    }

    fun getCustomerProfileByUser() = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data= mainRepository.getCustomerProfileByUser()))
        } catch (exception: Exception) {
            emit(Resource.error(data= null, message = exception.message ?: "An error has ocurred"))
        }
    }

}