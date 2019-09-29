package com.example.foreverfreedictionary.ui.mapper

import com.example.foreverfreedictionary.ui.model.AutoCompletionEntity
import com.example.foreverfreedictionary.vo.Resource
import com.example.foreverfreedictionary.vo.Status

class AutoCompletionMapper {
    fun fromDomain(resource: Resource<List<String>?>) : Resource<List<AutoCompletionEntity>> {
        return when (resource.status) {
            Status.LOADING -> {
                Resource.loading()
            }
            Status.SUCCESS -> {
                val mappedData = resource.data!!.map {
                    AutoCompletionEntity(it)
                }
                Resource.success(mappedData)
            }
            Status.ERROR -> {
                Resource.error(resource.message)
            }
        }
    }
}