package com.example.foreverfreedictionary.ui.mapper

import com.example.foreverfreedictionary.data.local.TblMyVocabulary
import com.example.foreverfreedictionary.ui.model.DictionaryEntity
import com.example.foreverfreedictionary.data.local.model.MyVocabulary as MyVocabularyData
import com.example.foreverfreedictionary.ui.model.MyVocabularyEntity
import com.example.foreverfreedictionary.vo.Resource

class MyVocabularyMapper {
    fun fromDomain(resource: Resource<List<MyVocabularyData>>, groupName: String): Resource<List<MyVocabularyEntity>> {
        return resource.map<MyVocabularyData, MyVocabularyEntity> {
            MyVocabularyEntity(groupName, it.query, it.word, it.soundBr, it.soundAme, it.ipaBr, it.ipaAme)
        }
    }

    fun fromData(resource: Resource<List<TblMyVocabulary>>): Resource<List<MyVocabularyEntity>> {
        return resource.map<TblMyVocabulary, MyVocabularyEntity> {
            MyVocabularyEntity(it.groupName, it.query, it.word, it.soundBr, it.soundAme, it.ipaBr, it.ipaAme)
        }
    }

    fun toData(groupName: String, dictionaryEntity: DictionaryEntity)
    : TblMyVocabulary{
        return TblMyVocabulary(dictionaryEntity.query, dictionaryEntity.word,
            groupName, dictionaryEntity.soundBr, dictionaryEntity.soundAme,
            dictionaryEntity.ipaBr, dictionaryEntity.ipaAme)
    }
}