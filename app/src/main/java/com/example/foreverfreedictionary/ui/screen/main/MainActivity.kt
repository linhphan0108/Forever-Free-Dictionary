package com.example.foreverfreedictionary.ui.screen.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import com.google.android.material.snackbar.Snackbar
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
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
import com.example.foreverfreedictionary.ui.dialog.VoiceRecognizerDialog
import com.example.foreverfreedictionary.ui.model.AutoCompletionEntity
import com.example.foreverfreedictionary.ui.screen.result.ResultActivity
import com.example.foreverfreedictionary.ui.screen.result.ResultActivity.Companion.ARG_QUERY
import com.example.foreverfreedictionary.util.SNSUtil
import com.example.foreverfreedictionary.vo.Status
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.layout_recycler_view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import android.view.inputmethod.EditorInfo
import androidx.core.view.postDelayed
import com.example.foreverfreedictionary.ui.screen.ocr_text_recognizer.OcrCaptureActivity
import android.app.AlarmManager
import android.app.PendingIntent
import com.example.foreverfreedictionary.extensions.howLongTilNext8Clock
import com.example.foreverfreedictionary.ui.broadcastReceiver.OnAlarmReminderBroadCastReceiver
import java.sql.Date


const val REQUEST_CODE_RESULT_ACTIVITY = 11
const val REQUEST_CODE_OCR_ACTIVITY = 12
class MainActivity : BaseActivity(), AutoCompletionViewHolder.OnItemListeners, CoroutineScope, VoiceRecognizerDialog.Listener {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    private val viewModel: MainActivityViewModel by viewModel(this){injector.mainActivityViewModel}
    private lateinit var appBarConfiguration: AppBarConfiguration

    private lateinit var autoCompletionAdapter: AutoCompletionAdapter
    private var isAutoCompletionViewSetup: Boolean = false

    private var voiceRecognizerDialog: VoiceRecognizerDialog? = null

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

        setupNavigator()
        registerViewModelListeners()
        registerEventListeners()
        scheduleAlarm()
//        val serviceIntent = Intent(this, ReminderService::class.java)
//        startService(serviceIntent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when(requestCode){
            REQUEST_CODE_RESULT_ACTIVITY -> {
                if (resultCode == Activity.RESULT_OK) {
                    edtSearch.postDelayed(200) {
                        showKeyboard(edtSearch)
                    }
                }
            }

            REQUEST_CODE_OCR_ACTIVITY -> {
                if (resultCode == Activity.RESULT_OK) {
                    val word = data?.getStringExtra(OcrCaptureActivity.SELECTED_WORD) ?: ""
                    edtSearch.setText(word)
                    edtSearch.setSelection(word.length)
                    edtSearch.postDelayed(200) {
                        showKeyboard(edtSearch)
                    }
                }
            }
            else ->{
                super.onActivityResult(requestCode, resultCode, data)
            }
        }
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
        openResultScreen(item.value)
    }

    override fun onVoiceRecognizeResult(detectedSpeech: String) {
        edtSearch.setText(detectedSpeech)
        edtSearch.setSelection(detectedSpeech.length)
    }

    //  navigation section
    private fun registerEventListeners(){
        iBtnMic.setOnClickListener {
            if (voiceRecognizerDialog == null){
                voiceRecognizerDialog = VoiceRecognizerDialog.newInstance()
            }
            voiceRecognizerDialog!!.show(supportFragmentManager, MainActivity::class.java.name)
        }

        iBtnCamera.setOnClickListener {
            val intent = Intent(this, OcrCaptureActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE_OCR_ACTIVITY)
        }
    }

    private fun scheduleAlarm() {
        val intent = Intent(applicationContext, OnAlarmReminderBroadCastReceiver::class.java)
        val alarmIntent = PendingIntent.getBroadcast(
            applicationContext,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val startTime = Date(System.currentTimeMillis()).howLongTilNext8Clock()
        val backupAlarmMgr = this.getSystemService(ALARM_SERVICE) as AlarmManager
        backupAlarmMgr.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            startTime,
            AlarmManager.INTERVAL_DAY, //todo this must be changed to 1 hour
            alarmIntent
        )//alarm will repeat after every day
    }

//    navigation section
    fun openResultScreen(query: String){
//        findNavController(R.id.nav_host_fragment).navigate(R.id.resultActivity,
//        ResultActivity.createBundle(query))
        val intent = Intent(this, ResultActivity::class.java)
        intent.putExtra(ARG_QUERY, query)
        startActivityForResult(intent, REQUEST_CODE_RESULT_ACTIVITY)
    }


    //  inner methods
    private fun setupNavigator(){
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(setOf(
            R.id.nav_home,
            R.id.nav_favorites,
            R.id.nav_history,
            R.id.nav_my_vocabulary
        ), drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
//        navView.setupWithNavController(navController)
        navView.setNavigationItemSelectedListener {item ->
            var handled = onNavigationItemSelected(item, navController, navView)
            if (!handled){
                // handle literally select actions by ourselves
                // without redirecting to another screen
                handled = when(item.itemId){
                    R.id.nav_about_us_feedback -> {
                        navController.navigate(R.id.nav_about_us_feedback)
                        true
                    }
                    R.id.action_invite_friends -> {
                        SNSUtil.shareThisApp(this)
                        true
                    }
                    R.id.action_rate_app -> {
                        SNSUtil.rateThisApp(this)
                        true
                    }
                    else -> {
                        false
                    }
                }
            }

            return@setNavigationItemSelectedListener handled
        }
        addOnDestinationChangedListener(navController, navView){destinationId ->
            onHomeScreenVisibilityChanged(destinationId == R.id.nav_home)
        }
    }

    private fun setSearchBox(){
        edtSearch.setOnEditorActionListener { _, actionId, _ ->
            when(actionId){
                EditorInfo.IME_ACTION_SEARCH -> {
                    if (edtSearch.text.isNotBlank()) {
                        openResultScreen(edtSearch.text.toString())
                    }
                    true
                }
                else -> false
            }
        }
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

        edtSearch.setOnFocusChangeListener { view , hasFocus ->
            if (hasFocus) {
                showAutoCompletion(listOf())
            }else{
                hideAutoCompletion()
                hideSoftKeyboard(view)
            }
        }

        iBtnEmptySearchBox.setOnClickListener {
            edtSearch.setText("")
        }
    }

    private fun registerViewModelListeners(){
        viewModel.autoCompletion.observe(this, Observer { resource ->
            when(resource.status){
                Status.LOADING -> {  }
                Status.ERROR -> {
                    dismissLoading()
                    showSnackBar(rcvAutoCompletion, resource.message ?: getString(R.string.unknown_error_message), length = Snackbar.LENGTH_INDEFINITE,
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
            isAutoCompletionViewSetup = true
        }else{
            autoCompletionAdapter.items = data
        }
        llAutocompletionContainer.visibility = View.VISIBLE
        txtEmpty.visibility = if (data.isEmpty() && edtSearch.text.isNotEmpty()) {
            View.VISIBLE
        } else View.INVISIBLE
    }

    private fun clearAutoCompletion(){
        if (isAutoCompletionViewSetup) {
            autoCompletionAdapter.items = listOf()
        }
    }

    private fun hideAutoCompletion(){
        llAutocompletionContainer?.apply { this.visibility = View.GONE }
    }

    private fun onSearchBoxInputChanged(isEmpty: Boolean) {
        iBtnEmptySearchBox.visibility = if(isEmpty) View.INVISIBLE else View.VISIBLE
        iBtnMic.visibility = if (isEmpty) View.VISIBLE else View.GONE
        iBtnCamera.visibility = if (isEmpty) View.VISIBLE else View.GONE
        if (isEmpty) clearAutoCompletion()
    }

    private fun onHomeScreenVisibilityChanged(visibility: Boolean){
        val vs = if (visibility) View.VISIBLE else View.GONE
        cslSearchBoxContainer.visibility = vs
        swDarkMode.visibility = vs
        hideAutoCompletion()
    }
}
