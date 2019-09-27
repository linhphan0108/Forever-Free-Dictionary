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
import com.example.foreverfreedictionary.extensions.getDimension
import com.example.foreverfreedictionary.extensions.getTomorrow0Clock
import com.example.foreverfreedictionary.extensions.showSnackBar
import com.example.foreverfreedictionary.ui.adapter.FavoriteAdapter
import com.example.foreverfreedictionary.ui.adapter.viewholder.FavoriteViewHolder
import com.example.foreverfreedictionary.ui.model.FavoriteEntity
import com.example.foreverfreedictionary.ui.screen.result.ResultActivity
import com.example.foreverfreedictionary.util.VerticalSpaceItemDecoration
import com.example.foreverfreedictionary.vo.Status
import kotlinx.android.synthetic.main.fragment_favorite.*
import kotlinx.android.synthetic.main.view_no_data.*
import java.sql.Date

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
        viewModel.removeFavorite(item)
    }

    override fun onSetReminderButtonClicked(item: FavoriteEntity) {
        viewModel.setReminder(item)
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

        viewModel.insertFavoriteResponse.observe(this, Observer {resource ->
            when(resource.status) {
                Status.LOADING -> { }
                Status.ERROR -> { }
                Status.SUCCESS -> {
                }
            }
        })

        viewModel.removeFavoriteResponse.observe(this, Observer { resource ->
            when(resource.status) {
                Status.LOADING -> { }
                Status.ERROR -> { }
                Status.SUCCESS -> {
                    resource.data?.let { confirmRollback(it) }
                }
            }
        })

        viewModel.setReminderResponse.observe(this, Observer { resource ->
            when(resource.status) {
                Status.LOADING -> {
                }
                Status.ERROR -> {
                }
                Status.SUCCESS -> {

                }
            }
        })
    }

    private fun setupRecyclerView(){
        favoriteAdapter = FavoriteAdapter(listOf(), this)
        val space: Int = context?.getDimension(R.dimen.recycler_View_divider)?.toInt() ?: 0
        val itemDecor = VerticalSpaceItemDecoration(verticalSpace = space,
            firstItem = space,
            dividerDrawableRes = R.drawable.recycler_view_divider,
            context = context)
        rcvFavorite.layoutManager = LinearLayoutManager(context)
        rcvFavorite.addItemDecoration(itemDecor)
        rcvFavorite.adapter = favoriteAdapter
    }

    private fun confirmRollback(item: FavoriteEntity){
        view?.let {
            showSnackBar(it, getString(R.string.ask_for_rollback_message, item.word),
                action = getString(R.string.favorite_rollback),
                listener = View.OnClickListener {
                    viewModel.insertFavorite(item)
                })
        }
    }
}