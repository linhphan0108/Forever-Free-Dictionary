package com.example.foreverfreedictionary.ui.screen.reminder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foreverfreedictionary.R
import com.example.foreverfreedictionary.di.injector
import com.example.foreverfreedictionary.di.viewModel
import com.example.foreverfreedictionary.extensions.getDimension
import com.example.foreverfreedictionary.extensions.getTomorrow0Clock
import com.example.foreverfreedictionary.ui.adapter.ReminderApdater
import com.example.foreverfreedictionary.ui.adapter.viewholder.ReminderViewHolder
import com.example.foreverfreedictionary.ui.baseMVVM.BaseFragment
import com.example.foreverfreedictionary.ui.model.ReminderEntity
import com.example.foreverfreedictionary.ui.screen.result.ResultActivity
import com.example.foreverfreedictionary.util.VerticalSpaceItemDecoration
import com.example.foreverfreedictionary.vo.Status
import kotlinx.android.synthetic.main.fragment_reminder.*
import kotlinx.android.synthetic.main.view_no_data.*
import java.sql.Date

class ReminderFragment : BaseFragment(), ReminderViewHolder.OnItemListeners {

    private val viewModel by viewModel(this){injector.reminderViewModel}

    private lateinit var reminderAdapter: ReminderApdater

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_reminder, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        viewModel.getReminders()
        registerViewModelListeners()
    }

    private fun registerViewModelListeners(){
        viewModel.remindersResponse.observe(this, Observer { resource ->
            when(resource.status){
                Status.LOADING -> {}
                Status.ERROR -> {}
                Status.SUCCESS -> {
                    val data = resource.data
                    reminderAdapter.items = data
                    txtEmpty.visibility = if (data?.isEmpty() == true){
                        View.VISIBLE
                    }else{
                        View.GONE
                    }
                }
            }
        })

        viewModel.insertReminderResponse.observe(this, Observer {resource ->
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

    override fun onItemClicked(item: ReminderEntity) {
        findNavController().navigate(R.id.action_nav_to_resultActivity,
            ResultActivity.createBundle(item.query))
    }

    override fun onSetReminderButtonClicked(item: ReminderEntity) {
        val query = item.query
        val isReminded = !item.isReminded
        val time = if (isReminded) {
            Date(System.currentTimeMillis()).getTomorrow0Clock()
        }else{
            item.remindTime
        }
        viewModel.updateReminderStatus(query, isReminded, time)
    }

    override fun onDeleteButtonClicked(item: ReminderEntity) {

    }

    private fun setupRecyclerView(){
        reminderAdapter = ReminderApdater(listOf(), this)
        val space: Int = context?.getDimension(R.dimen.recycler_View_divider)?.toInt() ?: 0
        val itemDecor = VerticalSpaceItemDecoration(verticalSpace = space,
            firstItem = space,
            dividerDrawableRes = R.drawable.recycler_view_divider,
            context = context)
        rcvReminder.layoutManager = LinearLayoutManager(context)
        rcvReminder.addItemDecoration(itemDecor)
        rcvReminder.adapter = reminderAdapter
    }
}