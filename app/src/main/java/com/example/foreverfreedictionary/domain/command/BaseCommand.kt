package com.example.foreverfreedictionary.domain.command

import android.content.Context
import android.net.ConnectivityManager
import com.example.foreverfreedictionary.vo.Resource
import java.lang.Exception

abstract class BaseCommand<T> {
    abstract suspend fun execute(): Resource<T>

    suspend fun execute(context: Context): Resource<T>{
        isInternetOn(context)
        return execute()
    }

    private fun isInternetOn(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return if (activeNetworkInfo != null && activeNetworkInfo.isConnected){
            true
        }else{
            throw Exception("no internet")
        }
    }
}