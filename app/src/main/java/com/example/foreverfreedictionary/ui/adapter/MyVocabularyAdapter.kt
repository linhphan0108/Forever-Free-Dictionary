package com.example.foreverfreedictionary.ui.adapter

import com.example.foreverfreedictionary.ui.adapter.base.AsyncListEntityDifferDelegationAdapter
import com.example.foreverfreedictionary.ui.adapter.delegate.MyVocabularyDelegate
import com.example.foreverfreedictionary.ui.adapter.delegate.MyVocabularyGroupDelegate
import com.example.foreverfreedictionary.ui.adapter.viewholder.MyVocabularyGroupViewHolder
import com.example.foreverfreedictionary.ui.adapter.viewholder.MyVocabularyViewHolder
import com.example.foreverfreedictionary.ui.model.Entity

class MyVocabularyAdapter(items: List<Entity>, listenerGroup: MyVocabularyGroupViewHolder.OnItemListeners? = null,
                          listener: MyVocabularyViewHolder.OnItemListeners? = null):  AsyncListEntityDifferDelegationAdapter() {
    init {
        delegatesManager.addDelegate(MyVocabularyGroupDelegate(listenerGroup))
        delegatesManager.addDelegate(MyVocabularyDelegate(listener))
        setItems(items)
    }
}