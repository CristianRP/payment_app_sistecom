package com.sistecom.paymentapp.security

import android.content.Context
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool
import com.amazonaws.regions.Region
import com.amazonaws.regions.Regions

/**
 * Created by: cristianramirez
 * On: 24/05/20 at: 09:46 PM
 *
 */

data class CognitoSettings(
        val userPoolId: String = "us-east-1_HsaFjtAMk",
        val clientId: String = "6j4dm9ih6osg5t83lkkd533e3q",
        val clientSecret: String = "obe8f16ivh84o0vdtkde0r1gqo29d5glimipkhbl7cdcrej2lqo",
        val cognitoRegion: Regions = Regions.US_EAST_1,
        var context: Context
) {
    fun getUserPool(): CognitoUserPool {
        return CognitoUserPool(context, userPoolId, clientId, clientSecret, cognitoRegion)
    }
}