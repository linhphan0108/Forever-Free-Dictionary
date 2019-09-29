package com.example.foreverfreedictionary.domain.provider

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.foreverfreedictionary.data.cloud.ApiEmptyResponse
import com.example.foreverfreedictionary.data.cloud.ApiErrorResponse
import com.example.foreverfreedictionary.data.cloud.ApiResponse
import com.example.foreverfreedictionary.data.cloud.ApiSuccessResponse
import com.example.foreverfreedictionary.util.ThreadUtil
import com.example.foreverfreedictionary.vo.Resource

abstract class BaseProvider {
    /**
     * The database serves as the single source of truth.
     * Therefore UI can receive data updates from database only.
     * Function notify UI about:
     *
     * @see com.example.foreverfreedictionary.vo.Status.SUCCESS - with data from database
     * @see com.example.foreverfreedictionary.vo.Status.ERROR - if error has occurred from any source
     * @see com.example.foreverfreedictionary.vo.Status.LOADING
     */
    suspend fun <ResultType, RequestType> singleTruthSourceLiveData(
        dbCall: suspend () -> LiveData<ResultType>,
        cloudCall: suspend () -> ApiResponse<RequestType>,
        saveToDb: suspend (RequestType) -> Unit): LiveData<Resource<ResultType>> {

        val mediatorLiveData = MediatorLiveData<Resource<ResultType>>()
        mediatorLiveData.addSource(Transformations.map(dbCall.invoke()) {
            Resource.success(it)
        }) {
            mediatorLiveData.value = it
        }
        when (val apiResponse = cloudCall.invoke()) {
            is ApiErrorResponse -> {
                mediatorLiveData.postValue(Resource.error(apiResponse.errorMessage))
                ThreadUtil.checkNotMainThread()
            }
            is ApiEmptyResponse -> {

            }
            is ApiSuccessResponse -> {
                saveToDb(apiResponse.data)
                ThreadUtil.checkNotMainThread()
            }
        }
        return mediatorLiveData
    }

    suspend fun <T, A> firstSource(dbCall: suspend () -> T,
                                   cloudCall: suspend () -> ApiResponse<A>,
                                   mapper: suspend (ApiResponse<A>) -> Resource<T>
    ): LiveData<Resource<T>> {
        val local = dbCall.invoke()
        val resource: Resource<T> = if (local != null){
            Resource.success(local)
        }else{
            mapper(cloudCall.invoke())
        }
        return MutableLiveData<Resource<T>>().apply {
            postValue(resource)
        }
    }

    suspend fun <T, A> pushSources( databaseQuery: suspend () -> T,
                                    cloudCall: suspend () -> ApiResponse<A>,
                                    mapper: suspend (ApiResponse<A>) -> Resource<T>): Resource<T> {
        var result = Resource.success(databaseQuery.invoke())
        if (result.data == null){
            result = mapper(cloudCall.invoke())
        }
        return result
    }
}