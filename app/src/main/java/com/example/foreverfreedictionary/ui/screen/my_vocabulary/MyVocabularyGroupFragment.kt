package com.example.foreverfreedictionary.ui.screen.my_vocabulary

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foreverfreedictionary.R
import com.example.foreverfreedictionary.di.injector
import com.example.foreverfreedictionary.di.viewModel
import com.example.foreverfreedictionary.extensions.getDimension
import com.example.foreverfreedictionary.ui.adapter.MyVocabularyAdapter
import com.example.foreverfreedictionary.ui.adapter.viewholder.MyVocabularyGroupViewHolder
import com.example.foreverfreedictionary.ui.dialog.NewMyVocabularyGroupDialog
import com.example.foreverfreedictionary.ui.model.MyVocabularyGroupEntity
import com.example.foreverfreedictionary.util.VerticalSpaceItemDecoration
import com.example.foreverfreedictionary.vo.Status
import kotlinx.android.synthetic.main.fragment_my_vocabulary_group.*
import kotlinx.android.synthetic.main.view_no_data.*

class MyVocabularyGroupFragment : Fragment(), MyVocabularyGroupViewHolder.OnItemListeners,
    NewMyVocabularyGroupDialog.OnListeners {

    private val viewModel: MyVocabularyGroupViewModel by viewModel(this){injector.myVocabularyGroupViewModel}

    private lateinit var myVocabularyAdapter: MyVocabularyAdapter
    private val newMyVocabularyGroupDialog by lazy { NewMyVocabularyGroupDialog.newInstance().apply { listener = this@MyVocabularyGroupFragment } }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_my_vocabulary_group, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getAllMyVocabularyGroup()
        setupRecyclerView()
        registerViewModelListeners()
    }



    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.fragment_my_vocabulary_group, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.action_add_vocabulary_group -> {
                fragmentManager?.let {
                    newMyVocabularyGroupDialog.show(it, NewMyVocabularyGroupDialog::javaClass.name)
                }
                true
            }
            else -> {false}
        }
    }

    override fun onInsertGroupSuccess() {

    }

    override fun onInsertGroupFailed(message: String) {
    }

    override fun delete(groupName: String) {
        viewModel.deleteMyVocabularyGroup(groupName)
    }

    override fun onItemClick(item: MyVocabularyGroupEntity) {
        val action = MyVocabularyGroupFragmentDirections.actionNavMyVocabularyGroupToNavMyVocabulary(item.name)
        findNavController().navigate(action)
    }

    private fun registerViewModelListeners(){
        viewModel.myVocabularyGroup.observe(this, Observer {resource ->
            when(resource.status){
                Status.LOADING -> {}
                Status.ERROR -> {}
                Status.SUCCESS -> {
                    val data = resource.data
                    myVocabularyAdapter.items = data
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
        myVocabularyAdapter = MyVocabularyAdapter(listOf(), this)
        val space: Int = context?.getDimension(R.dimen.recycler_View_divider)?.toInt() ?: 0
        val itemDecor = VerticalSpaceItemDecoration(verticalSpace = space,
            firstItem = space,
            dividerDrawableRes = R.drawable.recycler_view_divider,
            context = context)
        rcvMyVocabularyGroup.layoutManager = LinearLayoutManager(context)
        rcvMyVocabularyGroup.addItemDecoration(itemDecor)
        rcvMyVocabularyGroup.adapter = myVocabularyAdapter
    }
}