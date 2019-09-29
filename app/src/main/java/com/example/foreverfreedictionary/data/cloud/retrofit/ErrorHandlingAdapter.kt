/*
 * Copyright (C) 2015 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.retrofit

import com.example.foreverfreedictionary.data.cloud.ApiResponse
import java.io.IOException
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.util.concurrent.Executor
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

/**
 * A sample showing a custom [CallAdapter] which adapts the built-in [Call] to a custom
 * version whose callback has more granular methods.
 */
object ErrorHandlingAdapter {
    /** A callback which offers granular callbacks for various conditions.  */
    internal interface MyCallback<T> {
        /** Called for [200, 300) responses.  */
        fun success(response: ApiResponse<T>)

        /** Called for 401 responses.  */
        fun unauthenticated(response: ApiResponse<*>)

        /** Called for [400, 500) responses, except 401.  */
        fun clientError(response: ApiResponse<*>)

        /** Called for [500, 600) response.  */
        fun serverError(response: ApiResponse<*>)

        /** Called for network errors while making the call.  */
        fun networkError(e: IOException)

        /** Called for unexpected errors while making the call.  */
        fun unexpectedError(t: Throwable)
    }

    internal interface MyCall<T> {
        fun cancel()
        fun enqueue(callback: MyCallback<T>)
        fun clone(): MyCall<T>

        // Left as an exercise for the reader...
        // TODO MyResponse<T> execute() throws MyHttpException;
    }

    class ErrorHandlingCallAdapterFactory : CallAdapter.Factory() {
        override fun get(
            returnType: Type, annotations: Array<Annotation>, retrofit: Retrofit
        ): CallAdapter<*, *>? {
            if (getRawType(returnType) != MyCall::class.java) {
                return null
            }
            check(returnType is ParameterizedType) { "MyCall must have generic type (e.g., MyCall<ResponseBody>)" }
            val responseType = getParameterUpperBound(0, returnType)
            val callbackExecutor = retrofit.callbackExecutor()
            return ErrorHandlingCallAdapter<Any>(responseType, callbackExecutor)
        }

        private class ErrorHandlingCallAdapter<R> internal constructor(
            private val responseType: Type,
            private val callbackExecutor: Executor?
        ) : CallAdapter<R, MyCall<R>> {

            override fun responseType(): Type {
                return responseType
            }

            override fun adapt(call: Call<R>): MyCall<R> {
                return MyCallAdapter(call, callbackExecutor)
            }
        }
    }

    /** Adapts a [Call] to [MyCall].  */
    internal class MyCallAdapter<T>(
        private val call: Call<T>,
        private val callbackExecutor: Executor?
    ) : MyCall<T> {

        override fun cancel() {
            call.cancel()
        }

        override fun enqueue(callback: MyCallback<T>) {
            call.enqueue(object : Callback<T> {
                override fun onResponse(call: Call<T>, response: Response<T>) {
                    // TODO if 'callbackExecutor' is not null, the 'callback' methods should be executed
                    // on that executor by submitting a Runnable. This is left as an exercise for the reader.

                    when (response.code()) {
                        in 200..299 -> callback.success(ApiResponse.create(response))
                        401 -> callback.unauthenticated(ApiResponse.create(response))
                        in 400..499 -> callback.clientError(ApiResponse.create(response))
                        in 500..599 -> callback.serverError(ApiResponse.create(response))
                        else -> callback.unexpectedError(RuntimeException("Unexpected response $response"))
                    }
                }

                override fun onFailure(call: Call<T>, t: Throwable) {
                    // TODO if 'callbackExecutor' is not null, the 'callback' methods should be executed
                    // on that executor by submitting a Runnable. This is left as an exercise for the reader.

                    if (t is IOException) {
                        callback.networkError(t)
                    } else {
                        callback.unexpectedError(t)
                    }
                }
            })
        }

        override fun clone(): MyCall<T> {
            return MyCallAdapter(call.clone(), callbackExecutor)
        }
    }
}
