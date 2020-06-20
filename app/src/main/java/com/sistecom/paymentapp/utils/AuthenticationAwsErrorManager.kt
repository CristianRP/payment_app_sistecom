package com.sistecom.paymentapp.utils

import com.amazonaws.services.cognitoidentityprovider.model.*
import java.lang.Exception

/**
 * Created by: cristianramirez
 * On: 15/06/20 at: 05:35 PM
 *
 */

object AuthenticationAwsErrorManager {
    fun toAuthError(exception: Exception): String {
        return when(exception) {
            is UserNotFoundException -> AuthenticationError.UserNotFound()
            is InvalidParameterException -> AuthenticationError.InvalidParameter()
            is NotAuthorizedException -> AuthenticationError.UserNotFound()
            is InvalidPasswordException -> AuthenticationError.InvalidPassword()
            is InvalidLambdaResponseException -> AuthenticationError.InvalidResponse()
            is LimitExceededException -> AuthenticationError.LimitExceeded()
            is UsernameExistsException -> AuthenticationError.UserNameExists()
            is UserNotConfirmedException -> AuthenticationError.UserNotConfirmed()
            else -> AuthenticationError.UnknownError()
        }
    }
}