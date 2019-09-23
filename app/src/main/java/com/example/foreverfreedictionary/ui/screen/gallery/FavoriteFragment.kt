package com.example.foreverfreedictionary.ui.screen.gallery

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
import com.example.foreverfreedictionary.ui.adapter.FavoriteAdapter
import com.example.foreverfreedictionary.ui.adapter.HistoryAdapter
import com.example.foreverfreedictionary.ui.adapter.viewholder.FavoriteViewHolder
import com.example.foreverfreedictionary.ui.model.FavoriteEntity
import com.example.foreverfreedictionary.ui.screen.result.ResultActivity
import com.example.foreverfreedictionary.vo.Status
import kotlinx.android.synthetic.main.fragment_favorite.*
import kotlinx.android.synthetic.main.view_no_data.*

class FavoriteFragment : Fragment(), FavoriteViewHolder.OnItemListeners {

    private val viewModel: FavoriteViewModel by viewModel(this){injector.favoriteViewModel}
    private lateinit var favoriteAdapter: FavoriteAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_favorite, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        registerViewModelListeners()
        viewModel.getFavorites()
    }


    override fun onItemClicked(item: FavoriteEntity) {
        findNavController().navigate(R.id.action_nav_to_resultActivity,
            ResultActivity.createBundle(item.query))
    }

    override fun onFavoriteButtonClicked(item: FavoriteEntity) {
        viewModel.updateFavorite(item.query, !item.isFavorite)
    }

    private fun registerViewModelListeners(){
        viewModel.favoriteResponse.observe(this, Observer { resource ->
            when(resource.status){
                Status.LOADING -> {}
                Status.ERROR -> {}
                Status.SUCCESS -> {
                    val data = resource.data
                    favoriteAdapter.items = data
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
        favoriteAdapter = FavoriteAdapter(listOf(), this)
        rcvFavorite.layoutManager = LinearLayoutManager(context)
        rcvFavorite.adapter = favoriteAdapter
    }
}