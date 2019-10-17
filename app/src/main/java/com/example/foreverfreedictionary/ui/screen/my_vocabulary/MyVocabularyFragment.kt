package com.example.foreverfreedictionary.ui.screen.my_vocabulary

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foreverfreedictionary.R
import com.example.foreverfreedictionary.di.injector
import com.example.foreverfreedictionary.di.viewModel
import com.example.foreverfreedictionary.extensions.getDimension
import com.example.foreverfreedictionary.ui.adapter.MyVocabularyAdapter
import com.example.foreverfreedictionary.ui.adapter.viewholder.MyVocabularyViewHolder
import com.example.foreverfreedictionary.ui.model.MyVocabularyEntity
import com.example.foreverfreedictionary.ui.screen.main.REQUEST_CODE_RESULT_ACTIVITY
import com.example.foreverfreedictionary.ui.screen.result.ResultActivity
import com.example.foreverfreedictionary.util.VerticalSpaceItemDecoration
import com.example.foreverfreedictionary.vo.Status
import kotlinx.android.synthetic.main.fragment_my_vocabulary.*
import kotlinx.android.synthetic.main.view_no_data.*

class MyVocabularyFragment : Fragment(), MyVocabularyViewHolder.OnItemListeners{

    private val viewModel: MyVocabularyViewModel by viewModel(this){injector.myVocabularyViewModel}

    private lateinit var myVocabularyAdapter: MyVocabularyAdapter

    val args: MyVocabularyFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_my_vocabulary_group, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val groupName = args.groupName
        viewModel.getAllMyVocabularyOfGroup(groupName)
        setupRecyclerView()
        registerViewModelListeners()
    }


    override fun delete(query: String, groupName: String) {
        viewModel.deleteMyVocabulary(query, groupName)
    }

    override fun onItemClick(item: MyVocabularyEntity) {
        val intent = Intent(activity, ResultActivity::class.java)
        intent.putExtra(ResultActivity.ARG_QUERY, item.query)
        startActivityForResult(intent, REQUEST_CODE_RESULT_ACTIVITY)
    }

    private fun registerViewModelListeners(){
        viewModel.myVocabulary.observe(this, Observer { resource ->
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
        myVocabularyAdapter = MyVocabularyAdapter(listOf(), listener = this)
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