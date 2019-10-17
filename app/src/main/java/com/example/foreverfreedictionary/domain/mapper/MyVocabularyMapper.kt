package com.example.foreverfreedictionary.domain.mapper

import com.example.foreverfreedictionary.data.local.TblMyVocabulary
import com.example.foreverfreedictionary.data.local.model.MyVocabulary

class MyVocabularyMapper {
    fun fromData(data: List<TblMyVocabulary>, groupId: Long, groupName: String): List<MyVocabulary>{
        return data.map {
            MyVocabulary(groupId, groupName, it.query, it.word, it.soundBr, it.soundAme, it.ipaBr, it.ipaAme)
        }
    }
}