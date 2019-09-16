package com.example.foreverfreedictionary.ui.screen.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.foreverfreedictionary.ui.baseMVVM.BaseViewModel
import com.example.foreverfreedictionary.vo.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.jsoup.Jsoup
import javax.inject.Inject

const val url = "https://www.ldoceonline.com/"
class HomeViewModel@Inject constructor() : BaseViewModel() {

    private val _wordOfTheDayResponse = MutableLiveData<Resource<String>>()
    val wordOfTheDay: LiveData<Resource<String>> = _wordOfTheDayResponse

    fun fetchWordOfTheDay() {
        uiScope.launch {
            //Working on UI thread
            //Use dispatcher to switch between context
            val deferred = async(Dispatchers.Default) {
                //Working on background thread
                Jsoup.connect(url).get()
            }
            //Working on UI thread
            val document = deferred.await()

            //remove unnecessary elements
            document.select(".header").remove()
            document.select(".text_welcome").remove()
            document.select(".right_col").remove()
            document.select(".carousel").remove()
            document.select(".footer").remove()

            //remove border
            val leftColElement = document.selectFirst(".left_col")
            val middleColElement = document.selectFirst(".middle_col")
            val pictureOfTheDayElement = document.selectFirst("#iotd")
            val tcotw = document.selectFirst("#tcotw")
            pictureOfTheDayElement.attr("style", "border:none; border-bottom: 1px solid lightgray")
            pictureOfTheDayElement.selectFirst(".pictures").attr("style", "border:none")
            tcotw.attr("style", "border:none")
            leftColElement.selectFirst("#wotd").attr("style", "border:none; border-bottom: 1px solid lightgray")
            middleColElement.selectFirst("#hot_topics").attr("style", "border:none; border-bottom: 1px solid lightgray")

            //reorder topics
            leftColElement.insertChildren(0, pictureOfTheDayElement)
            middleColElement.insertChildren(2, tcotw)



            val data = document.html()

            _wordOfTheDayResponse.value = Resource.success(data)
        }
    }
}