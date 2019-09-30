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
import com.example.foreverfreedictionary.extensions.showSnackBar
import com.example.foreverfreedictionary.ui.baseMVVM.BaseFragment
import com.example.foreverfreedictionary.ui.screen.main.MainActivity
import com.example.foreverfreedictionary.util.WebViewUtil
import com.example.foreverfreedictionary.vo.Status
import com.google.android.material.snackbar.Snackbar
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
        wvWotd.webViewClient = object : WebViewClient(){

            override fun shouldOverrideUrlLoading(view: WebView?, url: String): Boolean {
                val query = WebViewUtil.extractQuery(url)
                if (!query.isNullOrBlank()){
                    (activity as MainActivity).openResultScreen(query)
                }
                return true
            }

            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                request?.url?.let {
                    val stringUri = it.toString()
                    val query = WebViewUtil.extractQuery(stringUri)
                    if (!query.isNullOrBlank()){
                        (activity as MainActivity).openResultScreen(query)
                    }
                }
                return true
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                progressBar?.apply { this.visibility = View.GONE}
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
                    view?.let {
                        showSnackBar(it, resource.message ?: getString(R.string.unknown_error_message), length = Snackbar.LENGTH_INDEFINITE,
                            listener = View.OnClickListener {
                            })
                    }
                }
                Status.SUCCESS -> {
                    showWebViewResult(resource.data.orEmpty())
                }
            }
        })
    }
}