package com.sistecom.paymentapp.ui.viewmodel

import android.app.Application
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.liveData
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserAttributes
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.SignUpHandler
import com.amazonaws.services.cognitoidentityprovider.model.*
import com.sistecom.paymentapp.data.api.RetrofitBuilder
import com.sistecom.paymentapp.data.api.SistecomAPI
import com.sistecom.paymentapp.data.api.SistecomApiHelper
import com.sistecom.paymentapp.data.model.User
import com.sistecom.paymentapp.data.model.auth.CognitoReferenceResponse
import com.sistecom.paymentapp.data.repository.MainRepository
import com.sistecom.paymentapp.security.CognitoSettings
import com.sistecom.paymentapp.utils.AuthenticationError
import com.sistecom.paymentapp.utils.Resource
import com.sistecom.paymentapp.utils.Status
import kotlinx.coroutines.Dispatchers
import kotlin.math.sign

class RegisterViewModel(application: Application) : AndroidViewModel(application) {

    private val context = getApplication<Application>().applicationContext
    private val cognitoSettings by lazy { CognitoSettings(context= context) }
    var user: User? = User()
    lateinit var cognitoUserAttributes: CognitoUserAttributes
    val visibility: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }
    lateinit var apiHelper: SistecomApiHelper
    lateinit var mainRepository: MainRepository
    val signUpSuccess: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    fun addCognitoReferenceToCustomer() = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data= mainRepository.addCognitoReferenceToCustomer()))
        } catch (exception: java.lang.Exception) {
            Log.e("REGISTER_VM_ERROR", exception.message)
            emit(Resource.error(data= null, message = exception.message ?: "An error has ocurred"))
        }
    }

    fun signUpInCognito() {
        visibility.value = View.VISIBLE
        cognitoUserAttributes= CognitoUserAttributes()
        cognitoUserAttributes.addAttribute("email", user?.email)
        cognitoUserAttributes.addAttribute("name", user?.name)
        cognitoUserAttributes.addAttribute("middle_name", user?.middleName)
        cognitoUserAttributes.addAttribute("preferred_username", user?.userName)
        cognitoUserAttributes.addAttribute("custom:cusomer_code", user?.customerRefCode)
        cognitoSettings.getUserPool().signUpInBackground(
                user?.userName,
                user?.password,
                cognitoUserAttributes,
                null,
                signUpCallback
        )
    }

    private var signUpCallback: SignUpHandler = object : SignUpHandler {
        override fun onSuccess(cognitoUser: CognitoUser, signUpResult: SignUpResult) {
            visibility.value = View.GONE
            signUpSuccess.value = true
            Log.i("VM_SIGNUP_CALLBACK", "Signup success is confirmed ${signUpResult.isUserConfirmed}")
            if (signUpResult.userConfirmed) {
                Log.i("VM_SIGNUP_CALLBACK", "Signup success TODO: Login")
            } else {
                Log.i("VM_SIGNUP_CALLBACK", "Signup success .. user not confirmed, verification code sent to: ${signUpResult.codeDeliveryDetails.destination}")
            }
        }
        override fun onFailure(exception: Exception) {
            visibility.value = View.GONE
            signUpSuccess.value = false
            Log.e("VM_SIGNUP_CALLBACK", "Sign up failure ${exception.javaClass}")
            Log.e("VM_SIGNUP_CALLBACK", "Sign up failure ${exception.message}")
            Toast.makeText(context, toAuthError(exception), Toast.LENGTH_LONG).show()
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

    private fun initApi(alternId: String, userId: String) {
        SistecomApiHelper(RetrofitBuilder.apiService, alternId = alternId, userId = userId)
        MainRepository(apiHelper)
    }

    private fun getResponse(cognitoResponse: CognitoReferenceResponse) {
        //Toast.makeText(context, "Cliente referenciado con éxito", Toast.LENGTH_SHORT).show()
        Log.d("REGISTER_VM_REFER", cognitoResponse.toString())
    }
}