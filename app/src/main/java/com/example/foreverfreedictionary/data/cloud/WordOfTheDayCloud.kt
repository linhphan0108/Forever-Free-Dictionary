package com.example.foreverfreedictionary.data.cloud

import com.example.foreverfreedictionary.ui.screen.home.url
import org.jsoup.Jsoup
import java.lang.Exception

class WordOfTheDayCloud{
    fun fetchWordOfTheDay(): ApiResponse<String> {
        try {
            val document = Jsoup.connect(url).get()

            //remove unnecessary elements
            document.select(".header").remove()
            document.select(".text_welcome").remove()
            document.select("#ad_topslot").remove()
            document.select(".right_col").remove()
            document.select(".carousel").remove()
            document.select(".footer").remove()

            //remove border
            val leftColElement = document.selectFirst(".left_col")
            val middleColElement = document.selectFirst(".middle_col")
            val pictureOfTheDayElement = document.selectFirst("#iotd")
            val tcotw = document.selectFirst("#tcotw")
            pictureOfTheDayElement.attr(
                "style",
                "border:none; border-bottom: 1px solid lightgray; margin-top:0px"
            )
            pictureOfTheDayElement.selectFirst(".pictures").attr("style", "border:none")
            tcotw.attr("style", "border:none")
            leftColElement.selectFirst("#wotd")
                .attr("style", "border:none; border-bottom: 1px solid lightgray")
            middleColElement.selectFirst("#hot_topics")
                .attr("style", "border:none; border-bottom: 1px solid lightgray")

            //reorder topics
            leftColElement.insertChildren(0, pictureOfTheDayElement)
            middleColElement.insertChildren(2, tcotw)

            return ApiResponse.create(document.html())
        }catch (e: Exception){
            return ApiResponse.create(e)
        }
    }
}