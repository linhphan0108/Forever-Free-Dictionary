package com.example.foreverfreedictionary.domain.provider

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Transformations
import com.example.foreverfreedictionary.util.ThreadUtil
import com.example.foreverfreedictionary.vo.Resource
import com.example.foreverfreedictionary.vo.Status.*

/**
 * The database serves as the single source of truth.
 * Therefore UI can receive data updates from database only.
 * Function notify UI about:
 *
 * @see com.example.foreverfreedictionary.vo.Status.SUCCESS - with data from database
 * @see com.example.foreverfreedictionary.vo.Status.ERROR - if error has occurred from any source
 * @see com.example.foreverfreedictionary.vo.Status.LOADING
 */
suspend fun <T, A> resultLiveData(databaseQuery: suspend () -> LiveData<T>,
                                  cloudCall: suspend () -> Resource<A>,
                                  saveCloudData: suspend (A) -> Unit): LiveData<Resource<T>>{
    val mediatorLiveData = MediatorLiveData<Resource<T>>()
    mediatorLiveData.addSource(Transformations.map(databaseQuery.invoke()) {
        Resource.success(it)
    }) {
        mediatorLiveData.value = it
    }
    val responseStatus = cloudCall.invoke()
    if (responseStatus.status == SUCCESS) {
        saveCloudData(responseStatus.data!!)
        ThreadUtil.checkNotMainThread()
    } else if (responseStatus.status == ERROR) {
        mediatorLiveData.postValue(Resource.error(responseStatus.message!!))
        ThreadUtil.checkNotMainThread()
    }

    return mediatorLiveData
}