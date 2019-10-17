package com.example.foreverfreedictionary.ui.mapper

import com.example.foreverfreedictionary.data.local.TblMyVocabulary
import com.example.foreverfreedictionary.data.local.TblMyVocabularyGroup
import com.example.foreverfreedictionary.ui.model.MyVocabularyGroupEntity
import com.example.foreverfreedictionary.vo.Resource

class MyVocabularyGroupMapper {
    fun fromDomain(resource: Resource<List<TblMyVocabularyGroup>>): Resource<List<MyVocabularyGroupEntity>> {
        return resource.map<TblMyVocabularyGroup, MyVocabularyGroupEntity> {
            MyVocabularyGroupEntity(it.name)
        }
    }

    fun fromDomain(groupResource: Resource<List<TblMyVocabularyGroup>>, myvocabularyResource: Resource<List<TblMyVocabulary>>): Resource<List<MyVocabularyGroupEntity>>{
        val myVocabularyData = myvocabularyResource.data
        return groupResource.map<TblMyVocabularyGroup, MyVocabularyGroupEntity> {
            val groupEntity = MyVocabularyGroupEntity(it.name)
            myVocabularyData?.forEach { myVocabulary ->
                if (myVocabulary.groupName == it.name){
                    groupEntity.isSelected = true
                }
            }
            groupEntity
        }
    }
}