package com.example.foreverfreedictionary.ui.screen.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.example.foreverfreedictionary.R
import com.example.foreverfreedictionary.ui.screen.result.ResultActivity
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        homeViewModel.text.observe(this, Observer {
        })
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        edtSearch.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            when(actionId){
                EditorInfo.IME_ACTION_SEARCH -> {
                    navigateResultScreen()
                    true
                }
                else -> false
            }
        })
    }


    private fun navigateResultScreen(){
        activity?.findNavController(R.id.nav_host_fragment)?.navigate(R.id.resultActivity, ResultActivity.createBundle(edtSearch.text.toString()))

//        ActivityNavigator(this)
//            .createDestination()
//            .setIntent(Intent(this, SecondActivity::class.java))
//            .navigate(null, null)
    }
}