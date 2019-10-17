package com.example.foreverfreedictionary.ui.dialog

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import com.example.foreverfreedictionary.R
import com.example.foreverfreedictionary.di.injector
import com.example.foreverfreedictionary.di.viewModel
import com.example.foreverfreedictionary.vo.Status
import kotlinx.android.synthetic.main.dialog_new_vocabulary_group.*

class NewMyVocabularyGroupDialog : DialogFragment() {
    companion object{
        fun newInstance(): NewMyVocabularyGroupDialog {
            return NewMyVocabularyGroupDialog()
        }
    }

    private val viewmodel by viewModel(this){injector.newMyVocabularyGroupDialogViewModel}

    var listener: OnListeners? = null

    override fun onStart() {
        super.onStart()
        // remove black outer overlay, or change opacity
        dialog?.window?.also { window ->
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_new_vocabulary_group, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        registerViewModelListeners()
        edtGroupName.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                btnOk.isEnabled = !s.isNullOrBlank()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

        })
        btnOk.setOnClickListener{
            viewmodel.insertNewMyVocabularyGroup(edtGroupName.text.toString().trim())
        }
    }

    private fun registerViewModelListeners(){
        viewmodel.insertMyVocabularyGroupLiveData.observe(this, Observer { resource ->
            when(resource.status){
                Status.LOADING -> {}
                Status.ERROR -> {
                    listener?.onInsertGroupFailed(resource.message!!)
                    dismiss()
                }
                Status.SUCCESS -> {
                    listener?.onInsertGroupSuccess()
                    dismiss()
                }
            }
        })
    }

    interface OnListeners{
        fun onInsertGroupSuccess()
        fun onInsertGroupFailed(message: String)
    }
}