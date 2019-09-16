package com.example.foreverfreedictionary.ui.adapter.diffUtil

import com.example.foreverfreedictionary.ui.adapter.base.AbsDiffUtilEntityItemCallbackDelegate
import com.example.foreverfreedictionary.ui.model.AutoCompletionEntity
import com.example.foreverfreedictionary.ui.model.Entity

class AutoCompletionDiffUtilCallback :
    AbsDiffUtilEntityItemCallbackDelegate<AutoCompletionEntity>() {
    override fun areContentsTheSame(
        oldItem: AutoCompletionEntity,
        newItem: AutoCompletionEntity
    ): Boolean {
        return oldItem == newItem
    }

    override fun getChangePayload(
        oldItem: AutoCompletionEntity,
        newItem: AutoCompletionEntity
    ): Any? {
        return null
    }

    override fun isForViewType(item: Entity): Boolean {
        return true
    }
}