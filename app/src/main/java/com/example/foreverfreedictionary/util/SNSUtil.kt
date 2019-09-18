package com.example.foreverfreedictionary.util

import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.foreverfreedictionary.BuildConfig
import com.example.foreverfreedictionary.R


object SNSUtil{
    fun shareThisApp(context: Context){
        try {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.app_name))
            val shareMessage = context.getString(R.string.share_message, BuildConfig.APPLICATION_ID)
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
            context.startActivity(Intent.createChooser(shareIntent, "Choose one"))
        } catch (e: Exception) {
            Log.e("SNSUtil", e.message ?: "")
        }
    }
}