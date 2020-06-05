package com.sistecom.paymentapp.utils

import com.sistecom.paymentapp.utils.Status.SUCCESS
import com.sistecom.paymentapp.utils.Status.ERROR
import com.sistecom.paymentapp.utils.Status.LOADING

/**
 * Created by: cristianramirez
 * On: 4/06/20 at: 03:11 PM
 *
 */

data class Resource<out T>(val status: Status, val data: T?, val message: String?) {
    companion object {
        fun <T> success(data: T): Resource<T> = Resource(status = SUCCESS, data = data, message = null)
        fun <T> error(data: T?, message: String?): Resource<T> =
                Resource(status = ERROR, data = data, message = message)
        fun <T> loading(data: T?): Resource<T> = Resource(status = LOADING, data = data, message = null)
    }
}