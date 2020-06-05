package com.sistecom.paymentapp.data.model

/**
 * Created by: cristianramirez
 * On: 3/06/20 at: 03:02 AM
 *
 */

data class User(
        var email: String? = "",
        var name: String? = "",
        var middleName: String? = "",
        var userName: String? = "",
        var customerRefCode: String? = "",
        var password: String? = ""
)