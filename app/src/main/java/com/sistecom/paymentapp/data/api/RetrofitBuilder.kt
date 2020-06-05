package com.sistecom.paymentapp.data.api

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

/**
 * Created by: cristianramirez
 * On: 4/06/20 at: 05:36 PM
 *
 */

object RetrofitBuilder {

    private fun RetrofitBuilder() {}

    private val API_BASE_URL: String = "http://172.107.178.99/sistecore/"//Constants.HOST_NAME

    private val gsonBuilder = GsonBuilder()
    //.registerTypeAdapter(Date::class.java, DateDeserializer())

    private val builder = Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gsonBuilder.create()))

    private var retrofit = builder.build()

    private val loggingInterceptor = HttpLoggingInterceptor()

    private val okHttpClient = OkHttpClient.Builder()
            //.readTimeout(Constants.READ_TIMEOUT_OKHTTP, Constants.TIME_UNIT_OKHTTP)
            //.connectTimeout(Constants.CONNECT_TIMEOUT_OKHTTP, Constants.TIME_UNIT_OKHTTP)

    fun <T> createService(serviceClass: Class<T>): T {
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        if (!okHttpClient.interceptors().contains(loggingInterceptor)) {
            okHttpClient.addInterceptor(loggingInterceptor)
            builder.client(okHttpClient.build())
            retrofit = builder.build()
        }
        return retrofit.create(serviceClass)
    }

    val apiService: SistecomAPI = RetrofitBuilder.createService(SistecomAPI::class.java)
}