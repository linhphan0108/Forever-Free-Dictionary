package com.example.foreverfreedictionary.ui.baseMVVM

import androidx.appcompat.app.AppCompatActivity
import com.example.foreverfreedictionary.ui.dialog.LoadingDialog

abstract class BaseActivity : AppCompatActivity(){
    private val loadingDialog: LoadingDialog by lazy { LoadingDialog.newInstance() }

    fun showLoading(){
        loadingDialog.showLoading(supportFragmentManager)
    }

    fun dismissLoading(){
        loadingDialog.dismissLoading()
    }
}