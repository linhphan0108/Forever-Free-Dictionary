package com.example.foreverfreedictionary.data.cloud

import com.example.foreverfreedictionary.data.cloud.model.Dictionary
import com.example.foreverfreedictionary.util.*
import org.jsoup.Jsoup
import java.sql.Date
import java.util.*

class DictionaryDataCloud {
    suspend fun queryDictionaryData(query: String): ApiResponse<Dictionary> {
        try {
            var isTopicPage = false
            val url =
                if (query.contains("-topic/")){
                    isTopicPage = true
                    DOMAIN + query
                }else if(query.startsWith(SEARCH_DIRECTION_PATH) || query.startsWith(
                        SEARCH_ENGLISH_DIRECTION_PATH)){
                    DOMAIN + query
                }else {
                    SEARCH_FORM_SUBMIT_DIRECTION_URL + query
                }
            val response = Jsoup.connect(url).followRedirects(true).execute()
            val document = response.parse()
            val location = response.url().toExternalForm()
            val isDictionaryPage = location.startsWith(DICTIONARY_URL)
            val isCheckSpellPage = location.startsWith(CHECK_SPELL_URL)
            val lastAccess = Date(Calendar.getInstance().timeInMillis)

            return if (isDictionaryPage || isCheckSpellPage || isTopicPage) {
                //remove unnecessary elements
                document.select(".header")?.remove()
                document.select("#ad_leftslot_container")?.remove()
                document.select(".am-default")?.remove()
                document.select(".responsive_cell2")?.remove()
                document.select(".footer")?.remove()
                document.select("link[href^=https://d27ucmmhxk51xv.cloudfront.net/external/fonts/font-awesome/4.2.0/css/font-awesome.min.css]")
                    ?.forEach { cssElement ->
                        cssElement.attr("href", "css/font-awesome.min.css")
                    }


                val entryContent = document.selectFirst("div.entry_content")
                val content = document.html()
                val dictionary = if (isDictionaryPage || isTopicPage) {
                    var topic: String? = null
                    val word = if (isTopicPage){
                        val titleElement = entryContent.selectFirst(".topicpagetitle > span.span")
                        val topicElement = titleElement.selectFirst("a")
                        val title: String
                        if (topicElement == null){
                            title = titleElement.ownText().substringAfter("Topic:").trim()
                            topic = title
                        }else{
                            title = titleElement.ownText().substringBefore(" in").trim()
                            topic = topicElement.text().substringBefore(" topic").trim()
                        }
                        title
                    }else{
                        entryContent.selectFirst(".pagetitle").text()
                    }
                    val ipaBr = entryContent.selectFirst("span.PRON")?.text()
                    var ipaAme = entryContent.selectFirst("span.AMEVARPRON")?.text()
                    val soundBr =
                        entryContent.selectFirst("span.speaker.amefile")?.attr("data-src-mp3")
                    val soundAme =
                        entryContent.selectFirst("span.speaker.brefile")?.attr("data-src-mp3")
                    if (ipaAme?.startsWith("$") == true) {
                        ipaAme = ipaAme.substringAfter("$")
                    }
                    Dictionary(query, word, topic, isCheckSpellPage, content, soundBr, soundAme, ipaBr, ipaAme, url, lastAccess)
                } else {//the check spell page
                    Dictionary(query, query, null, isCheckSpellPage, content, null, null, null, null, location, lastAccess)
                }

                ApiResponse.create(dictionary)
            } else {
                ApiResponse.create<Dictionary>(null)
            }
        }catch (e: Exception){
            return ApiResponse.create(e)
        }
    }
}