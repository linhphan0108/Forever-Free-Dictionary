package com.example.foreverfreedictionary.ui.screen.result

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.foreverfreedictionary.ui.baseMVVM.BaseViewModel
import com.example.foreverfreedictionary.util.CSS
import com.example.foreverfreedictionary.util.DICTIONARY_URL
import com.example.foreverfreedictionary.util.JAVASCRIPT
import com.example.foreverfreedictionary.vo.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.jsoup.Jsoup
import javax.inject.Inject

class ResultActivityViewModel  @Inject constructor() : BaseViewModel(){

    private val _queryResponse = MutableLiveData<Resource<String>>()
    val queryResponse: LiveData<Resource<String>> = _queryResponse

    fun query(query: String){
        _queryResponse.value = Resource.loading()
        //Connect to website
        uiScope.launch {
            //Working on UI thread
            //Use dispatcher to switch between context
            val deferred = async(Dispatchers.Default) {
                //Working on background thread
                val url = DICTIONARY_URL + query
                Jsoup.connect(url).get()
            }
            //Working on UI thread
            val document = deferred.await()
            val element = document.selectFirst("div.entry_content")
            val content = if (element != null) {//get content successfully
                element.html()
            }else{
                val suggestionsElement = document.selectFirst("html body.search div.content div.responsive_cell6 div.page_content")
                if (suggestionsElement != null){
                    suggestionsElement.html()
                }else{
                    "Oops something went wrong"
                }
            }
            val data = CSS + JAVASCRIPT + content
            _queryResponse.value = Resource.success(data)
        }
    }
}