package com.example.foreverfreedictionary.data.cloud

import com.example.foreverfreedictionary.data.cloud.model.Dictionary
import com.example.foreverfreedictionary.domain.datasource.DictionaryDataDs
import com.example.foreverfreedictionary.util.DICTIONARY_URL
import com.example.foreverfreedictionary.util.DOMAIN
import com.example.foreverfreedictionary.vo.Resource
import org.jsoup.Jsoup

class DictionaryDataCloud : DictionaryDataDs {
        override suspend fun queryDictionaryData(query: String): Resource<Dictionary> {
        val url = if (query.contains('/') || query.contains('?')){
                        DOMAIN + query
                    }else if(query.contains(' ')){
                        DICTIONARY_URL + query.replace(",", "").replace(" ", "-")
                    }else {
                        DICTIONARY_URL + query
                    }
        val document = Jsoup.connect(url).get()
            val hasContent = document.selectFirst("div.entry_content") != null

        val content = if (document.selectFirst("div.page_content") !=null || hasContent){
            //remove unnecessary elements
            document.select(".header").remove()
            document.select("#ad_leftslot_container").remove()
            document.select(".responsive_cell2").remove()
            document.select(".footer").remove()

            if (hasContent){
                document.select("link[href^=https://d27ucmmhxk51xv.cloudfront.net/external/fonts/font-awesome/4.2.0/css/font-awesome.min.css]")
                    .forEach { cssElement ->
                        cssElement.attr("href", "css/font-awesome.min.css")
                    }
            }

            document.html()
        }else{
            "Oops something went wrong"
        }
        val ipaBr = document.selectFirst("span.PRON").text()
        var ipaAme = document.selectFirst("span.AMEVARPRON")?.text()
        val soundBr = document.selectFirst("span.speaker.amefile")?.attr("data-src-mp3")
        val soundAme = document.selectFirst("span.speaker.brefile")?.attr("data-src-mp3")
        if (ipaAme?.startsWith("$") == true){
            ipaAme = ipaAme.substringAfter("$")
        }
        val dictionary = Dictionary(query, content, soundBr, soundAme, ipaBr, ipaAme, url)
        return Resource.success(dictionary)
    }
}