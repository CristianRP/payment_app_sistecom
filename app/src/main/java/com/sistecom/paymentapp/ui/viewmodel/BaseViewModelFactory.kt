package com.sistecom.paymentapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * Created by: cristianramirez
 * On: 3/06/20 at: 02:36 AM
 *
 */

class BaseViewModelFactory<T> (val creator: () -> T) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return creator() as T
    }
}