package com.example.foreverfreedictionary.domain.provider

import androidx.lifecycle.LiveData
import com.example.foreverfreedictionary.data.cloud.ApiResponse
import com.example.foreverfreedictionary.data.local.TblMyVocabularyGroup
import com.example.foreverfreedictionary.data.local.room.MyVocabularyGroupDao
import com.example.foreverfreedictionary.vo.Resource
import javax.inject.Inject

class MyVocabularyGroupProvider @Inject constructor(
    private val myVocabularyGroupDao: MyVocabularyGroupDao
) : BaseProvider(){
    suspend fun insert(entity: TblMyVocabularyGroup): Resource<Long> {
        return pushSources(databaseQuery = {
            myVocabularyGroupDao.insert(entity)
        }, cloudCall = {
            ApiResponse.create(-1)
        }, mapper = {
            Resource.success(-1)
        })
    }

    suspend fun getAllMyVocabularyGroup(): LiveData<Resource<List<TblMyVocabularyGroup>>> {
        return singleTruthSourceLiveData(
            dbCall = {
                myVocabularyGroupDao.getAllMyVocabularyGroup()
            }, cloudCall = {
                ApiResponse.create<TblMyVocabularyGroup>(null)
            }, saveToDb = {
                Resource.success(null)
            }
        )
    }

    suspend fun delete(groupName: String): Resource<Int>{
        return pushSources(databaseQuery = {
            myVocabularyGroupDao.delete(groupName)
        }, cloudCall = {
            ApiResponse.create(0)
        }, mapper = {
            Resource.success(0)
        })
    }
}