package com.example.foreverfreedictionary.ui.screen.home

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.lifecycle.Observer
import com.example.foreverfreedictionary.R
import com.example.foreverfreedictionary.di.injector
import com.example.foreverfreedictionary.di.viewModel
import com.example.foreverfreedictionary.ui.baseMVVM.BaseFragment
import com.example.foreverfreedictionary.ui.screen.main.MainActivity
import com.example.foreverfreedictionary.util.SEARCH_FORM_SUBMIT_DIRECTION_URL
import com.example.foreverfreedictionary.util.DOMAIN
import com.example.foreverfreedictionary.vo.Status
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : BaseFragment() {

    private val homeViewModel: HomeViewModel by viewModel(this){ injector.homeViewModel }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupWebView()
        registerViewModelListeners()
        homeViewModel.fetchWordOfTheDay()
    }

    private fun setupWebView(){
        @SuppressLint("SetJavaScriptEnabled")
        wvWotd.settings.javaScriptEnabled = true
//        wvResult.settings.allowUniversalAccessFromFileURLs = true
//        wvResult.settings.allowFileAccessFromFileURLs = true
        wvWotd.webViewClient = object : WebViewClient(){

            override fun shouldOverrideUrlLoading(view: WebView?, url: String): Boolean {
//                view.query(url)
                return true
            }

            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                request?.url?.let {
                    val stringUri = it.toString()
                    val query = when {
                        stringUri.startsWith(SEARCH_FORM_SUBMIT_DIRECTION_URL) -> stringUri.substringAfterLast('/')
                        stringUri.startsWith(DOMAIN) -> stringUri.substringAfterLast(DOMAIN)
                        else -> ""
                    }
                    if (query.isNotBlank()){
                        (activity as MainActivity).openResultScreen(query)
                    }
                }
                return true
            }
        }
    }

    private fun showWebViewResult(data: String){
        wvWotd.loadDataWithBaseURL("file:///android_asset/", data, "text/html", "UTF-8", null)
    }

    private fun registerViewModelListeners(){
        homeViewModel.wordOfTheDay.observe(this, Observer { resource ->
            when(resource.status){
                Status.LOADING -> { }
                Status.ERROR -> {
                }
                Status.SUCCESS -> {
                    showWebViewResult(resource.data.orEmpty())
                }
            }
        })
    }
}