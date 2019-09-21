package com.example.foreverfreedictionary.data.cloud

import com.example.foreverfreedictionary.data.cloud.model.Dictionary
import com.example.foreverfreedictionary.domain.datasource.DictionaryDataDs
import com.example.foreverfreedictionary.util.CHECK_SPELL_URL
import com.example.foreverfreedictionary.util.DICTIONARY_URL
import com.example.foreverfreedictionary.util.SEARCH_FORM_SUBMIT_DIRECTION_URL
import com.example.foreverfreedictionary.vo.Resource
import org.jsoup.Jsoup
import timber.log.Timber

class DictionaryDataCloud : DictionaryDataDs {
    override suspend fun queryDictionaryData(query: String): Resource<Dictionary> {
        try {
            val url =
//            if (query.contains('/') || query.contains('?')){
//                DOMAIN + query
//            }else if(query.contains(' ')){//query example: "want for something"
//                SEARCH_FORM_SUBMIT_DIRECTION_URL + query.replace(",", "").replace(" ", "-")
//            }else {
                SEARCH_FORM_SUBMIT_DIRECTION_URL + query
//            }
            val response = Jsoup.connect(url).followRedirects(true).execute()
            val document = response.parse()
            val location = response.url().toExternalForm()
            val isDictionaryPage = location.startsWith(DICTIONARY_URL)
            val isCheckSpellPage = location.startsWith(CHECK_SPELL_URL)

            return if (isDictionaryPage || isCheckSpellPage) {
                //remove unnecessary elements
                document.select(".header").remove()
                document.select("#ad_leftslot_container").remove()
                document.select(".responsive_cell2").remove()
                document.select(".footer").remove()
                document.select("link[href^=https://d27ucmmhxk51xv.cloudfront.net/external/fonts/font-awesome/4.2.0/css/font-awesome.min.css]")
                    ?.forEach { cssElement ->
                        cssElement.attr("href", "css/font-awesome.min.css")
                    }


                val entryContent = document.selectFirst("div.entry_content")
                val content = document.html()
                val dictionary = if (isDictionaryPage) {
                    val word = entryContent.selectFirst(".pagetitle").text()
                    val ipaBr = entryContent.selectFirst("span.PRON")?.text()
                    var ipaAme = entryContent.selectFirst("span.AMEVARPRON")?.text()
                    val soundBr =
                        entryContent.selectFirst("span.speaker.amefile")?.attr("data-src-mp3")
                    val soundAme =
                        entryContent.selectFirst("span.speaker.brefile")?.attr("data-src-mp3")
                    if (ipaAme?.startsWith("$") == true) {
                        ipaAme = ipaAme.substringAfter("$")
                    }
                    Dictionary(query, word, content, soundBr, soundAme, ipaBr, ipaAme, url)
                } else {//the check spell page
                    Dictionary(query, query, content, null, null, null, null, location)
                }

                Resource.success(dictionary)
            } else {
                Resource.error("Oops something went wrong")
            }
        }catch (e: Exception){
            Timber.e(e)
            return Resource.error("Oops something went wrong")
        }
    }
}