package com.example.foreverfreedictionary.util

object WebViewUtil {
    fun extractQuery(url: String?): String? {
        return url?.let {stringUri ->
            when {
                stringUri.startsWith(DICTIONARY_URL) -> stringUri.substringAfterLast('/')
                stringUri.startsWith(DOMAIN) -> stringUri.substringAfterLast(DOMAIN)
                stringUri.startsWith(LOCAL_DICTIONARY_URL) -> stringUri.substringAfterLast('/')
                stringUri.startsWith(LOCAL_DOMAIN) -> stringUri.substringAfterLast(LOCAL_DOMAIN)
                else -> null
            }
        }
    }
}