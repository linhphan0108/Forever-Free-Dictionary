package com.example.foreverfreedictionary.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.foreverfreedictionary.R

class LoadingDialog : DialogFragment() {
    companion object {
        fun newInstance() = LoadingDialog()
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_loading, container)
    }
}