package com.example.foreverfreedictionary.ui.screen.main

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.widget.Toolbar
import android.view.Menu
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foreverfreedictionary.R
import com.example.foreverfreedictionary.di.injector
import com.example.foreverfreedictionary.di.viewModel
import com.example.foreverfreedictionary.extensions.showSnackBar
import com.example.foreverfreedictionary.ui.adapter.AutoCompletionAdapter
import com.example.foreverfreedictionary.ui.adapter.viewholder.AutoCompletionViewHolder
import com.example.foreverfreedictionary.ui.baseMVVM.BaseActivity
import com.example.foreverfreedictionary.ui.model.AutoCompletionEntity
import com.example.foreverfreedictionary.ui.screen.result.ResultActivity
import com.example.foreverfreedictionary.vo.Status
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.layout_recycler_view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class MainActivity : BaseActivity(), AutoCompletionViewHolder.OnItemListeners, CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    private val viewModel: MainActivityViewModel by viewModel(this){injector.mainActivityViewModel}
    private lateinit var appBarConfiguration: AppBarConfiguration

    private lateinit var autoCompletionAdapter: AutoCompletionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        setSearchBox()

        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(setOf(
            R.id.nav_home,
            R.id.nav_gallery,
            R.id.nav_slideshow,
            R.id.nav_tools,
            R.id.nav_share,
            R.id.nav_send
        ), drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        registerViewModelListeners()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onBackPressed() {
        if (llAutocompletionContainer != null && llAutocompletionContainer.visibility == View.VISIBLE){
            edtSearch.clearFocus()
            llAutocompletionContainer.visibility = View.GONE
        }else {
            super.onBackPressed()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    //callback listeners
    override fun onItemClick(item: AutoCompletionEntity) {
        edtSearch.setText(item.value)
        openResultScreen(item.value)
    }

//    navigation section
    fun openResultScreen(query: String){
        findNavController(R.id.nav_host_fragment).navigate(R.id.resultActivity,
        ResultActivity.createBundle(query))
    }


    private fun setSearchBox(){
        edtSearch.addTextChangedListener(object :TextWatcher{
            private var searchFor = ""
            override fun afterTextChanged(s: Editable) {
                onSearchBoxInputChanged(s.isEmpty())
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val searchText = s.toString().trim()
                if (searchText == searchFor) return

                searchFor = searchText

                if (searchText.isEmpty()) return

                launch {
                    delay(300)  //debounce timeOut
                    if (searchText != searchFor)
                        return@launch

                    // do our magic here
                    viewModel.autocompleteQuery(searchFor)
                }
            }

        })

        edtSearch.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                showAutoCompletion(listOf())
            }else{
                hideAutoCompletion()
            }
        }

        iBtnEmptySearchBox.setOnClickListener {
            edtSearch.setText("")
        }
    }

    private fun registerViewModelListeners(){
        viewModel.autoCompletionResponse.observe(this, Observer { resource ->
            when(resource.status){
                Status.LOADING -> {  }
                Status.ERROR -> {
                    dismissLoading()
                    showSnackBar(rcvAutoCompletion, "Oops something went wrong", length = Snackbar.LENGTH_INDEFINITE,
                        listener = View.OnClickListener {
                        })
                }
                Status.SUCCESS -> {
                    dismissLoading()
                    showAutoCompletion(resource.data!!)
                }
            }
        })
    }

    private fun showAutoCompletion(data: List<AutoCompletionEntity>) {
        if(viewStubAutoCompletion != null) {
            viewStubAutoCompletion.inflate()
            autoCompletionAdapter = AutoCompletionAdapter(data, this)
            rcvAutoCompletion.layoutManager = LinearLayoutManager(this)
            rcvAutoCompletion.adapter = autoCompletionAdapter
        }else{
            autoCompletionAdapter.items = data
        }
        llAutocompletionContainer.visibility = View.VISIBLE
        txtEmpty.visibility = if (data.isEmpty() && edtSearch.text.isNotEmpty()) {
            View.VISIBLE
        } else View.INVISIBLE
    }

    private fun clearAutoCompletion(){
        autoCompletionAdapter.items = listOf()
    }

    private fun hideAutoCompletion(){
        llAutocompletionContainer.visibility = View.GONE
    }

    private fun onSearchBoxInputChanged(isEmpty: Boolean) {
        iBtnEmptySearchBox.visibility = if(isEmpty) View.GONE else View.VISIBLE
        iBtnMic.visibility = if (isEmpty) View.VISIBLE else View.INVISIBLE
        iBtnCamera.visibility = if (isEmpty) View.VISIBLE else View.INVISIBLE
        if (isEmpty) clearAutoCompletion()
    }
}
