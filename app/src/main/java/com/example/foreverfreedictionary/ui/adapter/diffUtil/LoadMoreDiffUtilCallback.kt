package com.example.foreverfreedictionary.ui.adapter.diffUtil

import com.example.foreverfreedictionary.ui.adapter.base.AbsDiffUtilEntityItemCallbackDelegate
import com.example.linh.vietkitchen.ui.model.Entity
import com.example.linh.vietkitchen.ui.model.LoadMoreItem

class LoadMoreDiffUtilCallback : AbsDiffUtilEntityItemCallbackDelegate<LoadMoreItem>() {
    override fun isForViewType(item: Entity): Boolean {
        return item is LoadMoreItem
    }

    override fun areContentsTheSame(oldItem: LoadMoreItem, newItem: LoadMoreItem): Boolean {
        return false
    }

    override fun getChangePayload(oldItem: LoadMoreItem, newItem: LoadMoreItem): Any? {
        return null
    }
}