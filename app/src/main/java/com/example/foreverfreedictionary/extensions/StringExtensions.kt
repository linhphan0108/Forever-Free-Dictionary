package com.example.foreverfreedictionary.extensions


fun String.trimFirstSpecialChar(): String{
    val trimmed = this.trim()
    if (trimmed.isBlank()) return ""
    val firstChar = trimmed.first()
    val isAlphabetChar = firstChar in 'a'..'z' || firstChar in 'A'..'Z'
    return if (!isAlphabetChar){
        if (trimmed.length > 2) {
            trimmed.substring(1, length - 1)
        }else{
            ""
        }
    }else{
        trimmed
    }
}

fun String.trimLastSpecialChar(): String{
    val trimmed = this.trim()
    if (trimmed.isBlank()) return ""
    val lastChar = trimmed.last()
    val isAlphabetChar = lastChar in 'a'..'z' || lastChar in 'A'..'Z'
    return if (!isAlphabetChar){
        if (trimmed.length > 2) {
            trimmed.substring(0, length - 2)
        }else{
            ""
        }
    }else{
        trimmed
    }
}