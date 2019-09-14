package com.example.foreverfreedictionary.extensions

import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.annotation.DimenRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import com.example.foreverfreedictionary.R
import com.google.android.material.snackbar.Snackbar

fun Context.color(res: Int): Int = ContextCompat.getColor(this, res)
fun Context.toast(message: String, length: Int = Toast.LENGTH_SHORT){
    Toast.makeText(this.applicationContext, message, length).show()
}

fun Context.showSnackBar(view: View, @StringRes resId: Int, length: Int = Snackbar.LENGTH_LONG,
                         action: String = getString(R.string.default_snack_bar_action),
                         listener: View.OnClickListener? = null){
    val l = if (listener != null)  Snackbar.LENGTH_INDEFINITE else length
    Snackbar.make(view, resId, l)
        .setAction(action, listener).show()
}
fun Context.showSnackBar(view: View, message: String, length: Int = Snackbar.LENGTH_LONG,
                         action: String = getString(R.string.default_snack_bar_action),
                         listener: View.OnClickListener? = null){
    val l = if (listener != null)  Snackbar.LENGTH_INDEFINITE else length
    Snackbar.make(view, message, l)
        .setAction(action, listener).show()
}

fun Context.getDimensionPixelSize(@DimenRes dimenRes: Int) = resources.getDimensionPixelSize(dimenRes)
fun Context.getDimension(@DimenRes dimenRes: Int) = resources.getDimension(dimenRes)