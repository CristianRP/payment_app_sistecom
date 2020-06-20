package com.sistecom.paymentapp.utils

import android.content.Context
import android.content.SharedPreferences

/**
 * Created by: cristianramirez
 * On: 6/06/20 at: 01:45 PM
 *
 */

object PrefManagerHelper {
    private lateinit var preferences: SharedPreferences
    private const val PREFS_NAME = "SISTECOM_PAY_PARAMS"

    const val USER_ID = "usuarioId"
    const val CUSTOMER_ID = "customerId"
    const val COGNITO_SUB = "sub"
    const val COGNITO_NAME = "name"
    const val COGNITO_MIDDLE_NAME = "middle_name"
    const val COGNITO_EMAIL = "email"
    const val USER_HAS_SESSION = "has_session"

    fun init(context: Context) {
        preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun read(key: String, value: String): String? {
        return preferences.getString(key, value)
    }

    fun read(key: String, value: Long): Long? {
        return preferences.getLong(key, value)
    }

    fun read(key: String, value: Boolean): Boolean? {
        return preferences.getBoolean(key, value)
    }

    fun write(key: String, value: String) {
        val prefsEditor: SharedPreferences.Editor = preferences.edit()
        with(prefsEditor) {
            putString(key, value)
            apply()
        }
    }

    fun write(key: String, value: Boolean) {
        val prefsEditor: SharedPreferences.Editor = preferences.edit()
        with(prefsEditor) {
            putBoolean(key, value)
            apply()
        }
    }

    fun write(key: String, value: Long) {
        val prefsEditor: SharedPreferences.Editor = preferences.edit()
        with(prefsEditor) {
            putLong(key, value)
            commit()
        }
    }

    fun write(key: String, value: Int) {
        val prefsEditor: SharedPreferences.Editor = preferences.edit()
        with(prefsEditor) {
            putInt(key, value)
            commit()
        }
    }

    fun clearAll() {
        val prefsEditor: SharedPreferences.Editor = preferences.edit()
        with(prefsEditor) {
            clear()
            putBoolean(USER_HAS_SESSION, false)
            commit()
        }
    }

}
