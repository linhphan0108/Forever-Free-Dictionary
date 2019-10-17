package com.example.foreverfreedictionary.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foreverfreedictionary.R
import com.example.foreverfreedictionary.di.injector
import com.example.foreverfreedictionary.di.viewModel
import com.example.foreverfreedictionary.extensions.getDimension
import com.example.foreverfreedictionary.ui.adapter.MyVocabularyGroupPickerAdapter
import com.example.foreverfreedictionary.ui.adapter.viewholder.MyVocabularyGroupPickerViewHolder
import com.example.foreverfreedictionary.ui.model.DictionaryEntity
import com.example.foreverfreedictionary.ui.model.MyVocabularyGroupEntity
import com.example.foreverfreedictionary.util.VerticalSpaceItemDecoration
import com.example.foreverfreedictionary.vo.Status
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.dialog_my_vocabulary_group_picker.*
import kotlinx.android.synthetic.main.view_no_data.*


const val ARG_DICTIONARY_ENTITY = "ARG_DICTIONARY_ENTITY"
class MyVocabularyGroupPicker : BottomSheetDialogFragment(),
    MyVocabularyGroupPickerViewHolder.OnItemListeners {

    private val viewModel by viewModel(this){injector.myVocabularyGroupPickerViewModel}

    private lateinit var myVocabularyAdapter: MyVocabularyGroupPickerAdapter
    private lateinit var dictionaryEntity: DictionaryEntity

    companion object{
        fun newInstance(it: DictionaryEntity): MyVocabularyGroupPicker{
            return MyVocabularyGroupPicker().apply {
                val bundle = Bundle()
                bundle.putParcelable(ARG_DICTIONARY_ENTITY, it)
                arguments = bundle
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_my_vocabulary_group_picker, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dictionaryEntity = arguments!!.getParcelable(ARG_DICTIONARY_ENTITY)!!
        viewModel.getAllMyVocabularyGroup(dictionaryEntity.query)
        setupRecyclerView()
        registerViewModelListeners()
        btnNewVocabularyGroup.setOnClickListener {
            NewMyVocabularyGroupDialog.newInstance().show(childFragmentManager, NewMyVocabularyGroupDialog::javaClass.name)
        }
    }

    override fun onItemClick(item: MyVocabularyGroupEntity) {

    }

    override fun onCheckedChanged(item: MyVocabularyGroupEntity, isChecked: Boolean) {
        viewModel.addMyVocabularyToGroup(item, dictionaryEntity)
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
        myVocabularyAdapter = MyVocabularyGroupPickerAdapter(listOf(), listener = this)
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