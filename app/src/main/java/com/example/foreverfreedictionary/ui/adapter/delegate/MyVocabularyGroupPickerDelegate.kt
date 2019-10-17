package com.example.foreverfreedictionary.ui.adapter.delegate

import android.view.ViewGroup
import com.example.foreverfreedictionary.ui.adapter.viewholder.MyVocabularyGroupPickerViewHolder
import com.example.foreverfreedictionary.ui.adapter.viewholder.MyVocabularyViewHolder
import com.example.foreverfreedictionary.ui.model.Entity
import com.example.foreverfreedictionary.ui.model.MyVocabularyEntity
import com.example.foreverfreedictionary.ui.model.MyVocabularyGroupEntity
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate

class MyVocabularyGroupPickerDelegate(private val listener: MyVocabularyGroupPickerViewHolder.OnItemListeners): AbsListItemAdapterDelegate<MyVocabularyGroupEntity, Entity, MyVocabularyGroupPickerViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup): MyVocabularyGroupPickerViewHolder {
        return MyVocabularyGroupPickerViewHolder(parent, listener)
    }

    override fun isForViewType(item: Entity, items: MutableList<Entity>, position: Int): Boolean {
        return item is MyVocabularyGroupEntity
    }

    override fun onBindViewHolder(
        item: MyVocabularyGroupEntity,
        holder: MyVocabularyGroupPickerViewHolder,
        payloads: MutableList<Any>
    ) {
        holder.bindView(item)
    }
}