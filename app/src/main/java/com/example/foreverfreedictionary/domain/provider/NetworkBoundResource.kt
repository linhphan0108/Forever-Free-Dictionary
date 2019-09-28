/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.foreverfreedictionary.domain.provider

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import com.example.foreverfreedictionary.data.cloud.ApiEmptyResponse
import com.example.foreverfreedictionary.data.cloud.ApiErrorResponse
import com.example.foreverfreedictionary.data.cloud.ApiResponse
import com.example.foreverfreedictionary.data.cloud.ApiSuccessResponse
import com.example.foreverfreedictionary.vo.Resource

/**
 * A generic class that can provide a resource backed by both the sqlite database and the network.
 *
 *
 * You can read more about it in the [Architecture
 * Guide](https://developer.android.com/arch).
 * @param <ResultType>
 * @param <RequestType>
</RequestType></ResultType> */
abstract class NetworkBoundResource<ResultType, RequestType> {

    private val result = MediatorLiveData<Resource<ResultType>>()

    init{
        result.postValue(Resource.loading(null))
        @Suppress("LeakingThis")
        if (hasOfflineMode()) {
            val dbSource = loadFromDb()
            result.addSource(dbSource) { data ->
                result.removeSource(dbSource)
                if (shouldFetch(data)) {
                    fetchFromNetwork(dbSource)
                } else {
                    result.addSource(dbSource) { newData ->
                        setValue(Resource.success(newData))
                    }
                }
            }
        }else{
            fetchFromNetwork()
        }
    }

    @MainThread
    private fun setValue(newValue: Resource<ResultType>) {
        if (result.value != newValue) {
            result.postValue(newValue)
        }
    }

    private fun fetchFromNetwork(dbSource: LiveData<ResultType>) {
        val apiResponse = createCall()
        // we re-attach dbSource as a new source, it will dispatch its latest value quickly
        result.addSource(dbSource) { newData ->
            setValue(Resource.loading(newData))
        }
        result.addSource(apiResponse) { response ->
            result.removeSource(apiResponse)
            result.removeSource(dbSource)
            when (response) {
                is ApiSuccessResponse -> {
                    saveCallResult(processResponse(response))
                    // we specially request a new live data,
                    // otherwise we will get immediately last cached value,
                    // which may not be updated with latest results received from network.
                    result.addSource(loadFromDb()) { newData ->
                        setValue(Resource.success(newData))
                    }
                }
                is ApiEmptyResponse -> {
                    // reload from disk whatever we had
                    result.addSource(loadFromDb()) { newData ->
                        setValue(Resource.success(newData))
                    }
                }
                is ApiErrorResponse -> {
                    onFetchFailed()
                    result.addSource(dbSource) { newData ->
                        setValue(Resource.error(response.errorMessage, newData))
                    }
                }
            }
        }
    }

    /**
     * called when {@link #hasOfflineMode()} return false
     * means that we don't support database for this api
     */
    private fun fetchFromNetwork() {
        val apiResponse = createCall()
        result.addSource(apiResponse) { response ->
            result.removeSource(apiResponse)
            when (response) {
                is ApiSuccessResponse -> {
                    setValue(Resource.success(mapRequestTypeToResultType(processResponse(response))))
                }
                is ApiEmptyResponse -> {
                    setValue(Resource.success(mapRequestTypeToResultType(null)))
                }
                is ApiErrorResponse -> {
                    onFetchFailed()
                    setValue(Resource.error(response.errorMessage))
                }
            }
        }
    }

    protected open fun onFetchFailed() {}

    fun asLiveData() = result as LiveData<Resource<ResultType>>

    @WorkerThread
    protected open fun processResponse(response: ApiSuccessResponse<RequestType>) = response.body

    @WorkerThread
    protected abstract fun saveCallResult(item: RequestType)

    @MainThread
    protected abstract fun shouldFetch(data: ResultType?): Boolean

    @MainThread
    protected abstract fun loadFromDb(): LiveData<ResultType>

    @MainThread
    protected abstract fun createCall(): LiveData<ApiResponse<RequestType>>

    /**
     * in case we don't support local database it should return false
     * to request data from api immediately without requesting local database
     * otherwise shouldn't override this method
     */
    protected open fun hasOfflineMode():Boolean = true

    protected abstract fun mapRequestTypeToResultType(requestResult: RequestType?): ResultType
}
