package com.sistecom.paymentapp.security

import android.content.Context
import com.amazonaws.regions.Region
import com.amazonaws.regions.Regions

/**
 * Created by: cristianramirez
 * On: 24/05/20 at: 09:46 PM
 *
 */

data class CognitoSettings(
        val userPoolId: String = "us-east-1_tdXCBcP6B",
        val clientId: String = "5df6fn8e3cq5le1l16m46cfhsc",
        val clientSecret: String = "mcdh49cc804ibsboqf7h2ld0lt6b9ed7kpnbks4ql64drh0h028",
        val cognitoRegion: Regions = Regions.US_EAST_1,
        var context: Context
) {
    constructor(context: Context) : this( "us-east-1_tdXCBcP6B", "5df6fn8e3cq5le1l16m46cfhsc", "mcdh49cc804ibsboqf7h2ld0lt6b9ed7kpnbks4ql64drh0h028", Regions.US_EAST_1,  context)
}