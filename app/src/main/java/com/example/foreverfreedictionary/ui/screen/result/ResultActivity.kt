package com.example.foreverfreedictionary.ui.screen.result

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.lifecycle.Observer
import com.example.foreverfreedictionary.R
import com.example.foreverfreedictionary.di.injector
import com.example.foreverfreedictionary.di.viewModel
import com.example.foreverfreedictionary.extensions.showSnackBar
import com.example.foreverfreedictionary.ui.baseMVVM.BaseActivity
import com.example.foreverfreedictionary.util.LOCAL_DICTIONARY_URL
import com.example.foreverfreedictionary.util.LOCAL_DOMAIN
import com.example.foreverfreedictionary.vo.Status
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_result_screen.*


class ResultActivity : BaseActivity() {

    private val viewModel: ResultActivityViewModel by viewModel(this){injector.resultActivityViewModel}

    companion object{
        const val ARG_QUERY = "ARG_QUERY"
        fun createBundle(query: String): Bundle{
            return Bundle().apply {
                putString(ARG_QUERY, query)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result_screen)
        setupToolbar()
        setupWebView()
        registerViewModelListeners()

        intent.extras?.getString(ARG_QUERY)?.let {
            viewModel.query(it)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.activity_result, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        menu?.findItem(R.id.action_pre)?.apply {
            val canGoBack = wvResult.canGoBack()
            isEnabled = canGoBack
            icon.alpha = if(canGoBack) 255 else 130
        }
        menu?.findItem(R.id.action_next)?.apply {
            val canGoForward = wvResult.canGoForward()
            isEnabled = canGoForward
            icon.alpha = if(canGoForward) 255 else 130
        }
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.action_pre -> {
                if(wvResult.canGoBack()) wvResult.goBack()
                true
            }

            R.id.action_next -> {
                if (wvResult.canGoForward()) wvResult.goForward()
                true
            }

            R.id.action_quick_back_search ->{
                setResult(RESULT_OK)
                onBackPressed()
                true
            }

            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun setupWebView(){
        @SuppressLint("SetJavaScriptEnabled")
        wvResult.settings.javaScriptEnabled = true
//        wvResult.settings.allowUniversalAccessFromFileURLs = true
//        wvResult.settings.allowFileAccessFromFileURLs = true
        wvResult.webViewClient = object : WebViewClient(){

            override fun shouldOverrideUrlLoading(view: WebView?, url: String): Boolean {
                viewModel.query(url)
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
                        stringUri.startsWith(LOCAL_DICTIONARY_URL) -> stringUri.substringAfterLast('/')
                        stringUri.startsWith(LOCAL_DOMAIN) -> stringUri.substringAfterLast(LOCAL_DOMAIN)
                        else -> ""
                    }
                    if (query.isNotBlank()){
                        viewModel.query(query)
                    }
                }
                return true
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                invalidateOptionsMenu()
            }
        }
    }

    private fun setupToolbar(){
        title = ""
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun showWebViewResult(data: String){
        wvResult.loadDataWithBaseURL("file:///android_asset/", data, "text/html", "UTF-8", null)
    }

    private fun registerViewModelListeners(){
        viewModel.dictionary.observe(this, Observer { resource ->
            when(resource.status){
                Status.LOADING -> { showLoading() }
                Status.ERROR -> {
                    dismissLoading()
                    showSnackBar(wvResult, "Oops something went wrong", length = Snackbar.LENGTH_INDEFINITE,
                        listener = View.OnClickListener {
                        })
                }
                Status.SUCCESS -> {
                    dismissLoading()
                    showWebViewResult(resource.data.orEmpty())
                }
            }
        })
    }
}
