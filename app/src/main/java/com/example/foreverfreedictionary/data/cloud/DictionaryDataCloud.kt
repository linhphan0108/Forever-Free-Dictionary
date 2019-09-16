package com.example.foreverfreedictionary.data.cloud

import com.example.foreverfreedictionary.domain.datasource.DictionaryDataDs
import com.example.foreverfreedictionary.util.DICTIONARY_URL
import com.example.foreverfreedictionary.util.DOMAIN
import com.example.foreverfreedictionary.vo.Resource
import org.jsoup.Jsoup

class DictionaryDataCloud : DictionaryDataDs {
        override suspend fun queryDictionaryData(query: String): Resource<String> {
        val url = if (query.contains('/') || query.contains('?')){
                        DOMAIN
                    }else {
                        DICTIONARY_URL
                    } + query
        val document = Jsoup.connect(url).get()
            val hasContent = document.selectFirst("div.entry_content") != null

        val result = if (document.selectFirst("div.page_content") !=null || hasContent){
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

        return Resource.success(result)
    }
}