package com.example.foreverfreedictionary.domain.provider

import androidx.lifecycle.LiveData
import com.example.foreverfreedictionary.data.cloud.ApiResponse
import com.example.foreverfreedictionary.data.local.TblMyVocabulary
import com.example.foreverfreedictionary.data.local.room.MyVocabularyDao
import com.example.foreverfreedictionary.vo.Resource
import javax.inject.Inject

class MyVocabularyProvider @Inject constructor(
    private val myVocabularyDao: MyVocabularyDao
) : BaseProvider() {
    suspend fun insert(entity: TblMyVocabulary) : Resource<Long>{
        return pushSources(databaseQuery = {
            myVocabularyDao.insert(entity)
        }, cloudCall = {
            ApiResponse.create(-1)
        }, mapper = {
            Resource.success(-1)
        })
    }

    suspend fun getAllMyVocabulary(groupName: String): LiveData<Resource<List<TblMyVocabulary>>> {
        return singleTruthSourceLiveData(
            dbCall = {
                myVocabularyDao.getAllMyVocabulary(groupName)
            }, cloudCall = {
                ApiResponse.create<TblMyVocabulary>(null)
            }, saveToDb = {
                Resource.success(null)
            }
        )
    }

    suspend fun getAllMyVocabularyByQuery(query: String): LiveData<Resource<List<TblMyVocabulary>>> {
        return singleTruthSourceLiveData(
            dbCall = {
                myVocabularyDao.getAllMyVocabularyByQuery(query)
            }, cloudCall = {
                ApiResponse.create<TblMyVocabulary>(null)
            }, saveToDb = {
                Resource.success(null)
            }
        )
    }

    suspend fun delete(query: String, groupName: String): Resource<Int> {
        return pushSources(databaseQuery = {
            myVocabularyDao.delete(query, groupName)
        }, cloudCall = {
            ApiResponse.create(0)
        }, mapper = {
            Resource.success(0)
        })
    }
}