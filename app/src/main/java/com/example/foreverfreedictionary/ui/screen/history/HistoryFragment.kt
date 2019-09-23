package com.example.foreverfreedictionary.ui.screen.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foreverfreedictionary.R
import com.example.foreverfreedictionary.di.injector
import com.example.foreverfreedictionary.di.viewModel
import com.example.foreverfreedictionary.ui.adapter.HistoryAdapter
import com.example.foreverfreedictionary.ui.adapter.viewholder.HistoryViewHolder
import com.example.foreverfreedictionary.ui.model.HistoryEntity
import com.example.foreverfreedictionary.ui.screen.result.ResultActivity
import com.example.foreverfreedictionary.vo.Status
import kotlinx.android.synthetic.main.fragment_history.*
import kotlinx.android.synthetic.main.view_no_data.*

class HistoryFragment : Fragment(), HistoryViewHolder.OnItemListeners {
    private val viewModel: HistoryViewModel by viewModel(this){injector.historyViewModel}
    private lateinit var historyAdapter: HistoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        registerViewModelListeners()
        setupRecyclerView()
        viewModel.getHistory()
    }

    // event listeners
    override fun onItemClicked(item: HistoryEntity) {
        findNavController().navigate(R.id.action_nav_to_resultActivity,
            ResultActivity.createBundle(item.query))
    }

    override fun onFavoriteButtonClicked(item: HistoryEntity) {
        viewModel.updateFavorite(item.query, !item.isFavorite)
    }

    private fun registerViewModelListeners(){
        viewModel.historyResponse.observe(this, Observer { resource ->
            when(resource.status){
                Status.LOADING -> {}
                Status.ERROR -> {}
                Status.SUCCESS -> {
                    val data = resource.data
                    historyAdapter.items = data
                    txtEmpty.visibility = if (data?.isEmpty() == true){
                        View.VISIBLE
                    }else{
                        View.GONE
                    }
                }
            }
        })
    }

    private fun setupRecyclerView(){
        historyAdapter = HistoryAdapter(listOf(), this)
        rcvHistory.layoutManager = LinearLayoutManager(context)
        rcvHistory.adapter = historyAdapter
    }
}