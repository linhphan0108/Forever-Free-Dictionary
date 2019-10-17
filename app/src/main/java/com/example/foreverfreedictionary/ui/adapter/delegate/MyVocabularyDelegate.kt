package com.example.foreverfreedictionary.ui.adapter.delegate

import android.view.ViewGroup
import com.example.foreverfreedictionary.ui.adapter.viewholder.MyVocabularyViewHolder
import com.example.foreverfreedictionary.ui.model.Entity
import com.example.foreverfreedictionary.ui.model.MyVocabularyEntity
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate

class MyVocabularyDelegate(private val listener: MyVocabularyViewHolder.OnItemListeners?): AbsListItemAdapterDelegate<MyVocabularyEntity, Entity, MyVocabularyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup): MyVocabularyViewHolder {
        return MyVocabularyViewHolder(parent, listener)
    }

    override fun isForViewType(item: Entity, items: MutableList<Entity>, position: Int): Boolean {
        return item is MyVocabularyEntity
    }

    override fun onBindViewHolder(
        item: MyVocabularyEntity,
        holder: MyVocabularyViewHolder,
        payloads: MutableList<Any>
    ) {
        holder.bindView(item)
    }
}