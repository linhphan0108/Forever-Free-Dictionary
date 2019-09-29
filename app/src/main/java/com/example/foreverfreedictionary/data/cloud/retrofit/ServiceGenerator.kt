package com.example.foreverfreedictionary.data.cloud.retrofit

import com.example.foreverfreedictionary.util.DOMAIN
import com.example.retrofit.ErrorHandlingAdapter
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.converter.gson.GsonConverterFactory

object ServiceGenerator {
    private var retrofit: Retrofit? = null
    private val gson = GsonBuilder().create()
    private val httpLoggingInterceptor =
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    private val okHttpClientBuilder = OkHttpClient.Builder()
        .addInterceptor(httpLoggingInterceptor)
    private val okHttpClient = okHttpClientBuilder.build()

    fun <T> createService(serviceClass: Class<T>): T {
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(DOMAIN)
                .addConverterFactory(GsonConverterFactory.create(gson))
//                .addCallAdapterFactory()
                .build()
        }
        return retrofit!!.create(serviceClass)
    }

}