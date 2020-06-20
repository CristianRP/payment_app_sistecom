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
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserDetails
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationContinuation
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationDetails
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.ChallengeContinuation
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.MultiFactorAuthenticationContinuation
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.AuthenticationHandler
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GenericHandler
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GetDetailsHandler
import com.amazonaws.services.cognitoidentityprovider.model.*
import com.google.gson.Gson
import com.sistecom.paymentapp.data.model.User
import com.sistecom.paymentapp.security.CognitoSettings
import com.sistecom.paymentapp.utils.AuthenticationAwsErrorManager
import com.sistecom.paymentapp.utils.AuthenticationError
import com.sistecom.paymentapp.utils.AuthenticationStatus
import com.sistecom.paymentapp.utils.PrefManagerHelper
import com.sistecom.paymentapp.utils.PrefManagerHelper.COGNITO_EMAIL
import com.sistecom.paymentapp.utils.PrefManagerHelper.COGNITO_MIDDLE_NAME
import com.sistecom.paymentapp.utils.PrefManagerHelper.COGNITO_NAME
import com.sistecom.paymentapp.utils.PrefManagerHelper.COGNITO_SUB
import com.sistecom.paymentapp.utils.PrefManagerHelper.CUSTOMER_ID
import com.sistecom.paymentapp.utils.PrefManagerHelper.USER_HAS_SESSION
import com.sistecom.paymentapp.utils.PrefManagerHelper.USER_ID
import java.lang.Exception

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val context = getApplication<Application>().applicationContext
    private val cognitoSettings by lazy { CognitoSettings(context= context) }
    private lateinit var cognitoUser: CognitoUser
    var user: User? = User()
    val loginSuccess: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }
    val visibility: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }

    val authenticationState = MutableLiveData<AuthenticationStatus>()

    init {
        if (checkPrefManager()!!) {
            authenticationState.value = AuthenticationStatus.AUTHENTICATED
        } else {
            authenticationState.value = AuthenticationStatus.UNAUTHENTICATED
        }
    }

    fun refuseAuthentication() {
        authenticationState.value = AuthenticationStatus.UNAUTHENTICATED
    }

    fun signInWithCognito() {
        Log.e("LOGIN_VM", "sigin session init")
        visibility.value = View.VISIBLE
        val cognitoUserPool = cognitoSettings.getUserPool()

        Log.e("TAG_LOG", "Pato ${user?.password}")
        cognitoUser = cognitoUserPool.getUser(user?.userName)
        cognitoUser.getSessionInBackground(authenticationHandler)
    }

    private var authenticationHandler: AuthenticationHandler = object : AuthenticationHandler {

        override fun onSuccess(userSession: CognitoUserSession?, newDevice: CognitoDevice?) {
            cognitoUser.getDetailsInBackground(authenticationDetails)
        }

        override fun getAuthenticationDetails(authenticationContinuation: AuthenticationContinuation?, userId: String?) {
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
            refuseAuthentication()
            Toast.makeText(context, exception?.let { AuthenticationAwsErrorManager.toAuthError(it) }, Toast.LENGTH_LONG).show()
        }
    }

    private var authenticationDetails: GetDetailsHandler = object : GetDetailsHandler {
        override fun onSuccess(cognitoUserDetails: CognitoUserDetails?) {
            loginSuccess.value = true
            visibility.value = View.GONE

            cognitoUserDetails?.attributes?.attributes?.get("preferred_username")?.let { PrefManagerHelper.write(USER_ID, it) }
            cognitoUserDetails?.attributes?.attributes?.get("custom:cusomer_code")?.let { PrefManagerHelper.write(CUSTOMER_ID, it) }
            cognitoUserDetails?.attributes?.attributes?.get("sub")?.let { PrefManagerHelper.write(COGNITO_SUB, it) }
            cognitoUserDetails?.attributes?.attributes?.get("name")?.let { PrefManagerHelper.write(COGNITO_NAME, it) }
            cognitoUserDetails?.attributes?.attributes?.get("middle_name")?.let { PrefManagerHelper.write(COGNITO_MIDDLE_NAME, it) }
            cognitoUserDetails?.attributes?.attributes?.get("email")?.let { PrefManagerHelper.write(COGNITO_EMAIL, it) }
            authenticationState.value = AuthenticationStatus.AUTHENTICATED
            PrefManagerHelper.write(USER_HAS_SESSION, authenticationState.value == AuthenticationStatus.AUTHENTICATED)

            Log.e("LOGIN_VM", "VISIBLE GONE")
        }

        override fun onFailure(exception: Exception?) {
            Log.e("LOGIN_ONFAILED_DETAIL", "SESSION completo: ${exception?.message}")
            visibility.value = View.GONE
            loginSuccess.value = false
            refuseAuthentication()
            Toast.makeText(context, exception?.let { AuthenticationAwsErrorManager.toAuthError(it) }, Toast.LENGTH_LONG).show()
        }
    }

    private fun checkPrefManager(): Boolean? {
        return PrefManagerHelper.read(USER_HAS_SESSION, false)
    }

    fun signOutUser() {
        val cognitoUserPool = cognitoSettings.getUserPool()

        Log.e("TAG_LOG", "Pato ${PrefManagerHelper.read(USER_ID, "user")}")
        cognitoUser = cognitoUserPool.getUser(PrefManagerHelper.read(USER_ID, "user"))
        refuseAuthentication()
        cognitoUser.signOut()
        PrefManagerHelper.clearAll()
    }
}
