package com.example.foreverfreedictionary.ui.adapter.delegate

import android.view.ViewGroup
import com.example.foreverfreedictionary.ui.adapter.viewholder.MyVocabularyGroupViewHolder
import com.example.foreverfreedictionary.ui.model.Entity
import com.example.foreverfreedictionary.ui.model.MyVocabularyGroupEntity
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate

class MyVocabularyGroupDelegate(private val listener: MyVocabularyGroupViewHolder.OnItemListeners?): AbsListItemAdapterDelegate<MyVocabularyGroupEntity, Entity, MyVocabularyGroupViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup): MyVocabularyGroupViewHolder {
        return MyVocabularyGroupViewHolder(parent, listener)
    }

    override fun isForViewType(item: Entity, items: MutableList<Entity>, position: Int): Boolean {
        return item is MyVocabularyGroupEntity
    }

    override fun onBindViewHolder(
        item: MyVocabularyGroupEntity,
        holder: MyVocabularyGroupViewHolder,
        payloads: MutableList<Any>
    ) {
        holder.bindView(item)
    }
}