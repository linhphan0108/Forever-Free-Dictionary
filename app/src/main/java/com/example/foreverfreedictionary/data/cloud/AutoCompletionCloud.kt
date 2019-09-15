package com.example.foreverfreedictionary.data.cloud

import com.example.foreverfreedictionary.domain.datasource.AutoCompletionDS
import com.example.foreverfreedictionary.vo.Resource
import javax.inject.Inject

class AutoCompletionCloud @Inject constructor(private val apiInterface: ApiInterface)
    : AutoCompletionDS {

    override suspend fun fetchAutoCompletion(query: String): Resource<List<String>> {
        return try {
            val primaryResponse = apiInterface.getSearchAutoCompletion(query)
            if (primaryResponse.isSuccessful){
                val results = primaryResponse.body()!!.results.map {
                    it.suggestion
                }
                Resource.success(results)
            }else{
                Resource.error(primaryResponse.errorBody().toString())
            }
        }catch (e: Exception){
            Resource.error(e.message)
        }
    }
}