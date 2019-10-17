package com.example.foreverfreedictionary.ui.adapter

import com.example.foreverfreedictionary.ui.adapter.base.AsyncListEntityDifferDelegationAdapter
import com.example.foreverfreedictionary.ui.adapter.delegate.MyVocabularyGroupPickerDelegate
import com.example.foreverfreedictionary.ui.adapter.viewholder.MyVocabularyGroupPickerViewHolder
import com.example.foreverfreedictionary.ui.model.Entity

class MyVocabularyGroupPickerAdapter(
    items: List<Entity>,
    listener: MyVocabularyGroupPickerViewHolder.OnItemListeners):  AsyncListEntityDifferDelegationAdapter() {
    init {
        delegatesManager.addDelegate(MyVocabularyGroupPickerDelegate(listener))
        setItems(items)
    }
}