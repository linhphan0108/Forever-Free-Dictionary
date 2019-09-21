package com.example.foreverfreedictionary.ui.screen.about_us

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.foreverfreedictionary.R
import com.example.foreverfreedictionary.ui.baseMVVM.BaseFragment
import kotlinx.android.synthetic.main.fragment_about_us.*

class AboutUsFragment : BaseFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_about_us, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        txtMessage.movementMethod = ScrollingMovementMethod()
    }
}
