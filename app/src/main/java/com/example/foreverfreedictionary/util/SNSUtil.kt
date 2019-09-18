package com.example.foreverfreedictionary.util

import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.foreverfreedictionary.BuildConfig
import com.example.foreverfreedictionary.R
import android.content.ActivityNotFoundException
import android.net.Uri


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

    fun rateThisApp(context: Context){
        val uri = Uri.parse("market://details?id=" + context.packageName)
        val goToMarket = Intent(Intent.ACTION_VIEW, uri)
        // To count with Play market back stack, After pressing back button,
        // to taken back to our application, we need to add following flags to intent.
        goToMarket.addFlags(
            Intent.FLAG_ACTIVITY_NO_HISTORY or
                 Intent.FLAG_ACTIVITY_NEW_DOCUMENT or
                 Intent.FLAG_ACTIVITY_MULTIPLE_TASK )
        try{
            context.startActivity(goToMarket)
        }
        catch (e:ActivityNotFoundException) {
            context.startActivity(
                Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + context.packageName)))
        }

    }
}