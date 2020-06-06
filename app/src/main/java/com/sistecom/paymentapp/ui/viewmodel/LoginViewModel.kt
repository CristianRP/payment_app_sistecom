package com.sistecom.paymentapp.ui.viewmodel

import android.app.Application
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoDevice
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationContinuation
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationDetails
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.ChallengeContinuation
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.MultiFactorAuthenticationContinuation
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.AuthenticationHandler
import com.amazonaws.services.cognitoidentityprovider.model.*
import com.sistecom.paymentapp.data.model.User
import com.sistecom.paymentapp.security.CognitoSettings
import com.sistecom.paymentapp.utils.AuthenticationError
import com.sistecom.paymentapp.utils.PrefManagerHelper
import com.sistecom.paymentapp.utils.PrefManagerHelper.CUSTOMER_ID
import com.sistecom.paymentapp.utils.PrefManagerHelper.USER_ID
import java.lang.Exception

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val context = getApplication<Application>().applicationContext
    private val cognitoSettings by lazy { CognitoSettings(context= context) }
    var user: User? = User()
    val loginSuccess: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }
    val visibility: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }

    fun signInWithCognito() {
        Log.e("LOGIN_VM", "sigin session init")
        visibility.value = View.VISIBLE
        val cognitoUser = cognitoSettings.getUserPool()
                .getUser(user?.userName)

        cognitoUser.getSessionInBackground(authenticationHandler)
    }

    private var authenticationHandler: AuthenticationHandler = object : AuthenticationHandler {
        override fun onSuccess(userSession: CognitoUserSession?, newDevice: CognitoDevice?) {
            loginSuccess.value = true
            visibility.value = View.GONE
            //userSession?.username
            Log.e("LOGIN_VM_ONSUCCESS", "Inicio completo: $user")
        }

        override fun getAuthenticationDetails(authenticationContinuation: AuthenticationContinuation?, userId: String?) {
            visibility.value = View.GONE
            val authenticationDetails = AuthenticationDetails(user?.userName, user?.password, null)
            authenticationContinuation?.setAuthenticationDetails(authenticationDetails)
            authenticationContinuation?.continueTask()
        }

        override fun getMFACode(continuation: MultiFactorAuthenticationContinuation?) {
            TODO("Not yet implemented")
        }

        override fun authenticationChallenge(continuation: ChallengeContinuation?) {
            TODO("Not yet implemented")
        }

        override fun onFailure(exception: Exception?) {
            visibility.value = View.GONE
            loginSuccess.value = false
            Toast.makeText(context, exception?.let { toAuthError(it) }, Toast.LENGTH_LONG).show()
        }

    }

    private fun toAuthError(exception: Exception): String {
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
