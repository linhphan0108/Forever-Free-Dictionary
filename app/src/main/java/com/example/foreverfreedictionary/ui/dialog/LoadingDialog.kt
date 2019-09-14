package com.example.foreverfreedictionary.ui.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.example.foreverfreedictionary.R

class LoadingDialog : DialogFragment() {
    companion object {
        fun newInstance() = LoadingDialog()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return inflater.inflate(R.layout.dialog_loading, container)
    }

    public fun showLoading(fragmentManager: FragmentManager){
        show(fragmentManager, DialogFragment::class.java.name)
    }

    public fun dismissLoading(){
        if (showsDialog && isAdded) dismiss()
    }
}