package com.sistecom.paymentapp.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserAttributes
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.SignUpHandler
import com.amazonaws.services.cognitoidentityprovider.model.SignUpResult
import com.sistecom.paymentapp.data.model.User
import com.sistecom.paymentapp.security.CognitoSettings

class RegisterViewModel(application: Application) : AndroidViewModel(application) {

    private val context = getApplication<Application>().applicationContext
    private val cognitoSettings by lazy { CognitoSettings(context= context) }
    var user: User? = User()
    lateinit var cognitoUserAttributes: CognitoUserAttributes

    fun signUpInCognito() {
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

    var signUpCallback: SignUpHandler = object : SignUpHandler {
        override fun onSuccess(user: CognitoUser, signUpResult: SignUpResult) {
            Log.i("VM_SIGNUP_CALLBACK", "Signup success is confirmed ${signUpResult.isUserConfirmed}")
            if (signUpResult.userConfirmed) {
                Log.i("VM_SIGNUP_CALLBACK", "Signup success TODO: Login")
            } else {
                Log.i("VM_SIGNUP_CALLBACK", "Signup success .. user not confirmed, verification code sent to: ${signUpResult.codeDeliveryDetails.destination}")
            }

        }
        override fun onFailure(exception: Exception) {
            Log.e("VM_SIGNUP_CALLBACK", "Sign up failure ${exception.localizedMessage}")
        }
    }
}