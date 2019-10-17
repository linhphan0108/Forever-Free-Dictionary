package com.example.foreverfreedictionary.vo

import com.example.foreverfreedictionary.vo.Status.*

/**
 * A generic class that holds a value with its loading status.
 * @param <T>
</T> */
data class Resource<out T>(val status: Status, val data: T?, val message: String?) {
    companion object {
        fun <T> success(data: T?): Resource<T> {
            return Resource(SUCCESS, data, null)
        }

        fun <T> error(msg: String?, data: T? = null): Resource<T> {
            return Resource(ERROR, data, msg)
        }

        fun <T> loading(data: T? = null): Resource<T> {
            return Resource(LOADING, data, null)
        }
    }

    /**
     * transform from current list data into expected list data with the passed function
     */
    inline fun <reified E, R> map(mapper: (item: E) -> R): Resource<List<R>>{
        return when(status){
            LOADING -> {
                loading()
            }
            ERROR -> {
                error(message)
            }
            SUCCESS -> {
                val mappedData: List<R>? = if (data is List<*>){
                    data.map {
                        mapper.invoke(it as E)
                    }
                }else{
                    null
                }
                success(mappedData)
            }
        }
    }
}