package com.example.foreverfreedictionary.domain.command

import androidx.lifecycle.LiveData
import com.example.foreverfreedictionary.vo.Resource
import kotlinx.coroutines.CoroutineScope

abstract class BaseCommandScope <T> {
    abstract suspend fun execute(scope: CoroutineScope): LiveData<Resource<T>>
}